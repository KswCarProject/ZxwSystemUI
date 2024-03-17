package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.BoostFramework;
import android.util.Log;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.PanelView;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.time.SystemClock;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.io.PrintWriter;

public abstract class PanelViewController {
    public static final String TAG = PanelView.class.getSimpleName();
    public final AmbientState mAmbientState;
    public boolean mAnimateAfterExpanding;
    public boolean mAnimatingOnDown;
    public Interpolator mBounceInterpolator;
    public CentralSurfaces mCentralSurfaces;
    public boolean mClosing;
    public boolean mCollapsedAndHeadsUpOnDown;
    public long mDownTime;
    public final DozeLog mDozeLog;
    public boolean mExpandLatencyTracking;
    public float mExpandedFraction = 0.0f;
    public float mExpandedHeight = 0.0f;
    public boolean mExpanding;
    public float mExpansionDragDownAmountPx = 0.0f;
    public final FalsingManager mFalsingManager;
    public int mFixedDuration = -1;
    public FlingAnimationUtils mFlingAnimationUtils;
    public FlingAnimationUtils mFlingAnimationUtilsClosing;
    public FlingAnimationUtils mFlingAnimationUtilsDismissing;
    public final Runnable mFlingCollapseRunnable = new Runnable() {
        public void run() {
            PanelViewController panelViewController = PanelViewController.this;
            panelViewController.fling(0.0f, false, panelViewController.mNextCollapseSpeedUpFactor, false);
        }
    };
    public boolean mGestureWaitForTouchSlop;
    public boolean mHandlingPointerUp;
    public boolean mHasLayoutedSinceDown;
    public HeadsUpManagerPhone mHeadsUpManager;
    public ValueAnimator mHeightAnimator;
    public boolean mHintAnimationRunning;
    public float mHintDistance;
    public boolean mIgnoreXTouchSlop;
    public boolean mInSplitShade;
    public float mInitialOffsetOnTouch;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public boolean mInstantExpanding;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public boolean mIsFlinging;
    public boolean mIsLaunchAnimationRunning;
    public boolean mIsSpringBackAnimation;
    public KeyguardBottomAreaView mKeyguardBottomArea;
    public final KeyguardStateController mKeyguardStateController;
    public KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    public float mLastGesturedOverExpansion = -1.0f;
    public final LatencyTracker mLatencyTracker;
    public final LockscreenGestureLogger mLockscreenGestureLogger;
    public float mMinExpandHeight;
    public boolean mMotionAborted;
    public float mNextCollapseSpeedUpFactor = 1.0f;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public boolean mNotificationsDragEnabled;
    public float mOverExpansion;
    public boolean mPanelClosedOnDown;
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    public float mPanelFlingOvershootAmount;
    public boolean mPanelUpdateWhenAnimatorEnds;
    public BoostFramework mPerf = null;
    public final Resources mResources;
    public float mSlopMultiplier;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final StatusBarTouchableRegionManager mStatusBarTouchableRegionManager;
    public final SystemClock mSystemClock;
    public boolean mTouchAboveFalsingThreshold;
    public boolean mTouchDisabled;
    public final TouchHandler mTouchHandler;
    public int mTouchSlop;
    public boolean mTouchSlopExceeded;
    public boolean mTouchSlopExceededBeforeDown;
    public boolean mTouchStartedInEmptyArea;
    public boolean mTracking;
    public int mTrackingPointer;
    public int mUnlockFalsingThreshold;
    public boolean mUpdateFlingOnLayout;
    public float mUpdateFlingVelocity;
    public boolean mUpwardsWhenThresholdReached;
    public final VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    public boolean mVibrateOnOpening;
    public final VibratorHelper mVibratorHelper;
    public final PanelView mView;
    public String mViewName;

    public abstract boolean canCollapsePanelOnTouch();

    public abstract OnLayoutChangeListener createLayoutChangeListener();

    public abstract OnConfigurationChangedListener createOnConfigurationChangedListener();

    public abstract TouchHandler createTouchHandler();

    public abstract int getMaxPanelHeight();

    public abstract boolean isInContentBounds(float f, float f2);

    public abstract boolean isPanelVisibleBecauseOfHeadsUp();

    public abstract boolean isTrackingBlocked();

    public abstract void onClosingFinished();

    public abstract void onExpandingFinished();

    public void onExpandingStarted() {
    }

    public abstract void onHeightUpdated(float f);

    public abstract boolean onMiddleClicked();

    public abstract boolean shouldGestureIgnoreXTouchSlop(float f, float f2);

    public abstract boolean shouldGestureWaitForTouchSlop();

    public abstract boolean shouldPanelBeVisible();

    public abstract boolean shouldUseDismissingAnimation();

    public void notifyExpandingStarted() {
        if (!this.mExpanding) {
            this.mExpanding = true;
            onExpandingStarted();
        }
    }

    public final void notifyExpandingFinished() {
        endClosing();
        if (this.mExpanding) {
            this.mExpanding = false;
            onExpandingFinished();
        }
    }

