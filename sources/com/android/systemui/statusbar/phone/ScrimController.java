package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.util.function.TriConsumer;
import com.android.keyguard.BouncerPanelExpansionCalculator;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$id;
import com.android.systemui.animation.ShadeInterpolation;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.AlarmTimeout;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class ScrimController implements ViewTreeObserver.OnPreDrawListener, Dumpable {
    public static final boolean DEBUG = Log.isLoggable("ScrimController", 3);
    public static final int TAG_END_ALPHA = R$id.scrim_alpha_end;
    public static final int TAG_KEY_ANIM = R$id.scrim;
    public static final int TAG_START_ALPHA = R$id.scrim_alpha_start;
    public float mAdditionalScrimBehindAlphaKeyguard = 0.0f;
    public boolean mAnimateChange;
    public boolean mAnimatingPanelExpansionOnUnlock;
    public long mAnimationDelay;
    public long mAnimationDuration = -1;
    public Animator.AnimatorListener mAnimatorListener;
    public float mBehindAlpha = -1.0f;
    public int mBehindTint;
    public boolean mBlankScreen;
    public Runnable mBlankingTransitionRunnable;
    public float mBouncerHiddenFraction = 1.0f;
    public Callback mCallback;
    public boolean mClipsQsScrim;
    public ColorExtractor.GradientColors mColors;
    public boolean mDarkenWhileDragging;
    public final float mDefaultScrimAlpha;
    public final DockManager mDockManager;
    public final DozeParameters mDozeParameters;
    public boolean mExpansionAffectsAlpha = true;
    public final Handler mHandler;
    public float mInFrontAlpha = -1.0f;
    public int mInFrontTint;
    public final Interpolator mInterpolator = new DecelerateInterpolator();
    public boolean mKeyguardOccluded;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardVisibilityCallback mKeyguardVisibilityCallback;
    public final Executor mMainExecutor;
    public boolean mNeedsDrawableColorUpdate;
    public float mNotificationsAlpha = -1.0f;
    public ScrimView mNotificationsScrim;
    public int mNotificationsTint;
    public float mPanelExpansionFraction = 1.0f;
    public float mPanelScrimMinFraction;
    public Runnable mPendingFrameCallback;
    public boolean mQsBottomVisible;
    public float mQsExpansion;
    public float mRawPanelExpansionFraction;
    public boolean mScreenBlankingCallbackCalled;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public boolean mScreenOn;
    public ScrimView mScrimBehind;
    public float mScrimBehindAlphaKeyguard = 0.2f;
    public Runnable mScrimBehindChangeRunnable;
    public ScrimView mScrimInFront;
    public final TriConsumer<ScrimState, Float, ColorExtractor.GradientColors> mScrimStateListener;
    public Consumer<Integer> mScrimVisibleListener;
    public int mScrimsVisibility;
    public ScrimState mState = ScrimState.UNINITIALIZED;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final AlarmTimeout mTimeTicker;
    public boolean mTracking;
    public float mTransitionToFullShadeProgress;
    public float mTransitionToLockScreenFullShadeNotificationsProgress;
    public boolean mTransitioningToFullShade;
    public boolean mUnOcclusionAnimationRunning;
    public boolean mUpdatePending;
    public final WakeLock mWakeLock;
    public boolean mWakeLockHeld;
    public boolean mWallpaperSupportsAmbientMode;
    public boolean mWallpaperVisibilityTimedOut;

    public interface Callback {
        void onCancelled() {
        }

        void onDisplayBlanked() {
        }

        void onFinished() {
        }

        void onStart() {
        }
    }

    public void setCurrentUser(int i) {
    }

    public void setUnocclusionAnimationRunning(boolean z) {
        this.mUnOcclusionAnimationRunning = z;
    }

    public ScrimController(LightBarController lightBarController, DozeParameters dozeParameters, AlarmManager alarmManager, final KeyguardStateController keyguardStateController, DelayedWakeLock.Builder builder, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, ConfigurationController configurationController, Executor executor, ScreenOffAnimationController screenOffAnimationController, PanelExpansionStateManager panelExpansionStateManager, KeyguardUnlockAnimationController keyguardUnlockAnimationController, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        Objects.requireNonNull(lightBarController);
        LightBarController lightBarController2 = lightBarController;
        this.mScrimStateListener = new ScrimController$$ExternalSyntheticLambda0(lightBarController);
        this.mDefaultScrimAlpha = 1.0f;
        this.mKeyguardStateController = keyguardStateController;
        this.mDarkenWhileDragging = !keyguardStateController.canDismissLockScreen();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardVisibilityCallback = new KeyguardVisibilityCallback();
        this.mHandler = handler;
        this.mMainExecutor = executor;
        this.mScreenOffAnimationController = screenOffAnimationController;
        AlarmManager alarmManager2 = alarmManager;
        this.mTimeTicker = new AlarmTimeout(alarmManager, new ScrimController$$ExternalSyntheticLambda1(this), "hide_aod_wallpaper", handler);
        this.mWakeLock = builder.setHandler(handler).setTag("Scrims").build();
        this.mDozeParameters = dozeParameters;
        this.mDockManager = dockManager;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        keyguardStateController.addCallback(new KeyguardStateController.Callback() {
            public void onKeyguardFadingAwayChanged() {
                ScrimController.this.setKeyguardFadingAway(keyguardStateController.isKeyguardFadingAway(), keyguardStateController.getKeyguardFadingAwayDuration());
            }
        });
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onThemeChanged() {
                ScrimController.this.onThemeChanged();
            }

            public void onUiModeChanged() {
                ScrimController.this.onThemeChanged();
            }
        });
        panelExpansionStateManager.addExpansionListener(new ScrimController$$ExternalSyntheticLambda2(this));
        this.mColors = new ColorExtractor.GradientColors();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(PanelExpansionChangeEvent panelExpansionChangeEvent) {
        setRawPanelExpansionFraction(panelExpansionChangeEvent.getFraction());
    }

    public void attachViews(ScrimView scrimView, ScrimView scrimView2, ScrimView scrimView3) {
        this.mNotificationsScrim = scrimView2;
        this.mScrimBehind = scrimView;
        this.mScrimInFront = scrimView3;
        updateThemeColors();
        scrimView.enableBottomEdgeConcave(this.mClipsQsScrim);
        this.mNotificationsScrim.enableRoundedCorners(true);
        Runnable runnable = this.mScrimBehindChangeRunnable;
        if (runnable != null) {
            this.mScrimBehind.setChangeRunnable(runnable, this.mMainExecutor);
            this.mScrimBehindChangeRunnable = null;
        }
        ScrimState[] values = ScrimState.values();
        for (int i = 0; i < values.length; i++) {
            values[i].init(this.mScrimInFront, this.mScrimBehind, this.mDozeParameters, this.mDockManager);
            values[i].setScrimBehindAlphaKeyguard(this.mScrimBehindAlphaKeyguard);
            values[i].setDefaultScrimAlpha(this.mDefaultScrimAlpha);
        }
        this.mScrimBehind.setDefaultFocusHighlightEnabled(false);
        this.mNotificationsScrim.setDefaultFocusHighlightEnabled(false);
        this.mScrimInFront.setDefaultFocusHighlightEnabled(false);
        updateScrims();
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardVisibilityCallback);
    }

    public void setScrimCornerRadius(int i) {
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView != null && this.mNotificationsScrim != null) {
            scrimView.setCornerRadius(i);
            this.mNotificationsScrim.setCornerRadius(i);
        }
    }

    public void setScrimVisibleListener(Consumer<Integer> consumer) {
        this.mScrimVisibleListener = consumer;
    }

    public void transitionTo(ScrimState scrimState) {
        transitionTo(scrimState, (Callback) null);
    }

    public void transitionTo(ScrimState scrimState, Callback callback) {
        if (scrimState != this.mState) {
            if (DEBUG) {
                Log.d("ScrimController", "State changed to: " + scrimState);
            }
            if (scrimState != ScrimState.UNINITIALIZED) {
                ScrimState scrimState2 = this.mState;
                this.mState = scrimState;
                Trace.traceCounter(4096, "scrim_state", scrimState.ordinal());
                Callback callback2 = this.mCallback;
                if (callback2 != null) {
                    callback2.onCancelled();
                }
                this.mCallback = callback;
                scrimState.prepare(scrimState2);
                this.mScreenBlankingCallbackCalled = false;
                this.mAnimationDelay = 0;
                this.mBlankScreen = scrimState.getBlanksScreen();
                this.mAnimateChange = scrimState.getAnimateChange();
                this.mAnimationDuration = scrimState.getAnimationDuration();
                applyState();
                boolean z = true;
                this.mScrimInFront.setFocusable(!scrimState.isLowPowerState());
                this.mScrimBehind.setFocusable(!scrimState.isLowPowerState());
                this.mNotificationsScrim.setFocusable(!scrimState.isLowPowerState());
                this.mScrimInFront.setBlendWithMainColor(scrimState.shouldBlendWithMainColor());
                Runnable runnable = this.mPendingFrameCallback;
                if (runnable != null) {
                    this.mScrimBehind.removeCallbacks(runnable);
                    this.mPendingFrameCallback = null;
                }
                if (this.mHandler.hasCallbacks(this.mBlankingTransitionRunnable)) {
                    this.mHandler.removeCallbacks(this.mBlankingTransitionRunnable);
                    this.mBlankingTransitionRunnable = null;
                }
                if (scrimState == ScrimState.BRIGHTNESS_MIRROR) {
                    z = false;
                }
                this.mNeedsDrawableColorUpdate = z;
                if (this.mState.isLowPowerState()) {
                    holdWakeLock();
                }
                this.mWallpaperVisibilityTimedOut = false;
                if (shouldFadeAwayWallpaper()) {
                    DejankUtils.postAfterTraversal(new ScrimController$$ExternalSyntheticLambda3(this));
                } else {
                    AlarmTimeout alarmTimeout = this.mTimeTicker;
                    Objects.requireNonNull(alarmTimeout);
                    DejankUtils.postAfterTraversal(new ScrimController$$ExternalSyntheticLambda4(alarmTimeout));
                }
                if (!this.mKeyguardUpdateMonitor.needsSlowUnlockTransition() || this.mState != ScrimState.UNLOCKED) {
                    ScrimState scrimState3 = ScrimState.AOD;
                    if (((scrimState2 == scrimState3 || scrimState2 == ScrimState.PULSING) && (!this.mDozeParameters.getAlwaysOn() || this.mState == ScrimState.UNLOCKED)) || (this.mState == scrimState3 && !this.mDozeParameters.getDisplayNeedsBlanking())) {
                        onPreDraw();
                    } else {
                        scheduleUpdate();
                    }
                } else {
                    this.mAnimationDelay = 100;
                    scheduleUpdate();
                }
                dispatchBackScrimState(this.mScrimBehind.getViewAlpha());
                return;
            }
            throw new IllegalArgumentException("Cannot change to UNINITIALIZED.");
        } else if (callback != null && this.mCallback != callback) {
            callback.onFinished();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$transitionTo$1() {
        this.mTimeTicker.schedule(this.mDozeParameters.getWallpaperAodDuration(), 1);
    }

    public final boolean shouldFadeAwayWallpaper() {
        if (this.mWallpaperSupportsAmbientMode && this.mState == ScrimState.AOD && (this.mDozeParameters.getAlwaysOn() || this.mDockManager.isDocked())) {
            return true;
        }
        return false;
    }

    public ScrimState getState() {
        return this.mState;
    }

    public void setAdditionalScrimBehindAlphaKeyguard(float f) {
        this.mAdditionalScrimBehindAlphaKeyguard = f;
    }

    public void applyCompositeAlphaOnScrimBehindKeyguard() {
        setScrimBehindValues(((float) ColorUtils.compositeAlpha((int) (this.mAdditionalScrimBehindAlphaKeyguard * 255.0f), 51)) / 255.0f);
    }

    public final void setScrimBehindValues(float f) {
        this.mScrimBehindAlphaKeyguard = f;
        ScrimState[] values = ScrimState.values();
        for (ScrimState scrimBehindAlphaKeyguard : values) {
            scrimBehindAlphaKeyguard.setScrimBehindAlphaKeyguard(f);
        }
        scheduleUpdate();
    }

    public void onTrackingStarted() {
        this.mTracking = true;
        this.mDarkenWhileDragging = true ^ this.mKeyguardStateController.canDismissLockScreen();
        if (!this.mKeyguardUnlockAnimationController.isPlayingCannedUnlockAnimation()) {
            this.mAnimatingPanelExpansionOnUnlock = false;
        }
    }

    public void onExpandingFinished() {
        this.mTracking = false;
        setUnocclusionAnimationRunning(false);
    }

    @VisibleForTesting
    public void onHideWallpaperTimeout() {
        ScrimState scrimState = this.mState;
        if (scrimState == ScrimState.AOD || scrimState == ScrimState.PULSING) {
            holdWakeLock();
            this.mWallpaperVisibilityTimedOut = true;
            this.mAnimateChange = true;
            this.mAnimationDuration = this.mDozeParameters.getWallpaperFadeOutDuration();
            scheduleUpdate();
        }
    }

    public final void holdWakeLock() {
        if (!this.mWakeLockHeld) {
            WakeLock wakeLock = this.mWakeLock;
            if (wakeLock != null) {
                this.mWakeLockHeld = true;
                wakeLock.acquire("ScrimController");
                return;
            }
            Log.w("ScrimController", "Cannot hold wake lock, it has not been set yet");
        }
    }

    @VisibleForTesting
    public void setRawPanelExpansionFraction(float f) {
        if (!Float.isNaN(f)) {
            this.mRawPanelExpansionFraction = f;
            calculateAndUpdatePanelExpansion();
            return;
        }
        throw new IllegalArgumentException("rawPanelExpansionFraction should not be NaN");
    }

    public void setPanelScrimMinFraction(float f) {
        if (!Float.isNaN(f)) {
            this.mPanelScrimMinFraction = f;
            calculateAndUpdatePanelExpansion();
            return;
        }
        throw new IllegalArgumentException("minFraction should not be NaN");
    }

    public final void calculateAndUpdatePanelExpansion() {
        float f = this.mRawPanelExpansionFraction;
        float f2 = this.mPanelScrimMinFraction;
        if (f2 < 1.0f) {
            f = Math.max((f - f2) / (1.0f - f2), 0.0f);
        }
        if (this.mPanelExpansionFraction != f) {
            int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
            boolean z = true;
            if (i != 0 && this.mKeyguardUnlockAnimationController.isPlayingCannedUnlockAnimation()) {
                this.mAnimatingPanelExpansionOnUnlock = true;
            } else if (i == 0) {
                this.mAnimatingPanelExpansionOnUnlock = false;
            }
            this.mPanelExpansionFraction = f;
            ScrimState scrimState = this.mState;
            if (!(scrimState == ScrimState.UNLOCKED || scrimState == ScrimState.KEYGUARD || scrimState == ScrimState.DREAMING || scrimState == ScrimState.SHADE_LOCKED || scrimState == ScrimState.PULSING)) {
                z = false;
            }
            if (z && this.mExpansionAffectsAlpha && !this.mAnimatingPanelExpansionOnUnlock) {
                applyAndDispatchState();
            }
        }
    }

    public void setTransitionToFullShadeProgress(float f, float f2) {
        if (f != this.mTransitionToFullShadeProgress || f2 != this.mTransitionToLockScreenFullShadeNotificationsProgress) {
            this.mTransitionToFullShadeProgress = f;
            this.mTransitionToLockScreenFullShadeNotificationsProgress = f2;
            setTransitionToFullShade(f > 0.0f || f2 > 0.0f);
            applyAndDispatchState();
        }
    }

    public final void setTransitionToFullShade(boolean z) {
        if (z != this.mTransitioningToFullShade) {
            this.mTransitioningToFullShade = z;
            if (z) {
                ScrimState.SHADE_LOCKED.prepare(this.mState);
            }
        }
    }

    public void setNotificationsBounds(float f, float f2, float f3, float f4) {
        if (this.mClipsQsScrim) {
            this.mNotificationsScrim.setDrawableBounds(f - 1.0f, f2, f3 + 1.0f, f4);
            this.mScrimBehind.setBottomEdgePosition((int) f2);
            return;
        }
        this.mNotificationsScrim.setDrawableBounds(f, f2, f3, f4);
    }

    public void setNotificationsOverScrollAmount(int i) {
        this.mNotificationsScrim.setTranslationY((float) i);
    }

    public void setQsPosition(float f, int i) {
        if (!Float.isNaN(f)) {
            float notificationScrimAlpha = ShadeInterpolation.getNotificationScrimAlpha(f);
            boolean z = true;
            boolean z2 = i > 0;
            if (this.mQsExpansion != notificationScrimAlpha || this.mQsBottomVisible != z2) {
                this.mQsExpansion = notificationScrimAlpha;
                this.mQsBottomVisible = z2;
                ScrimState scrimState = this.mState;
                if (!(scrimState == ScrimState.SHADE_LOCKED || scrimState == ScrimState.KEYGUARD || scrimState == ScrimState.PULSING)) {
                    z = false;
                }
                if (z && this.mExpansionAffectsAlpha) {
                    applyAndDispatchState();
                }
            }
        }
    }

    public void setBouncerHiddenFraction(float f) {
        if (this.mBouncerHiddenFraction != f) {
            this.mBouncerHiddenFraction = f;
            if (this.mState == ScrimState.DREAMING) {
                applyAndDispatchState();
            }
        }
    }

    public void setClipsQsScrim(boolean z) {
        if (z != this.mClipsQsScrim) {
            this.mClipsQsScrim = z;
            for (ScrimState clipQsScrim : ScrimState.values()) {
                clipQsScrim.setClipQsScrim(this.mClipsQsScrim);
            }
            ScrimView scrimView = this.mScrimBehind;
            if (scrimView != null) {
                scrimView.enableBottomEdgeConcave(this.mClipsQsScrim);
            }
            ScrimState scrimState = this.mState;
            if (scrimState != ScrimState.UNINITIALIZED) {
                scrimState.prepare(scrimState);
                applyAndDispatchState();
            }
        }
    }

    @VisibleForTesting
    public boolean getClipQsScrim() {
        return this.mClipsQsScrim;
    }

    public final void setOrAdaptCurrentAnimation(View view) {
        if (view != null) {
            float currentScrimAlpha = getCurrentScrimAlpha(view);
            boolean z = view == this.mScrimBehind && this.mQsBottomVisible;
            if (!isAnimating(view) || z) {
                updateScrimColor(view, currentScrimAlpha, getCurrentScrimTint(view));
                return;
            }
            ValueAnimator valueAnimator = (ValueAnimator) view.getTag(TAG_KEY_ANIM);
            int i = TAG_END_ALPHA;
            float floatValue = ((Float) view.getTag(i)).floatValue();
            int i2 = TAG_START_ALPHA;
            view.setTag(i2, Float.valueOf(((Float) view.getTag(i2)).floatValue() + (currentScrimAlpha - floatValue)));
            view.setTag(i, Float.valueOf(currentScrimAlpha));
            valueAnimator.setCurrentPlayTime(valueAnimator.getCurrentPlayTime());
        }
    }

    public final void applyState() {
        this.mInFrontTint = this.mState.getFrontTint();
        this.mBehindTint = this.mState.getBehindTint();
        this.mNotificationsTint = this.mState.getNotifTint();
        this.mInFrontAlpha = this.mState.getFrontAlpha();
        this.mBehindAlpha = this.mState.getBehindAlpha();
        this.mNotificationsAlpha = this.mState.getNotifAlpha();
        assertAlphasValid();
        if (this.mExpansionAffectsAlpha) {
            ScrimState scrimState = this.mState;
            ScrimState scrimState2 = ScrimState.UNLOCKED;
            if (scrimState == scrimState2 || scrimState == ScrimState.DREAMING) {
                if (!this.mScreenOffAnimationController.shouldExpandNotifications() && !this.mAnimatingPanelExpansionOnUnlock) {
                    float pow = (float) Math.pow((double) getInterpolatedFraction(), 0.800000011920929d);
                    if (this.mClipsQsScrim) {
                        this.mBehindAlpha = 1.0f;
                        this.mNotificationsAlpha = pow * this.mDefaultScrimAlpha;
                    } else {
                        this.mBehindAlpha = pow * this.mDefaultScrimAlpha;
                        this.mNotificationsAlpha = MathUtils.constrainedMap(0.0f, 1.0f, 0.3f, 0.75f, this.mPanelExpansionFraction);
                    }
                    this.mBehindTint = this.mState.getBehindTint();
                    this.mInFrontAlpha = 0.0f;
                }
                float f = this.mBouncerHiddenFraction;
                if (f != 1.0f) {
                    float aboutToShowBouncerProgress = BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(f);
                    this.mBehindAlpha = MathUtils.lerp(this.mDefaultScrimAlpha, this.mBehindAlpha, aboutToShowBouncerProgress);
                    this.mBehindTint = ColorUtils.blendARGB(ScrimState.BOUNCER.getBehindTint(), this.mBehindTint, aboutToShowBouncerProgress);
                }
            } else if (scrimState == ScrimState.AUTH_SCRIMMED_SHADE) {
                float pow2 = ((float) Math.pow((double) getInterpolatedFraction(), 0.800000011920929d)) * this.mDefaultScrimAlpha;
                this.mBehindAlpha = pow2;
                this.mNotificationsAlpha = pow2;
                if (this.mClipsQsScrim) {
                    this.mBehindAlpha = 1.0f;
                    this.mBehindTint = -16777216;
                }
            } else {
                ScrimState scrimState3 = ScrimState.KEYGUARD;
                if (scrimState == scrimState3 || scrimState == ScrimState.SHADE_LOCKED || scrimState == ScrimState.PULSING) {
                    Pair<Integer, Float> calculateBackStateForState = calculateBackStateForState(scrimState);
                    int intValue = ((Integer) calculateBackStateForState.first).intValue();
                    float floatValue = ((Float) calculateBackStateForState.second).floatValue();
                    if (this.mTransitionToFullShadeProgress > 0.0f) {
                        Pair<Integer, Float> calculateBackStateForState2 = calculateBackStateForState(ScrimState.SHADE_LOCKED);
                        floatValue = MathUtils.lerp(floatValue, ((Float) calculateBackStateForState2.second).floatValue(), this.mTransitionToFullShadeProgress);
                        intValue = ColorUtils.blendARGB(intValue, ((Integer) calculateBackStateForState2.first).intValue(), this.mTransitionToFullShadeProgress);
                    }
                    this.mInFrontAlpha = this.mState.getFrontAlpha();
                    if (this.mClipsQsScrim) {
                        this.mNotificationsAlpha = floatValue;
                        this.mNotificationsTint = intValue;
                        this.mBehindAlpha = 1.0f;
                        this.mBehindTint = -16777216;
                    } else {
                        this.mBehindAlpha = floatValue;
                        if (this.mState == ScrimState.SHADE_LOCKED) {
                            this.mNotificationsAlpha = getInterpolatedFraction();
                        } else {
                            this.mNotificationsAlpha = Math.max(1.0f - getInterpolatedFraction(), this.mQsExpansion);
                        }
                        if (this.mState == scrimState3 && this.mTransitionToLockScreenFullShadeNotificationsProgress > 0.0f) {
                            this.mNotificationsAlpha = MathUtils.lerp(this.mNotificationsAlpha, getInterpolatedFraction(), this.mTransitionToLockScreenFullShadeNotificationsProgress);
                        }
                        this.mNotificationsTint = this.mState.getNotifTint();
                        this.mBehindTint = intValue;
                    }
                    ScrimState scrimState4 = this.mState;
                    boolean z = scrimState4 == scrimState3 && this.mTransitionToFullShadeProgress == 0.0f && this.mQsExpansion == 0.0f && !this.mClipsQsScrim;
                    if (this.mKeyguardOccluded || z) {
                        this.mNotificationsAlpha = 0.0f;
                    }
                    if (this.mUnOcclusionAnimationRunning && scrimState4 == scrimState3) {
                        this.mNotificationsAlpha = scrimState3.getNotifAlpha();
                        this.mNotificationsTint = scrimState3.getNotifTint();
                        this.mBehindAlpha = scrimState3.getBehindAlpha();
                        this.mBehindTint = scrimState3.getBehindTint();
                    }
                }
            }
            if (this.mState != scrimState2) {
                this.mAnimatingPanelExpansionOnUnlock = false;
            }
            assertAlphasValid();
        }
    }

    public final void assertAlphasValid() {
        if (Float.isNaN(this.mBehindAlpha) || Float.isNaN(this.mInFrontAlpha) || Float.isNaN(this.mNotificationsAlpha)) {
            throw new IllegalStateException("Scrim opacity is NaN for state: " + this.mState + ", front: " + this.mInFrontAlpha + ", back: " + this.mBehindAlpha + ", notif: " + this.mNotificationsAlpha);
        }
    }

    public final Pair<Integer, Float> calculateBackStateForState(ScrimState scrimState) {
        float f;
        int i;
        int i2;
        float interpolatedFraction = getInterpolatedFraction();
        float notifAlpha = this.mClipsQsScrim ? scrimState.getNotifAlpha() : scrimState.getBehindAlpha();
        float f2 = 0.0f;
        if (this.mDarkenWhileDragging) {
            f = MathUtils.lerp(this.mDefaultScrimAlpha, notifAlpha, interpolatedFraction);
        } else {
            f = MathUtils.lerp(0.0f, notifAlpha, interpolatedFraction);
        }
        if (this.mClipsQsScrim) {
            i = ColorUtils.blendARGB(ScrimState.BOUNCER.getNotifTint(), scrimState.getNotifTint(), interpolatedFraction);
        } else {
            i = ColorUtils.blendARGB(ScrimState.BOUNCER.getBehindTint(), scrimState.getBehindTint(), interpolatedFraction);
        }
        float f3 = this.mQsExpansion;
        if (f3 > 0.0f) {
            f = MathUtils.lerp(f, this.mDefaultScrimAlpha, f3);
            float f4 = this.mQsExpansion;
            if (this.mStatusBarKeyguardViewManager.isBouncerInTransit()) {
                f4 = BouncerPanelExpansionCalculator.showBouncerProgress(this.mPanelExpansionFraction);
            }
            if (this.mClipsQsScrim) {
                i2 = ScrimState.SHADE_LOCKED.getNotifTint();
            } else {
                i2 = ScrimState.SHADE_LOCKED.getBehindTint();
            }
            i = ColorUtils.blendARGB(i, i2, f4);
        }
        if (!this.mKeyguardStateController.isKeyguardGoingAway()) {
            f2 = f;
        }
        return new Pair<>(Integer.valueOf(i), Float.valueOf(f2));
    }

    public final void applyAndDispatchState() {
        applyState();
        if (!this.mUpdatePending) {
            setOrAdaptCurrentAnimation(this.mScrimBehind);
            setOrAdaptCurrentAnimation(this.mNotificationsScrim);
            setOrAdaptCurrentAnimation(this.mScrimInFront);
            dispatchBackScrimState(this.mScrimBehind.getViewAlpha());
            if (this.mWallpaperVisibilityTimedOut) {
                this.mWallpaperVisibilityTimedOut = false;
                DejankUtils.postAfterTraversal(new ScrimController$$ExternalSyntheticLambda5(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$applyAndDispatchState$2() {
        this.mTimeTicker.schedule(this.mDozeParameters.getWallpaperAodDuration(), 1);
    }

    public void setAodFrontScrimAlpha(float f) {
        if (this.mInFrontAlpha != f && shouldUpdateFrontScrimAlpha()) {
            this.mInFrontAlpha = f;
            updateScrims();
        }
        ScrimState.AOD.setAodFrontScrimAlpha(f);
        ScrimState.PULSING.setAodFrontScrimAlpha(f);
    }

    public final boolean shouldUpdateFrontScrimAlpha() {
        if ((this.mState != ScrimState.AOD || (!this.mDozeParameters.getAlwaysOn() && !this.mDockManager.isDocked())) && this.mState != ScrimState.PULSING) {
            return false;
        }
        return true;
    }

    public void setWakeLockScreenSensorActive(boolean z) {
        for (ScrimState wakeLockScreenSensorActive : ScrimState.values()) {
            wakeLockScreenSensorActive.setWakeLockScreenSensorActive(z);
        }
        ScrimState scrimState = this.mState;
        if (scrimState == ScrimState.PULSING) {
            float behindAlpha = scrimState.getBehindAlpha();
            if (this.mBehindAlpha != behindAlpha) {
                this.mBehindAlpha = behindAlpha;
                if (!Float.isNaN(behindAlpha)) {
                    updateScrims();
                    return;
                }
                throw new IllegalStateException("Scrim opacity is NaN for state: " + this.mState + ", back: " + this.mBehindAlpha);
            }
        }
    }

    public void scheduleUpdate() {
        ScrimView scrimView;
        if (!this.mUpdatePending && (scrimView = this.mScrimBehind) != null) {
            scrimView.invalidate();
            this.mScrimBehind.getViewTreeObserver().addOnPreDrawListener(this);
            this.mUpdatePending = true;
        }
    }

    public void updateScrims() {
        ScrimState scrimState;
        boolean z = true;
        if (this.mNeedsDrawableColorUpdate) {
            this.mNeedsDrawableColorUpdate = false;
            boolean z2 = this.mScrimInFront.getViewAlpha() != 0.0f && !this.mBlankScreen;
            boolean z3 = this.mScrimBehind.getViewAlpha() != 0.0f && !this.mBlankScreen;
            boolean z4 = this.mNotificationsScrim.getViewAlpha() != 0.0f && !this.mBlankScreen;
            this.mScrimInFront.setColors(this.mColors, z2);
            this.mScrimBehind.setColors(this.mColors, z3);
            this.mNotificationsScrim.setColors(this.mColors, z4);
            dispatchBackScrimState(this.mScrimBehind.getViewAlpha());
        }
        ScrimState scrimState2 = this.mState;
        ScrimState scrimState3 = ScrimState.AOD;
        boolean z5 = (scrimState2 == scrimState3 || scrimState2 == ScrimState.PULSING) && this.mWallpaperVisibilityTimedOut;
        if (!(scrimState2 == ScrimState.PULSING || scrimState2 == scrimState3) || !this.mKeyguardOccluded) {
            z = false;
        }
        if (z5 || z) {
            this.mBehindAlpha = 1.0f;
        }
        if (this.mKeyguardStateController.isKeyguardGoingAway()) {
            this.mNotificationsAlpha = 0.0f;
        }
        if (this.mKeyguardOccluded && ((scrimState = this.mState) == ScrimState.KEYGUARD || scrimState == ScrimState.SHADE_LOCKED)) {
            this.mBehindAlpha = 0.0f;
            this.mNotificationsAlpha = 0.0f;
        }
        setScrimAlpha(this.mScrimInFront, this.mInFrontAlpha);
        setScrimAlpha(this.mScrimBehind, this.mBehindAlpha);
        setScrimAlpha(this.mNotificationsScrim, this.mNotificationsAlpha);
        onFinished(this.mState);
        dispatchScrimsVisible();
    }

    public final void dispatchBackScrimState(float f) {
        if (this.mClipsQsScrim && this.mQsBottomVisible) {
            f = this.mNotificationsAlpha;
        }
        this.mScrimStateListener.accept(this.mState, Float.valueOf(f), this.mScrimInFront.getColors());
    }

    public final void dispatchScrimsVisible() {
        ScrimView scrimView = this.mClipsQsScrim ? this.mNotificationsScrim : this.mScrimBehind;
        int i = (this.mScrimInFront.getViewAlpha() == 1.0f || scrimView.getViewAlpha() == 1.0f) ? 2 : (this.mScrimInFront.getViewAlpha() == 0.0f && scrimView.getViewAlpha() == 0.0f) ? 0 : 1;
        if (this.mScrimsVisibility != i) {
            this.mScrimsVisibility = i;
            this.mScrimVisibleListener.accept(Integer.valueOf(i));
        }
    }

    public final float getInterpolatedFraction() {
        if (this.mStatusBarKeyguardViewManager.isBouncerInTransit()) {
            return BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(this.mPanelExpansionFraction);
        }
        return ShadeInterpolation.getNotificationScrimAlpha(this.mPanelExpansionFraction);
    }

    public final void setScrimAlpha(ScrimView scrimView, float f) {
        boolean z = false;
        if (f == 0.0f) {
            scrimView.setClickable(false);
        } else {
            if (this.mState != ScrimState.AOD) {
                z = true;
            }
            scrimView.setClickable(z);
        }
        updateScrim(scrimView, f);
    }

    public final String getScrimName(ScrimView scrimView) {
        if (scrimView == this.mScrimInFront) {
            return "front_scrim";
        }
        if (scrimView == this.mScrimBehind) {
            return "behind_scrim";
        }
        return scrimView == this.mNotificationsScrim ? "notifications_scrim" : "unknown_scrim";
    }

    public final void updateScrimColor(View view, float f, int i) {
        float max = Math.max(0.0f, Math.min(1.0f, f));
        if (view instanceof ScrimView) {
            ScrimView scrimView = (ScrimView) view;
            Trace.traceCounter(4096, getScrimName(scrimView) + "_alpha", (int) (255.0f * max));
            Trace.traceCounter(4096, getScrimName(scrimView) + "_tint", Color.alpha(i));
            scrimView.setTint(i);
            scrimView.setViewAlpha(max);
        } else {
            view.setAlpha(max);
        }
        dispatchScrimsVisible();
    }

    public final void startScrimAnimation(final View view, float f) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        Animator.AnimatorListener animatorListener = this.mAnimatorListener;
        if (animatorListener != null) {
            ofFloat.addListener(animatorListener);
        }
        ofFloat.addUpdateListener(new ScrimController$$ExternalSyntheticLambda6(this, view, view instanceof ScrimView ? ((ScrimView) view).getTint() : 0));
        ofFloat.setInterpolator(this.mInterpolator);
        ofFloat.setStartDelay(this.mAnimationDelay);
        ofFloat.setDuration(this.mAnimationDuration);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public final Callback mLastCallback;
            public final ScrimState mLastState;

            {
                this.mLastState = ScrimController.this.mState;
                this.mLastCallback = ScrimController.this.mCallback;
            }

            public void onAnimationEnd(Animator animator) {
                view.setTag(ScrimController.TAG_KEY_ANIM, (Object) null);
                ScrimController.this.onFinished(this.mLastCallback, this.mLastState);
                ScrimController.this.dispatchScrimsVisible();
            }
        });
        view.setTag(TAG_START_ALPHA, Float.valueOf(f));
        view.setTag(TAG_END_ALPHA, Float.valueOf(getCurrentScrimAlpha(view)));
        view.setTag(TAG_KEY_ANIM, ofFloat);
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startScrimAnimation$3(View view, int i, ValueAnimator valueAnimator) {
        float floatValue = ((Float) view.getTag(TAG_START_ALPHA)).floatValue();
        float floatValue2 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateScrimColor(view, MathUtils.constrain(MathUtils.lerp(floatValue, getCurrentScrimAlpha(view), floatValue2), 0.0f, 1.0f), ColorUtils.blendARGB(i, getCurrentScrimTint(view), floatValue2));
        dispatchScrimsVisible();
    }

    public final float getCurrentScrimAlpha(View view) {
        if (view == this.mScrimInFront) {
            return this.mInFrontAlpha;
        }
        if (view == this.mScrimBehind) {
            return this.mBehindAlpha;
        }
        if (view == this.mNotificationsScrim) {
            return this.mNotificationsAlpha;
        }
        throw new IllegalArgumentException("Unknown scrim view");
    }

    public final int getCurrentScrimTint(View view) {
        if (view == this.mScrimInFront) {
            return this.mInFrontTint;
        }
        if (view == this.mScrimBehind) {
            return this.mBehindTint;
        }
        if (view == this.mNotificationsScrim) {
            return this.mNotificationsTint;
        }
        throw new IllegalArgumentException("Unknown scrim view");
    }

    public boolean onPreDraw() {
        this.mScrimBehind.getViewTreeObserver().removeOnPreDrawListener(this);
        this.mUpdatePending = false;
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onStart();
        }
        updateScrims();
        return true;
    }

    public final void onFinished(ScrimState scrimState) {
        onFinished(this.mCallback, scrimState);
    }

    public final void onFinished(Callback callback, ScrimState scrimState) {
        if (this.mPendingFrameCallback == null) {
            if (!isAnimating(this.mScrimBehind) && !isAnimating(this.mNotificationsScrim) && !isAnimating(this.mScrimInFront)) {
                if (this.mWakeLockHeld) {
                    this.mWakeLock.release("ScrimController");
                    this.mWakeLockHeld = false;
                }
                if (callback != null) {
                    callback.onFinished();
                    if (callback == this.mCallback) {
                        this.mCallback = null;
                    }
                }
                if (scrimState == ScrimState.UNLOCKED) {
                    this.mInFrontTint = 0;
                    this.mBehindTint = this.mState.getBehindTint();
                    this.mNotificationsTint = this.mState.getNotifTint();
                    updateScrimColor(this.mScrimInFront, this.mInFrontAlpha, this.mInFrontTint);
                    updateScrimColor(this.mScrimBehind, this.mBehindAlpha, this.mBehindTint);
                    updateScrimColor(this.mNotificationsScrim, this.mNotificationsAlpha, this.mNotificationsTint);
                }
            } else if (callback != null && callback != this.mCallback) {
                callback.onFinished();
            }
        }
    }

    public final boolean isAnimating(View view) {
        return (view == null || view.getTag(TAG_KEY_ANIM) == null) ? false : true;
    }

    @VisibleForTesting
    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
    }

    public final void updateScrim(ScrimView scrimView, float f) {
        Callback callback;
        float viewAlpha = scrimView.getViewAlpha();
        ValueAnimator valueAnimator = (ValueAnimator) ViewState.getChildTag(scrimView, TAG_KEY_ANIM);
        if (valueAnimator != null) {
            cancelAnimator(valueAnimator);
        }
        if (this.mPendingFrameCallback == null) {
            if (this.mBlankScreen) {
                blankDisplay();
                return;
            }
            boolean z = true;
            if (!this.mScreenBlankingCallbackCalled && (callback = this.mCallback) != null) {
                callback.onDisplayBlanked();
                this.mScreenBlankingCallbackCalled = true;
            }
            if (scrimView == this.mScrimBehind) {
                dispatchBackScrimState(f);
            }
            boolean z2 = f != viewAlpha;
            if (scrimView.getTint() == getCurrentScrimTint(scrimView)) {
                z = false;
            }
            if (!z2 && !z) {
                return;
            }
            if (this.mAnimateChange) {
                startScrimAnimation(scrimView, viewAlpha);
            } else {
                updateScrimColor(scrimView, f, getCurrentScrimTint(scrimView));
            }
        }
    }

    public final void cancelAnimator(ValueAnimator valueAnimator) {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public final void blankDisplay() {
        updateScrimColor(this.mScrimInFront, 1.0f, -16777216);
        ScrimController$$ExternalSyntheticLambda7 scrimController$$ExternalSyntheticLambda7 = new ScrimController$$ExternalSyntheticLambda7(this);
        this.mPendingFrameCallback = scrimController$$ExternalSyntheticLambda7;
        doOnTheNextFrame(scrimController$$ExternalSyntheticLambda7);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$blankDisplay$5() {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onDisplayBlanked();
            this.mScreenBlankingCallbackCalled = true;
        }
        this.mBlankingTransitionRunnable = new ScrimController$$ExternalSyntheticLambda8(this);
        int i = this.mScreenOn ? 32 : 500;
        if (DEBUG) {
            Log.d("ScrimController", "Fading out scrims with delay: " + i);
        }
        this.mHandler.postDelayed(this.mBlankingTransitionRunnable, (long) i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$blankDisplay$4() {
        this.mBlankingTransitionRunnable = null;
        this.mPendingFrameCallback = null;
        this.mBlankScreen = false;
        updateScrims();
    }

    @VisibleForTesting
    public void doOnTheNextFrame(Runnable runnable) {
        this.mScrimBehind.postOnAnimationDelayed(runnable, 32);
    }

    public void setScrimBehindChangeRunnable(Runnable runnable) {
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView == null) {
            this.mScrimBehindChangeRunnable = runnable;
        } else {
            scrimView.setChangeRunnable(runnable, this.mMainExecutor);
        }
    }

    public final void updateThemeColors() {
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView != null) {
            int defaultColor = Utils.getColorAttr(scrimView.getContext(), 16844002).getDefaultColor();
            int defaultColor2 = Utils.getColorAccent(this.mScrimBehind.getContext()).getDefaultColor();
            this.mColors.setMainColor(defaultColor);
            this.mColors.setSecondaryColor(defaultColor2);
            ColorExtractor.GradientColors gradientColors = this.mColors;
            gradientColors.setSupportsDarkText(ColorUtils.calculateContrast(gradientColors.getMainColor(), -1) > 4.5d);
            this.mNeedsDrawableColorUpdate = true;
        }
    }

    public final void onThemeChanged() {
        updateThemeColors();
        scheduleUpdate();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(" ScrimController: ");
        printWriter.print("  state: ");
        printWriter.println(this.mState);
        printWriter.println("    mClipQsScrim = " + this.mState.mClipQsScrim);
        printWriter.print("  frontScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mScrimInFront.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mInFrontAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mScrimInFront.getTint()));
        printWriter.print("  behindScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mScrimBehind.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mBehindAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mScrimBehind.getTint()));
        printWriter.print("  notificationsScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mNotificationsScrim.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mNotificationsAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mNotificationsScrim.getTint()));
        printWriter.print("  mTracking=");
        printWriter.println(this.mTracking);
        printWriter.print("  mDefaultScrimAlpha=");
        printWriter.println(this.mDefaultScrimAlpha);
        printWriter.print("  mPanelExpansionFraction=");
        printWriter.println(this.mPanelExpansionFraction);
        printWriter.print("  mExpansionAffectsAlpha=");
        printWriter.println(this.mExpansionAffectsAlpha);
        printWriter.print("  mState.getMaxLightRevealScrimAlpha=");
        printWriter.println(this.mState.getMaxLightRevealScrimAlpha());
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mWallpaperSupportsAmbientMode = z;
        ScrimState[] values = ScrimState.values();
        for (ScrimState wallpaperSupportsAmbientMode : values) {
            wallpaperSupportsAmbientMode.setWallpaperSupportsAmbientMode(z);
        }
    }

    public void onScreenTurnedOn() {
        this.mScreenOn = true;
        if (this.mHandler.hasCallbacks(this.mBlankingTransitionRunnable)) {
            if (DEBUG) {
                Log.d("ScrimController", "Shorter blanking because screen turned on. All good.");
            }
            this.mHandler.removeCallbacks(this.mBlankingTransitionRunnable);
            this.mBlankingTransitionRunnable.run();
        }
    }

    public void onScreenTurnedOff() {
        this.mScreenOn = false;
    }

    public void setExpansionAffectsAlpha(boolean z) {
        this.mExpansionAffectsAlpha = z;
    }

    public void setKeyguardOccluded(boolean z) {
        this.mKeyguardOccluded = z;
        updateScrims();
    }

    public void setHasBackdrop(boolean z) {
        for (ScrimState hasBackdrop : ScrimState.values()) {
            hasBackdrop.setHasBackdrop(z);
        }
        ScrimState scrimState = this.mState;
        if (scrimState == ScrimState.AOD || scrimState == ScrimState.PULSING) {
            float behindAlpha = scrimState.getBehindAlpha();
            if (Float.isNaN(behindAlpha)) {
                throw new IllegalStateException("Scrim opacity is NaN for state: " + this.mState + ", back: " + this.mBehindAlpha);
            } else if (this.mBehindAlpha != behindAlpha) {
                this.mBehindAlpha = behindAlpha;
                updateScrims();
            }
        }
    }

    public final void setKeyguardFadingAway(boolean z, long j) {
        for (ScrimState keyguardFadingAway : ScrimState.values()) {
            keyguardFadingAway.setKeyguardFadingAway(z, j);
        }
    }

    public void setLaunchingAffordanceWithPreview(boolean z) {
        for (ScrimState launchingAffordanceWithPreview : ScrimState.values()) {
            launchingAffordanceWithPreview.setLaunchingAffordanceWithPreview(z);
        }
    }

    public class KeyguardVisibilityCallback extends KeyguardUpdateMonitorCallback {
        public KeyguardVisibilityCallback() {
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            ScrimController.this.mNeedsDrawableColorUpdate = true;
            ScrimController.this.scheduleUpdate();
        }
    }
}