    public PanelViewController(PanelView panelView, FalsingManager falsingManager, DozeLog dozeLog, KeyguardStateController keyguardStateController, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationShadeWindowController notificationShadeWindowController, VibratorHelper vibratorHelper, StatusBarKeyguardViewManager statusBarKeyguardViewManager, LatencyTracker latencyTracker, FlingAnimationUtils.Builder builder, StatusBarTouchableRegionManager statusBarTouchableRegionManager, LockscreenGestureLogger lockscreenGestureLogger, PanelExpansionStateManager panelExpansionStateManager, AmbientState ambientState, InteractionJankMonitor interactionJankMonitor, KeyguardUnlockAnimationController keyguardUnlockAnimationController, SystemClock systemClock) {
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        keyguardStateController.addCallback(new KeyguardStateController.Callback() {
            public void onKeyguardFadingAwayChanged() {
                PanelViewController.this.requestPanelHeightUpdate();
            }
        });
        this.mAmbientState = ambientState;
        this.mView = panelView;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mLockscreenGestureLogger = lockscreenGestureLogger;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        TouchHandler createTouchHandler = createTouchHandler();
        this.mTouchHandler = createTouchHandler;
        panelView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewDetachedFromWindow(View view) {
            }

            public void onViewAttachedToWindow(View view) {
                PanelViewController panelViewController = PanelViewController.this;
                panelViewController.mViewName = panelViewController.mResources.getResourceName(panelViewController.mView.getId());
            }
        });
        panelView.addOnLayoutChangeListener(createLayoutChangeListener());
        panelView.setOnTouchListener(createTouchHandler);
        panelView.setOnConfigurationChangedListener(createOnConfigurationChangedListener());
        Resources resources = panelView.getResources();
        this.mResources = resources;
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mFlingAnimationUtils = builder.reset().setMaxLengthSeconds(0.6f).setSpeedUpFactor(0.6f).build();
        this.mFlingAnimationUtilsClosing = builder.reset().setMaxLengthSeconds(0.6f).setSpeedUpFactor(0.6f).build();
        this.mFlingAnimationUtilsDismissing = builder.reset().setMaxLengthSeconds(0.5f).setSpeedUpFactor(0.6f).setX2(0.6f).setY2(0.84f).build();
        this.mLatencyTracker = latencyTracker;
        this.mBounceInterpolator = new BounceInterpolator();
        this.mFalsingManager = falsingManager;
        this.mDozeLog = dozeLog;
        this.mNotificationsDragEnabled = resources.getBoolean(R$bool.config_enableNotificationShadeDrag);
        this.mVibratorHelper = vibratorHelper;
        this.mVibrateOnOpening = resources.getBoolean(R$bool.config_vibrateOnIconAnimation);
        this.mStatusBarTouchableRegionManager = statusBarTouchableRegionManager;
        this.mInteractionJankMonitor = interactionJankMonitor;
        this.mSystemClock = systemClock;
        this.mPerf = new BoostFramework();
    }

    public void loadDimens() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.mView.getContext());
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mHintDistance = this.mResources.getDimension(R$dimen.hint_move_distance);
        this.mPanelFlingOvershootAmount = this.mResources.getDimension(R$dimen.panel_overshoot_amount);
        this.mUnlockFalsingThreshold = this.mResources.getDimensionPixelSize(R$dimen.unlock_falsing_threshold);
        this.mInSplitShade = this.mResources.getBoolean(R$bool.config_use_split_notification_shade);
    }

    public float getTouchSlop(MotionEvent motionEvent) {
        if (motionEvent.getClassification() == 1) {
            return ((float) this.mTouchSlop) * this.mSlopMultiplier;
        }
        return (float) this.mTouchSlop;
    }

    public final void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.mVelocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }

    public void setTouchAndAnimationDisabled(boolean z) {
        this.mTouchDisabled = z;
        if (z) {
            cancelHeightAnimator();
            if (this.mTracking) {
                onTrackingStopped(true);
            }
            notifyExpandingFinished();
        }
    }

    public void startExpandLatencyTracking() {
        if (this.mLatencyTracker.isEnabled()) {
            this.mLatencyTracker.onActionStart(0);
            this.mExpandLatencyTracking = true;
        }
    }

    public final void startOpening(MotionEvent motionEvent) {
        updatePanelExpansionAndVisibility();
        maybeVibrateOnOpening();
        float displayWidth = this.mCentralSurfaces.getDisplayWidth();
        float displayHeight = this.mCentralSurfaces.getDisplayHeight();
        this.mLockscreenGestureLogger.writeAtFractionalPosition(1328, (int) ((motionEvent.getX() / displayWidth) * 100.0f), (int) ((motionEvent.getY() / displayHeight) * 100.0f), this.mCentralSurfaces.getRotation());
        this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_UNLOCKED_NOTIFICATION_PANEL_EXPAND);
    }

    public void maybeVibrateOnOpening() {
        if (this.mVibrateOnOpening) {
            this.mVibratorHelper.vibrate(2);
        }
    }

    public final boolean isDirectionUpwards(float f, float f2) {
        float f3 = f - this.mInitialTouchX;
        float f4 = f2 - this.mInitialTouchY;
        if (f4 < 0.0f && Math.abs(f4) >= Math.abs(f3)) {
            return true;
        }
        return false;
    }

    public void startExpandMotion(float f, float f2, boolean z, float f3) {
        if (!this.mHandlingPointerUp && !this.mStatusBarStateController.isDozing()) {
            beginJankMonitoring(0);
        }
        this.mInitialOffsetOnTouch = f3;
        this.mInitialTouchY = f2;
        this.mInitialTouchX = f;
        if (z) {
            this.mTouchSlopExceeded = true;
            setExpandedHeight(f3);
            onTrackingStarted();
        }
    }

    public final void endMotionEvent(MotionEvent motionEvent, float f, float f2, boolean z) {
        boolean z2;
        int i;
        this.mTrackingPointer = -1;
        boolean z3 = false;
        this.mAmbientState.setSwipingUp(false);
        if ((this.mTracking && this.mTouchSlopExceeded) || Math.abs(f - this.mInitialTouchX) > ((float) this.mTouchSlop) || Math.abs(f2 - this.mInitialTouchY) > ((float) this.mTouchSlop) || motionEvent.getActionMasked() == 3 || z) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float yVelocity = this.mVelocityTracker.getYVelocity();
            float hypot = (float) Math.hypot((double) this.mVelocityTracker.getXVelocity(), (double) this.mVelocityTracker.getYVelocity());
            boolean z4 = this.mStatusBarStateController.getState() == 1;
            if (motionEvent.getActionMasked() == 3 || z) {
                if (!this.mKeyguardStateController.isKeyguardFadingAway()) {
                    if (z4) {
                        z2 = true;
                    } else if (!this.mKeyguardStateController.isKeyguardFadingAway()) {
                        z2 = !this.mPanelClosedOnDown;
                    }
                }
                z2 = false;
            } else {
                z2 = flingExpands(yVelocity, hypot, f, f2);
            }
            this.mDozeLog.traceFling(z2, this.mTouchAboveFalsingThreshold, this.mCentralSurfaces.isFalsingThresholdNeeded(), this.mCentralSurfaces.isWakeUpComingFromTouch());
            if (!z2 && z4) {
                float displayDensity = this.mCentralSurfaces.getDisplayDensity();
                this.mLockscreenGestureLogger.write(186, (int) Math.abs((f2 - this.mInitialTouchY) / displayDensity), (int) Math.abs(yVelocity / displayDensity));
                this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_UNLOCK);
            }
            if (yVelocity == 0.0f) {
                i = 7;
            } else if (f2 - this.mInitialTouchY > 0.0f) {
                i = 0;
            } else {
                i = this.mKeyguardStateController.canDismissLockScreen() ? 4 : 8;
            }
            fling(yVelocity, z2, isFalseTouch(f, f2, i));
            onTrackingStopped(z2);
            if (z2 && this.mPanelClosedOnDown && !this.mHasLayoutedSinceDown) {
                z3 = true;
            }
            this.mUpdateFlingOnLayout = z3;
            if (z3) {
                this.mUpdateFlingVelocity = yVelocity;
            }
        } else if (!this.mCentralSurfaces.isBouncerShowing() && !this.mStatusBarKeyguardViewManager.isShowingAlternateAuthOrAnimating() && !this.mKeyguardStateController.isKeyguardGoingAway()) {
            onTrackingStopped(onEmptySpaceClick(this.mInitialTouchX));
        }
        this.mVelocityTracker.clear();
    }

    public float getCurrentExpandVelocity() {
        this.mVelocityTracker.computeCurrentVelocity(1000);
        return this.mVelocityTracker.getYVelocity();
    }

    public final int getFalsingThreshold() {
        return (int) (((float) this.mUnlockFalsingThreshold) * (this.mCentralSurfaces.isWakeUpComingFromTouch() ? 1.5f : 1.0f));
    }

    public void onTrackingStopped(boolean z) {
        this.mTracking = false;
        this.mCentralSurfaces.onTrackingStopped(z);
        updatePanelExpansionAndVisibility();
    }

    public void onTrackingStarted() {
        endClosing();
        this.mTracking = true;
        this.mCentralSurfaces.onTrackingStarted();
        notifyExpandingStarted();
        updatePanelExpansionAndVisibility();
    }

    public void cancelHeightAnimator() {
        ValueAnimator valueAnimator = this.mHeightAnimator;
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                this.mPanelUpdateWhenAnimatorEnds = false;
            }
            this.mHeightAnimator.cancel();
        }
        endClosing();
    }

    public final void endClosing() {
        if (this.mClosing) {
            setIsClosing(false);
            onClosingFinished();
        }
    }

    public boolean flingExpands(float f, float f2, float f3, float f4) {
        int i;
        if (this.mFalsingManager.isUnlockingDisabled()) {
            return true;
        }
        if (f4 - this.mInitialTouchY > 0.0f) {
            i = 0;
        } else {
            i = this.mKeyguardStateController.canDismissLockScreen() ? 4 : 8;
        }
        if (isFalseTouch(f3, f4, i)) {
            return true;
        }
        if (Math.abs(f2) < this.mFlingAnimationUtils.getMinVelocityPxPerSecond()) {
            return shouldExpandWhenNotFlinging();
        }
        if (f > 0.0f) {
            return true;
        }
        return false;
    }

    public boolean shouldExpandWhenNotFlinging() {
        return getExpandedFraction() > 0.5f;
    }

    public final boolean isFalseTouch(float f, float f2, int i) {
        if (!this.mCentralSurfaces.isFalsingThresholdNeeded()) {
            return false;
        }
        if (this.mFalsingManager.isClassifierEnabled()) {
            return this.mFalsingManager.isFalseTouch(i);
        }
        if (!this.mTouchAboveFalsingThreshold) {
            return true;
        }
        if (this.mUpwardsWhenThresholdReached) {
            return false;
        }
        return !isDirectionUpwards(f, f2);
    }

    public void fling(float f, boolean z) {
        fling(f, z, 1.0f, false);
    }

    public void fling(float f, boolean z, boolean z2) {
        fling(f, z, 1.0f, z2);
    }

    public void fling(float f, boolean z, float f2, boolean z2) {
        float maxPanelHeight = z ? (float) getMaxPanelHeight() : 0.0f;
        if (!z) {
            setIsClosing(true);
        }
        flingToHeight(f, z, maxPanelHeight, f2, z2);
    }

    public void flingToHeight(float f, boolean z, float f2, float f3, boolean z2) {
        int i;
        float f4 = f2;
        if (f4 == this.mExpandedHeight && this.mOverExpansion == 0.0f) {
            endJankMonitoring(0);
            this.mKeyguardStateController.notifyPanelFlingEnd();
            notifyExpandingFinished();
            return;
        }
        this.mIsFlinging = true;
        boolean z3 = z && !this.mInSplitShade && this.mStatusBarStateController.getState() != 1 && this.mOverExpansion == 0.0f && f >= 0.0f;
        final boolean z4 = z3 || (this.mOverExpansion != 0.0f && z);
        float lerp = z3 ? MathUtils.lerp(0.2f, 1.0f, MathUtils.saturate(f / (this.mFlingAnimationUtils.getHighVelocityPxPerSecond() * 0.5f))) + (this.mOverExpansion / this.mPanelFlingOvershootAmount) : 0.0f;
        ValueAnimator createHeightAnimator = createHeightAnimator(f4, lerp);
        if (z) {
            float f5 = (!z2 || f >= 0.0f) ? f : 0.0f;
            this.mFlingAnimationUtils.apply(createHeightAnimator, this.mExpandedHeight, f4 + (lerp * this.mPanelFlingOvershootAmount), f5, (float) this.mView.getHeight());
            if (f5 == 0.0f) {
                createHeightAnimator.setDuration(350);
            }
            i = -1;
        } else {
            if (!shouldUseDismissingAnimation()) {
                i = -1;
                this.mFlingAnimationUtilsClosing.apply(createHeightAnimator, this.mExpandedHeight, f2, f, (float) this.mView.getHeight());
            } else if (f == 0.0f) {
                createHeightAnimator.setInterpolator(Interpolators.PANEL_CLOSE_ACCELERATED);
                createHeightAnimator.setDuration((long) (((this.mExpandedHeight / ((float) this.mView.getHeight())) * 100.0f) + 200.0f));
                i = -1;
            } else {
                i = -1;
                this.mFlingAnimationUtilsDismissing.apply(createHeightAnimator, this.mExpandedHeight, f2, f, (float) this.mView.getHeight());
            }
            if (f == 0.0f) {
                createHeightAnimator.setDuration((long) (((float) createHeightAnimator.getDuration()) / f3));
            }
            int i2 = this.mFixedDuration;
            if (i2 != i) {
                createHeightAnimator.setDuration((long) i2);
            }
        }
        if (this.mPerf != null) {
            this.mPerf.perfHint(4224, this.mView.getContext().getPackageName(), i, 3);
        }
        createHeightAnimator.addListener(new AnimatorListenerAdapter() {
            public boolean mCancelled;

            public void onAnimationStart(Animator animator) {
                if (!PanelViewController.this.mStatusBarStateController.isDozing()) {
                    PanelViewController.this.beginJankMonitoring(0);
                }
            }

            public void onAnimationCancel(Animator animator) {
                if (PanelViewController.this.mPerf != null) {
                    PanelViewController.this.mPerf.perfLockRelease();
                }
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (PanelViewController.this.mPerf != null) {
                    PanelViewController.this.mPerf.perfLockRelease();
                }
                if (!z4 || this.mCancelled) {
                    PanelViewController.this.onFlingEnd(this.mCancelled);
                } else {
                    PanelViewController.this.springBack();
                }
            }
        });
        setAnimator(createHeightAnimator);
        createHeightAnimator.start();
    }

    public final void springBack() {
        float f = this.mOverExpansion;
        if (f == 0.0f) {
            onFlingEnd(false);
            return;
        }
        this.mIsSpringBackAnimation = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f, 0.0f});
        ofFloat.addUpdateListener(new PanelViewController$$ExternalSyntheticLambda4(this));
        ofFloat.setDuration(400);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public boolean mCancelled;

            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                PanelViewController.this.mIsSpringBackAnimation = false;
                PanelViewController.this.onFlingEnd(this.mCancelled);
            }
        });
        setAnimator(ofFloat);
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$springBack$0(ValueAnimator valueAnimator) {
        setOverExpansionInternal(((Float) valueAnimator.getAnimatedValue()).floatValue(), false);
    }

    public void onFlingEnd(boolean z) {
        this.mIsFlinging = false;
        setOverExpansionInternal(0.0f, false);
        setAnimator((ValueAnimator) null);
        this.mKeyguardStateController.notifyPanelFlingEnd();
        if (!z) {
            endJankMonitoring(0);
            notifyExpandingFinished();
        } else {
            cancelJankMonitoring(0);
        }
        updatePanelExpansionAndVisibility();
    }

    public void setExpandedHeight(float f) {
        setExpandedHeightInternal(f);
    }

    public void requestPanelHeightUpdate() {
        float maxPanelHeight = (float) getMaxPanelHeight();
        if (isFullyCollapsed() || maxPanelHeight == this.mExpandedHeight) {
            return;
        }
        if (this.mTracking && !isTrackingBlocked()) {
            return;
        }
        if (this.mHeightAnimator == null || this.mIsSpringBackAnimation) {
            setExpandedHeight(maxPanelHeight);
        } else {
            this.mPanelUpdateWhenAnimatorEnds = true;
        }
    }

    public void setExpandedHeightInternal(float f) {
        if (Float.isNaN(f)) {
            Log.wtf(TAG, "ExpandedHeight set to NaN");
        }
        this.mNotificationShadeWindowController.batchApplyWindowLayoutParams(new PanelViewController$$ExternalSyntheticLambda0(this, f));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setExpandedHeightInternal$2(float f) {
        float f2 = 0.0f;
        if (this.mExpandLatencyTracking && f != 0.0f) {
            DejankUtils.postAfterTraversal(new PanelViewController$$ExternalSyntheticLambda1(this));
            this.mExpandLatencyTracking = false;
        }
        float maxPanelHeight = (float) getMaxPanelHeight();
        if (this.mHeightAnimator == null) {
            if (this.mTracking && !this.mInSplitShade) {
                setOverExpansionInternal(Math.max(0.0f, f - maxPanelHeight), true);
            }
            this.mExpandedHeight = Math.min(f, maxPanelHeight);
        } else {
            this.mExpandedHeight = f;
        }
        float f3 = this.mExpandedHeight;
        if (f3 < 1.0f && f3 != 0.0f && this.mClosing) {
            this.mExpandedHeight = 0.0f;
            ValueAnimator valueAnimator = this.mHeightAnimator;
            if (valueAnimator != null) {
                valueAnimator.end();
            }
        }
        this.mExpansionDragDownAmountPx = f;
        if (maxPanelHeight != 0.0f) {
            f2 = this.mExpandedHeight / maxPanelHeight;
        }
        this.mExpandedFraction = Math.min(1.0f, f2);
        onHeightUpdated(this.mExpandedHeight);
        updatePanelExpansionAndVisibility();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setExpandedHeightInternal$1() {
        this.mLatencyTracker.onActionEnd(0);
    }

    public void setOverExpansion(float f) {
        this.mOverExpansion = f;
    }

    public final void setOverExpansionInternal(float f, boolean z) {
        if (!z) {
            this.mLastGesturedOverExpansion = -1.0f;
            setOverExpansion(f);
        } else if (this.mLastGesturedOverExpansion != f) {
            this.mLastGesturedOverExpansion = f;
            setOverExpansion(Interpolators.getOvershootInterpolation(MathUtils.saturate(f / (((float) this.mView.getHeight()) / 3.0f))) * this.mPanelFlingOvershootAmount * 2.0f);
        }
    }

    public void setExpandedFraction(float f) {
        setExpandedHeight(((float) getMaxPanelHeight()) * f);
    }

    public float getExpandedHeight() {
        return this.mExpandedHeight;
    }

    public float getExpandedFraction() {
        return this.mExpandedFraction;
    }

    public boolean isFullyExpanded() {
        return this.mExpandedHeight >= ((float) getMaxPanelHeight());
    }

    public boolean isFullyCollapsed() {
        return this.mExpandedFraction <= 0.0f;
    }

    public boolean isCollapsing() {
        return this.mClosing || this.mIsLaunchAnimationRunning;
    }

    public boolean isFlinging() {
        return this.mIsFlinging;
    }

    public boolean isTracking() {
        return this.mTracking;
    }

    public void collapse(boolean z, float f) {
        if (canPanelBeCollapsed()) {
            cancelHeightAnimator();
            notifyExpandingStarted();
            setIsClosing(true);
            if (z) {
                this.mNextCollapseSpeedUpFactor = f;
                this.mView.postDelayed(this.mFlingCollapseRunnable, 120);
                return;
            }
            fling(0.0f, false, f, false);
        }
    }

    public boolean canPanelBeCollapsed() {
        return !isFullyCollapsed() && !this.mTracking && !this.mClosing;
    }

    public void expand(boolean z) {
        if (isFullyCollapsed() || isCollapsing()) {
            this.mInstantExpanding = true;
            this.mAnimateAfterExpanding = z;
            this.mUpdateFlingOnLayout = false;
            abortAnimations();
            if (this.mTracking) {
                onTrackingStopped(true);
            }
            if (this.mExpanding) {
                notifyExpandingFinished();
            }
            updatePanelExpansionAndVisibility();
            this.mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (!PanelViewController.this.mInstantExpanding) {
                        PanelViewController.this.mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else if (PanelViewController.this.mCentralSurfaces.getNotificationShadeWindowView().isVisibleToUser()) {
                        PanelViewController.this.mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        if (PanelViewController.this.mAnimateAfterExpanding) {
                            PanelViewController.this.notifyExpandingStarted();
                            PanelViewController.this.beginJankMonitoring(0);
                            PanelViewController.this.fling(0.0f, true);
                        } else {
                            PanelViewController.this.setExpandedFraction(1.0f);
                        }
                        PanelViewController.this.mInstantExpanding = false;
                    }
                }
            });
            this.mView.requestLayout();
        }
    }

    public void instantCollapse() {
        abortAnimations();
        setExpandedFraction(0.0f);
        if (this.mExpanding) {
            notifyExpandingFinished();
        }
        if (this.mInstantExpanding) {
            this.mInstantExpanding = false;
            updatePanelExpansionAndVisibility();
        }
    }

    public final void abortAnimations() {
        cancelHeightAnimator();
        this.mView.removeCallbacks(this.mFlingCollapseRunnable);
    }

    public void startUnlockHintAnimation() {
        if (this.mHeightAnimator == null && !this.mTracking) {
            notifyExpandingStarted();
            startUnlockHintAnimationPhase1(new PanelViewController$$ExternalSyntheticLambda3(this));
            onUnlockHintStarted();
            this.mHintAnimationRunning = true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startUnlockHintAnimation$3() {
        notifyExpandingFinished();
        onUnlockHintFinished();
        this.mHintAnimationRunning = false;
    }

    public void onUnlockHintFinished() {
        this.mCentralSurfaces.onHintFinished();
    }

    public void onUnlockHintStarted() {
        this.mCentralSurfaces.onUnlockHintStarted();
    }

    public boolean isUnlockHintRunning() {
        return this.mHintAnimationRunning;
    }

    public final void startUnlockHintAnimationPhase1(final Runnable runnable) {
        ValueAnimator createHeightAnimator = createHeightAnimator(Math.max(0.0f, ((float) getMaxPanelHeight()) - this.mHintDistance));
        createHeightAnimator.setDuration(250);
        createHeightAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        createHeightAnimator.addListener(new AnimatorListenerAdapter() {
            public boolean mCancelled;

            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (this.mCancelled) {
                    PanelViewController.this.setAnimator((ValueAnimator) null);
                    runnable.run();
                    return;
                }
                PanelViewController.this.startUnlockHintAnimationPhase2(runnable);
            }
        });
        createHeightAnimator.start();
        setAnimator(createHeightAnimator);
        View[] viewArr = {this.mKeyguardBottomArea.getIndicationArea(), this.mCentralSurfaces.getAmbientIndicationContainer()};
        for (int i = 0; i < 2; i++) {
            View view = viewArr[i];
            if (view != null) {
                view.animate().translationY(-this.mHintDistance).setDuration(250).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).withEndAction(new PanelViewController$$ExternalSyntheticLambda5(this, view)).start();
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startUnlockHintAnimationPhase1$4(View view) {
        view.animate().translationY(0.0f).setDuration(450).setInterpolator(this.mBounceInterpolator).start();
    }

    public final void setAnimator(ValueAnimator valueAnimator) {
        this.mHeightAnimator = valueAnimator;
        if (valueAnimator == null && this.mPanelUpdateWhenAnimatorEnds) {
            this.mPanelUpdateWhenAnimatorEnds = false;
            requestPanelHeightUpdate();
        }
    }

    public final void startUnlockHintAnimationPhase2(final Runnable runnable) {
        ValueAnimator createHeightAnimator = createHeightAnimator((float) getMaxPanelHeight());
        createHeightAnimator.setDuration(450);
        createHeightAnimator.setInterpolator(this.mBounceInterpolator);
        createHeightAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                PanelViewController.this.setAnimator((ValueAnimator) null);
                runnable.run();
                PanelViewController.this.updatePanelExpansionAndVisibility();
            }
        });
        createHeightAnimator.start();
        setAnimator(createHeightAnimator);
    }

    public final ValueAnimator createHeightAnimator(float f) {
        return createHeightAnimator(f, 0.0f);
    }

    public final ValueAnimator createHeightAnimator(float f, float f2) {
        float f3 = this.mOverExpansion;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mExpandedHeight, f});
        ofFloat.addUpdateListener(new PanelViewController$$ExternalSyntheticLambda2(this, f2, f, f3, ofFloat));
        return ofFloat;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createHeightAnimator$5(float f, float f2, float f3, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        if (f > 0.0f || (f2 == 0.0f && f3 != 0.0f)) {
            setOverExpansionInternal(MathUtils.lerp(f3, this.mPanelFlingOvershootAmount * f, Interpolators.FAST_OUT_SLOW_IN.getInterpolation(valueAnimator.getAnimatedFraction())), false);
        }
        setExpandedHeightInternal(((Float) valueAnimator2.getAnimatedValue()).floatValue());
    }

    public void updateVisibility() {
        this.mView.setVisibility(shouldPanelBeVisible() ? 0 : 4);
    }

    public void updatePanelExpansionAndVisibility() {
        this.mPanelExpansionStateManager.onPanelExpansionChanged(this.mExpandedFraction, isExpanded(), this.mTracking, this.mExpansionDragDownAmountPx);
        updateVisibility();
    }

    public boolean isExpanded() {
        return this.mExpandedFraction > 0.0f || this.mInstantExpanding || isPanelVisibleBecauseOfHeadsUp() || this.mTracking || (this.mHeightAnimator != null && !this.mIsSpringBackAnimation);
    }

    public boolean onEmptySpaceClick(float f) {
        if (this.mHintAnimationRunning) {
            return true;
        }
        return onMiddleClicked();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Object[] objArr = new Object[8];
        objArr[0] = getClass().getSimpleName();
        objArr[1] = Float.valueOf(getExpandedHeight());
        objArr[2] = Integer.valueOf(getMaxPanelHeight());
        String str = "T";
        objArr[3] = this.mClosing ? str : "f";
        objArr[4] = this.mTracking ? str : "f";
        ValueAnimator valueAnimator = this.mHeightAnimator;
        objArr[5] = valueAnimator;
        objArr[6] = (valueAnimator == null || !valueAnimator.isStarted()) ? "" : " (started)";
        if (!this.mTouchDisabled) {
            str = "f";
        }
        objArr[7] = str;
        printWriter.println(String.format("[PanelView(%s): expandedHeight=%f maxPanelHeight=%d closing=%s tracking=%s timeAnim=%s%s touchDisabled=%s]", objArr));
    }

    public void setHeadsUpManager(HeadsUpManagerPhone headsUpManagerPhone) {
        this.mHeadsUpManager = headsUpManagerPhone;
    }

    public void setIsLaunchAnimationRunning(boolean z) {
        this.mIsLaunchAnimationRunning = z;
    }

    public void setIsClosing(boolean z) {
        this.mClosing = z;
    }

    public boolean isClosing() {
        return this.mClosing;
    }

    public void collapseWithDuration(int i) {
        this.mFixedDuration = i;
        collapse(false, 1.0f);
        this.mFixedDuration = -1;
    }

    public ViewGroup getView() {
        return this.mView;
    }

    public class TouchHandler implements View.OnTouchListener {
        public TouchHandler() {
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            int pointerId;
            if (!PanelViewController.this.mInstantExpanding && PanelViewController.this.mNotificationsDragEnabled && !PanelViewController.this.mTouchDisabled && (!PanelViewController.this.mMotionAborted || motionEvent.getActionMasked() == 0)) {
                int findPointerIndex = motionEvent.findPointerIndex(PanelViewController.this.mTrackingPointer);
                if (findPointerIndex < 0) {
                    PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(0);
                    findPointerIndex = 0;
                }
                float x = motionEvent.getX(findPointerIndex);
                float y = motionEvent.getY(findPointerIndex);
                boolean canCollapsePanelOnTouch = PanelViewController.this.canCollapsePanelOnTouch();
                int actionMasked = motionEvent.getActionMasked();
                int i = 1;
                if (actionMasked != 0) {
                    if (actionMasked != 1) {
                        if (actionMasked == 2) {
                            float r4 = y - PanelViewController.this.mInitialTouchY;
                            PanelViewController.this.addMovement(motionEvent);
                            boolean z = PanelViewController.this.mPanelClosedOnDown && !PanelViewController.this.mCollapsedAndHeadsUpOnDown;
                            if (canCollapsePanelOnTouch || PanelViewController.this.mTouchStartedInEmptyArea || PanelViewController.this.mAnimatingOnDown || z) {
                                float abs = Math.abs(r4);
                                float touchSlop = PanelViewController.this.getTouchSlop(motionEvent);
                                if ((r4 < (-touchSlop) || ((z || PanelViewController.this.mAnimatingOnDown) && abs > touchSlop)) && abs > Math.abs(x - PanelViewController.this.mInitialTouchX)) {
                                    PanelViewController.this.cancelHeightAnimator();
                                    PanelViewController panelViewController = PanelViewController.this;
                                    panelViewController.startExpandMotion(x, y, true, panelViewController.mExpandedHeight);
                                    return true;
                                }
                            }
                        } else if (actionMasked != 3) {
                            if (actionMasked != 5) {
                                if (actionMasked == 6 && PanelViewController.this.mTrackingPointer == (pointerId = motionEvent.getPointerId(motionEvent.getActionIndex()))) {
                                    if (motionEvent.getPointerId(0) != pointerId) {
                                        i = 0;
                                    }
                                    PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(i);
                                    PanelViewController.this.mInitialTouchX = motionEvent.getX(i);
                                    PanelViewController.this.mInitialTouchY = motionEvent.getY(i);
                                }
                            } else if (PanelViewController.this.mStatusBarStateController.getState() == 1) {
                                PanelViewController.this.mMotionAborted = true;
                                PanelViewController.this.mVelocityTracker.clear();
                            }
                        }
                    }
                    PanelViewController.this.mVelocityTracker.clear();
                } else {
                    PanelViewController.this.mCentralSurfaces.userActivity();
                    PanelViewController panelViewController2 = PanelViewController.this;
                    panelViewController2.mAnimatingOnDown = panelViewController2.mHeightAnimator != null && !PanelViewController.this.mIsSpringBackAnimation;
                    PanelViewController.this.mMinExpandHeight = 0.0f;
                    PanelViewController panelViewController3 = PanelViewController.this;
                    panelViewController3.mDownTime = panelViewController3.mSystemClock.uptimeMillis();
                    if (PanelViewController.this.mAnimatingOnDown && PanelViewController.this.mClosing) {
                        PanelViewController panelViewController4 = PanelViewController.this;
                        if (!panelViewController4.mHintAnimationRunning) {
                            panelViewController4.cancelHeightAnimator();
                            PanelViewController.this.mTouchSlopExceeded = true;
                            return true;
                        }
                    }
                    PanelViewController.this.mInitialTouchY = y;
                    PanelViewController.this.mInitialTouchX = x;
                    PanelViewController panelViewController5 = PanelViewController.this;
                    panelViewController5.mTouchStartedInEmptyArea = !panelViewController5.isInContentBounds(x, y);
                    PanelViewController panelViewController6 = PanelViewController.this;
                    panelViewController6.mTouchSlopExceeded = panelViewController6.mTouchSlopExceededBeforeDown;
                    PanelViewController.this.mMotionAborted = false;
                    PanelViewController panelViewController7 = PanelViewController.this;
                    panelViewController7.mPanelClosedOnDown = panelViewController7.isFullyCollapsed();
                    PanelViewController.this.mCollapsedAndHeadsUpOnDown = false;
                    PanelViewController.this.mHasLayoutedSinceDown = false;
                    PanelViewController.this.mUpdateFlingOnLayout = false;
                    PanelViewController.this.mTouchAboveFalsingThreshold = false;
                    PanelViewController.this.addMovement(motionEvent);
                }
            }
            return false;
        }

        /* JADX WARNING: Removed duplicated region for block: B:114:0x0269  */
        /* JADX WARNING: Removed duplicated region for block: B:115:0x026b  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTouch(android.view.View r8, android.view.MotionEvent r9) {
            /*
                r7 = this;
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.mInstantExpanding
                r0 = 0
                if (r8 != 0) goto L_0x02a9
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.mTouchDisabled
                r1 = 3
                if (r8 == 0) goto L_0x0018
                int r8 = r9.getActionMasked()
                if (r8 != r1) goto L_0x02a9
            L_0x0018:
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.mMotionAborted
                if (r8 == 0) goto L_0x0028
                int r8 = r9.getActionMasked()
                if (r8 == 0) goto L_0x0028
                goto L_0x02a9
            L_0x0028:
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.mNotificationsDragEnabled
                r2 = 1
                if (r8 != 0) goto L_0x003b
                com.android.systemui.statusbar.phone.PanelViewController r7 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r7.mTracking
                if (r8 == 0) goto L_0x003a
                r7.onTrackingStopped(r2)
            L_0x003a:
                return r0
            L_0x003b:
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.isFullyCollapsed()
                if (r8 == 0) goto L_0x0057
                r8 = 8194(0x2002, float:1.1482E-41)
                boolean r8 = r9.isFromSource(r8)
                if (r8 == 0) goto L_0x0057
                int r8 = r9.getAction()
                if (r8 != r2) goto L_0x0056
                com.android.systemui.statusbar.phone.PanelViewController r7 = com.android.systemui.statusbar.phone.PanelViewController.this
                r7.expand(r2)
            L_0x0056:
                return r2
            L_0x0057:
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                int r8 = r8.mTrackingPointer
                int r8 = r9.findPointerIndex(r8)
                if (r8 >= 0) goto L_0x006d
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                int r3 = r9.getPointerId(r0)
                r8.mTrackingPointer = r3
                r8 = r0
            L_0x006d:
                float r3 = r9.getX(r8)
                float r8 = r9.getY(r8)
                int r4 = r9.getActionMasked()
                if (r4 != 0) goto L_0x009b
                com.android.systemui.statusbar.phone.PanelViewController r4 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r5 = r4.shouldGestureWaitForTouchSlop()
                r4.mGestureWaitForTouchSlop = r5
                com.android.systemui.statusbar.phone.PanelViewController r4 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r5 = r4.isFullyCollapsed()
                if (r5 != 0) goto L_0x0097
                com.android.systemui.statusbar.phone.PanelViewController r5 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r5 = r5.shouldGestureIgnoreXTouchSlop(r3, r8)
                if (r5 == 0) goto L_0x0095
                goto L_0x0097
            L_0x0095:
                r5 = r0
                goto L_0x0098
            L_0x0097:
                r5 = r2
            L_0x0098:
                r4.mIgnoreXTouchSlop = r5
            L_0x009b:
                int r4 = r9.getActionMasked()
                r5 = 0
                if (r4 == 0) goto L_0x01ef
                if (r4 == r2) goto L_0x01c9
                r6 = 2
                if (r4 == r6) goto L_0x0103
                if (r4 == r1) goto L_0x01c9
                r1 = 5
                if (r4 == r1) goto L_0x00ee
                r8 = 6
                if (r4 == r8) goto L_0x00b1
                goto L_0x029a
            L_0x00b1:
                int r8 = r9.getActionIndex()
                int r8 = r9.getPointerId(r8)
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                int r1 = r1.mTrackingPointer
                if (r1 != r8) goto L_0x029a
                int r1 = r9.getPointerId(r0)
                if (r1 == r8) goto L_0x00c9
                r8 = r0
                goto L_0x00ca
            L_0x00c9:
                r8 = r2
            L_0x00ca:
                float r1 = r9.getY(r8)
                float r3 = r9.getX(r8)
                com.android.systemui.statusbar.phone.PanelViewController r4 = com.android.systemui.statusbar.phone.PanelViewController.this
                int r8 = r9.getPointerId(r8)
                r4.mTrackingPointer = r8
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.mHandlingPointerUp = r2
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                float r9 = r8.mExpandedHeight
                r8.startExpandMotion(r3, r1, r2, r9)
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.mHandlingPointerUp = r0
                goto L_0x029a
            L_0x00ee:
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                com.android.systemui.statusbar.SysuiStatusBarStateController r1 = r1.mStatusBarStateController
                int r1 = r1.getState()
                if (r1 != r2) goto L_0x029a
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                r1.mMotionAborted = r2
                com.android.systemui.statusbar.phone.PanelViewController r7 = com.android.systemui.statusbar.phone.PanelViewController.this
                r7.endMotionEvent(r9, r3, r8, r2)
                return r0
            L_0x0103:
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                r1.addMovement(r9)
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                float r1 = r1.mInitialTouchY
                float r1 = r8 - r1
                float r4 = java.lang.Math.abs(r1)
                com.android.systemui.statusbar.phone.PanelViewController r6 = com.android.systemui.statusbar.phone.PanelViewController.this
                float r9 = r6.getTouchSlop(r9)
                int r9 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1))
                if (r9 <= 0) goto L_0x016f
                float r9 = java.lang.Math.abs(r1)
                com.android.systemui.statusbar.phone.PanelViewController r4 = com.android.systemui.statusbar.phone.PanelViewController.this
                float r4 = r4.mInitialTouchX
                float r4 = r3 - r4
                float r4 = java.lang.Math.abs(r4)
                int r9 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
                if (r9 > 0) goto L_0x013a
                com.android.systemui.statusbar.phone.PanelViewController r9 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r9 = r9.mIgnoreXTouchSlop
                if (r9 == 0) goto L_0x016f
            L_0x013a:
                com.android.systemui.statusbar.phone.PanelViewController r9 = com.android.systemui.statusbar.phone.PanelViewController.this
                r9.mTouchSlopExceeded = r2
                com.android.systemui.statusbar.phone.PanelViewController r9 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r9 = r9.mGestureWaitForTouchSlop
                if (r9 == 0) goto L_0x016f
                com.android.systemui.statusbar.phone.PanelViewController r9 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r4 = r9.mTracking
                if (r4 != 0) goto L_0x016f
                boolean r9 = r9.mCollapsedAndHeadsUpOnDown
                if (r9 != 0) goto L_0x016f
                com.android.systemui.statusbar.phone.PanelViewController r9 = com.android.systemui.statusbar.phone.PanelViewController.this
                float r9 = r9.mInitialOffsetOnTouch
                int r9 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
                if (r9 == 0) goto L_0x0165
                com.android.systemui.statusbar.phone.PanelViewController r9 = com.android.systemui.statusbar.phone.PanelViewController.this
                float r1 = r9.mExpandedHeight
                r9.startExpandMotion(r3, r8, r0, r1)
                r1 = r5
            L_0x0165:
                com.android.systemui.statusbar.phone.PanelViewController r9 = com.android.systemui.statusbar.phone.PanelViewController.this
                r9.cancelHeightAnimator()
                com.android.systemui.statusbar.phone.PanelViewController r9 = com.android.systemui.statusbar.phone.PanelViewController.this
                r9.onTrackingStarted()
            L_0x016f:
                com.android.systemui.statusbar.phone.PanelViewController r9 = com.android.systemui.statusbar.phone.PanelViewController.this
                float r9 = r9.mInitialOffsetOnTouch
                float r9 = r9 + r1
                float r9 = java.lang.Math.max(r5, r9)
                com.android.systemui.statusbar.phone.PanelViewController r4 = com.android.systemui.statusbar.phone.PanelViewController.this
                float r4 = r4.mMinExpandHeight
                float r9 = java.lang.Math.max(r9, r4)
                float r4 = -r1
                com.android.systemui.statusbar.phone.PanelViewController r6 = com.android.systemui.statusbar.phone.PanelViewController.this
                int r6 = r6.getFalsingThreshold()
                float r6 = (float) r6
                int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r4 < 0) goto L_0x019e
                com.android.systemui.statusbar.phone.PanelViewController r4 = com.android.systemui.statusbar.phone.PanelViewController.this
                r4.mTouchAboveFalsingThreshold = r2
                com.android.systemui.statusbar.phone.PanelViewController r4 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r4.isDirectionUpwards(r3, r8)
                r4.mUpwardsWhenThresholdReached = r8
            L_0x019e:
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.mGestureWaitForTouchSlop
                if (r8 == 0) goto L_0x01ac
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.mTracking
                if (r8 == 0) goto L_0x029a
            L_0x01ac:
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.isTrackingBlocked()
                if (r8 != 0) goto L_0x029a
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                com.android.systemui.statusbar.notification.stack.AmbientState r8 = r8.mAmbientState
                int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
                if (r1 > 0) goto L_0x01be
                r1 = r2
                goto L_0x01bf
            L_0x01be:
                r1 = r0
            L_0x01bf:
                r8.setSwipingUp(r1)
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.setExpandedHeightInternal(r9)
                goto L_0x029a
            L_0x01c9:
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                r1.addMovement(r9)
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                r1.endMotionEvent(r9, r3, r8, r0)
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                android.animation.ValueAnimator r8 = r8.mHeightAnimator
                if (r8 != 0) goto L_0x029a
                int r8 = r9.getActionMasked()
                if (r8 != r2) goto L_0x01e8
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.endJankMonitoring(r0)
                goto L_0x029a
            L_0x01e8:
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.cancelJankMonitoring(r0)
                goto L_0x029a
            L_0x01ef:
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                float r4 = r1.mExpandedHeight
                r1.startExpandMotion(r3, r8, r0, r4)
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.mMinExpandHeight = r5
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r1 = r8.isFullyCollapsed()
                r8.mPanelClosedOnDown = r1
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.mHasLayoutedSinceDown = r0
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.mUpdateFlingOnLayout = r0
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.mMotionAborted = r0
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                com.android.systemui.util.time.SystemClock r1 = r8.mSystemClock
                long r3 = r1.uptimeMillis()
                r8.mDownTime = r3
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.mTouchAboveFalsingThreshold = r0
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r1 = r8.isFullyCollapsed()
                if (r1 == 0) goto L_0x0236
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                com.android.systemui.statusbar.phone.HeadsUpManagerPhone r1 = r1.mHeadsUpManager
                boolean r1 = r1.hasPinnedHeadsUp()
                if (r1 == 0) goto L_0x0236
                r1 = r2
                goto L_0x0237
            L_0x0236:
                r1 = r0
            L_0x0237:
                r8.mCollapsedAndHeadsUpOnDown = r1
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.addMovement(r9)
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                android.animation.ValueAnimator r8 = r8.mHeightAnimator
                if (r8 == 0) goto L_0x0255
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r1 = r8.mHintAnimationRunning
                if (r1 != 0) goto L_0x0255
                boolean r8 = r8.mIsSpringBackAnimation
                if (r8 != 0) goto L_0x0255
                r8 = r2
                goto L_0x0256
            L_0x0255:
                r8 = r0
            L_0x0256:
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r1 = r1.mGestureWaitForTouchSlop
                if (r1 == 0) goto L_0x0260
                if (r8 == 0) goto L_0x0279
            L_0x0260:
                com.android.systemui.statusbar.phone.PanelViewController r1 = com.android.systemui.statusbar.phone.PanelViewController.this
                if (r8 != 0) goto L_0x026b
                boolean r8 = r1.mTouchSlopExceededBeforeDown
                if (r8 == 0) goto L_0x0269
                goto L_0x026b
            L_0x0269:
                r8 = r0
                goto L_0x026c
            L_0x026b:
                r8 = r2
            L_0x026c:
                r1.mTouchSlopExceeded = r8
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.cancelHeightAnimator()
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.onTrackingStarted()
            L_0x0279:
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.isFullyCollapsed()
                if (r8 == 0) goto L_0x029a
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                com.android.systemui.statusbar.phone.HeadsUpManagerPhone r8 = r8.mHeadsUpManager
                boolean r8 = r8.hasPinnedHeadsUp()
                if (r8 != 0) goto L_0x029a
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                com.android.systemui.statusbar.phone.CentralSurfaces r8 = r8.mCentralSurfaces
                boolean r8 = r8.isBouncerShowing()
                if (r8 != 0) goto L_0x029a
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                r8.startOpening(r9)
            L_0x029a:
                com.android.systemui.statusbar.phone.PanelViewController r8 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r8 = r8.mGestureWaitForTouchSlop
                if (r8 == 0) goto L_0x02a8
                com.android.systemui.statusbar.phone.PanelViewController r7 = com.android.systemui.statusbar.phone.PanelViewController.this
                boolean r7 = r7.mTracking
                if (r7 == 0) goto L_0x02a9
            L_0x02a8:
                r0 = r2
            L_0x02a9:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PanelViewController.TouchHandler.onTouch(android.view.View, android.view.MotionEvent):boolean");
        }
    }

    public class OnLayoutChangeListener implements View.OnLayoutChangeListener {
        public OnLayoutChangeListener() {
        }

        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            PanelViewController.this.requestPanelHeightUpdate();
            PanelViewController.this.mHasLayoutedSinceDown = true;
            if (PanelViewController.this.mUpdateFlingOnLayout) {
                PanelViewController.this.abortAnimations();
                PanelViewController panelViewController = PanelViewController.this;
                panelViewController.fling(panelViewController.mUpdateFlingVelocity, true);
                PanelViewController.this.mUpdateFlingOnLayout = false;
            }
        }
    }

    public class OnConfigurationChangedListener implements PanelView.OnConfigurationChangedListener {
        public OnConfigurationChangedListener() {
        }

        public void onConfigurationChanged(Configuration configuration) {
            PanelViewController.this.loadDimens();
        }
    }

    public final void beginJankMonitoring(int i) {
        if (this.mInteractionJankMonitor != null) {
            this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(i, this.mView).setTag(isFullyCollapsed() ? "Expand" : "Collapse"));
        }
    }

    public final void endJankMonitoring(int i) {
        if (this.mInteractionJankMonitor != null) {
            InteractionJankMonitor.getInstance().end(i);
        }
    }

    public final void cancelJankMonitoring(int i) {
        if (this.mInteractionJankMonitor != null) {
            InteractionJankMonitor.getInstance().cancel(i);
        }
    }

    public float getExpansionFraction() {
        return this.mExpandedFraction;
    }

    public PanelExpansionStateManager getPanelExpansionStateManager() {
        return this.mPanelExpansionStateManager;
    }
}
