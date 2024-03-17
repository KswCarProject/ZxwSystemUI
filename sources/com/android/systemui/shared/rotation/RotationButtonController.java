package com.android.systemui.shared.rotation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.IRotationWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.view.RotationPolicy;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.shared.recents.utilities.ViewRippler;
import com.android.systemui.shared.rotation.RotationButton;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RotationButtonController {
    public static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    public final AccessibilityManager mAccessibilityManager;
    @SuppressLint({"InlinedApi"})
    public int mBehavior = 1;
    public final Runnable mCancelPendingRotationProposal = new RotationButtonController$$ExternalSyntheticLambda1(this);
    public final Context mContext;
    public final int mDarkIconColor;
    public boolean mHomeRotationEnabled;
    public boolean mHoveringRotationSuggestion;
    public final int mIconCcwStart0ResId;
    public final int mIconCcwStart90ResId;
    public final int mIconCwStart0ResId;
    public final int mIconCwStart90ResId;
    public int mIconResId;
    public boolean mIsNavigationBarShowing;
    public boolean mIsRecentsAnimationRunning;
    public int mLastRotationSuggestion;
    public final int mLightIconColor;
    public boolean mListenersRegistered = false;
    public final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    public boolean mPendingRotationSuggestion;
    public final Runnable mRemoveRotationProposal = new RotationButtonController$$ExternalSyntheticLambda0(this);
    public Consumer<Integer> mRotWatcherListener;
    public Animator mRotateHideAnimator;
    public RotationButton mRotationButton;
    public final IRotationWatcher.Stub mRotationWatcher = new IRotationWatcher.Stub() {
        public void onRotationChanged(int i) {
            RotationButtonController.this.mMainThreadHandler.postAtFrontOfQueue(new RotationButtonController$1$$ExternalSyntheticLambda0(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onRotationChanged$0(int i) {
            if (RotationButtonController.this.isRotationLocked()) {
                if (RotationButtonController.this.shouldOverrideUserLockPrefs(i)) {
                    RotationButtonController.this.setRotationLockedAtAngle(i);
                }
                RotationButtonController.this.setRotateSuggestionButtonState(false, true);
            }
            if (RotationButtonController.this.mRotWatcherListener != null) {
                RotationButtonController.this.mRotWatcherListener.accept(Integer.valueOf(i));
            }
        }
    };
    public boolean mSkipOverrideUserLockPrefsOnce;
    public final TaskStackListenerImpl mTaskStackListener;
    public final UiEventLogger mUiEventLogger = new UiEventLoggerImpl();
    public final ViewRippler mViewRippler = new ViewRippler();
    public final Supplier<Integer> mWindowRotationProvider;

    public static boolean hasDisable2RotateSuggestionFlag(int i) {
        return (i & 16) != 0;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        setRotateSuggestionButtonState(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mPendingRotationSuggestion = false;
    }

    public RotationButtonController(Context context, int i, int i2, int i3, int i4, int i5, int i6, Supplier<Integer> supplier) {
        this.mContext = context;
        this.mLightIconColor = i;
        this.mDarkIconColor = i2;
        this.mIconCcwStart0ResId = i3;
        this.mIconCcwStart90ResId = i4;
        this.mIconCwStart0ResId = i5;
        this.mIconCwStart90ResId = i6;
        this.mIconResId = i4;
        this.mAccessibilityManager = AccessibilityManager.getInstance(context);
        this.mTaskStackListener = new TaskStackListenerImpl();
        this.mWindowRotationProvider = supplier;
    }

    public void setRotationButton(RotationButton rotationButton, RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback) {
        this.mRotationButton = rotationButton;
        rotationButton.setRotationButtonController(this);
        this.mRotationButton.setOnClickListener(new RotationButtonController$$ExternalSyntheticLambda2(this));
        this.mRotationButton.setOnHoverListener(new RotationButtonController$$ExternalSyntheticLambda3(this));
        this.mRotationButton.setUpdatesCallback(rotationButtonUpdatesCallback);
    }

    public Context getContext() {
        return this.mContext;
    }

    public void registerListeners() {
        if (!this.mListenersRegistered && !getContext().getPackageManager().hasSystemFeature("android.hardware.type.pc")) {
            this.mListenersRegistered = true;
            try {
                WindowManagerGlobal.getWindowManagerService().watchRotation(this.mRotationWatcher, 0);
            } catch (IllegalArgumentException unused) {
                this.mListenersRegistered = false;
                Log.w("StatusBar/RotationButtonController", "RegisterListeners for the display failed");
            } catch (RemoteException e) {
                Log.e("StatusBar/RotationButtonController", "RegisterListeners caught a RemoteException", e);
                return;
            }
            TaskStackChangeListeners.getInstance().registerTaskStackListener(this.mTaskStackListener);
        }
    }

    public void unregisterListeners() {
        if (this.mListenersRegistered) {
            this.mListenersRegistered = false;
            try {
                WindowManagerGlobal.getWindowManagerService().removeRotationWatcher(this.mRotationWatcher);
                TaskStackChangeListeners.getInstance().unregisterTaskStackListener(this.mTaskStackListener);
            } catch (RemoteException e) {
                Log.e("StatusBar/RotationButtonController", "UnregisterListeners caught a RemoteException", e);
            }
        }
    }

    public void setRotationCallback(Consumer<Integer> consumer) {
        this.mRotWatcherListener = consumer;
    }

    public void setRotationLockedAtAngle(int i) {
        RotationPolicy.setRotationLockAtAngle(this.mContext, true, i);
    }

    public boolean isRotationLocked() {
        return RotationPolicy.isRotationLocked(this.mContext);
    }

    public void setRotateSuggestionButtonState(boolean z) {
        setRotateSuggestionButtonState(z, false);
    }

    public void setRotateSuggestionButtonState(boolean z, boolean z2) {
        View currentView;
        Drawable imageDrawable;
        if ((z || this.mRotationButton.isVisible()) && (currentView = this.mRotationButton.getCurrentView()) != null && (imageDrawable = this.mRotationButton.getImageDrawable()) != null) {
            this.mPendingRotationSuggestion = false;
            this.mMainThreadHandler.removeCallbacks(this.mCancelPendingRotationProposal);
            if (z) {
                Animator animator = this.mRotateHideAnimator;
                if (animator != null && animator.isRunning()) {
                    this.mRotateHideAnimator.cancel();
                }
                this.mRotateHideAnimator = null;
                currentView.setAlpha(1.0f);
                if (imageDrawable instanceof AnimatedVectorDrawable) {
                    AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageDrawable;
                    animatedVectorDrawable.reset();
                    animatedVectorDrawable.start();
                }
                if (!isRotateSuggestionIntroduced()) {
                    this.mViewRippler.start(currentView);
                }
                this.mRotationButton.show();
                return;
            }
            this.mViewRippler.stop();
            if (z2) {
                Animator animator2 = this.mRotateHideAnimator;
                if (animator2 != null && animator2.isRunning()) {
                    this.mRotateHideAnimator.pause();
                }
                this.mRotationButton.hide();
                return;
            }
            Animator animator3 = this.mRotateHideAnimator;
            if (animator3 == null || !animator3.isRunning()) {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(currentView, "alpha", new float[]{0.0f});
                ofFloat.setDuration(100);
                ofFloat.setInterpolator(LINEAR_INTERPOLATOR);
                ofFloat.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        RotationButtonController.this.mRotationButton.hide();
                    }
                });
                this.mRotateHideAnimator = ofFloat;
                ofFloat.start();
            }
        }
    }

    public void setDarkIntensity(float f) {
        this.mRotationButton.setDarkIntensity(f);
    }

    public void setRecentsAnimationRunning(boolean z) {
        this.mIsRecentsAnimationRunning = z;
        updateRotationButtonStateInOverview();
    }

    public void setHomeRotationEnabled(boolean z) {
        this.mHomeRotationEnabled = z;
        updateRotationButtonStateInOverview();
    }

    public final void updateRotationButtonStateInOverview() {
        if (this.mIsRecentsAnimationRunning && !this.mHomeRotationEnabled) {
            setRotateSuggestionButtonState(false, true);
        }
    }

    public void onRotationProposal(int i, boolean z) {
        int intValue = this.mWindowRotationProvider.get().intValue();
        if (this.mRotationButton.acceptRotationProposal()) {
            if (!this.mHomeRotationEnabled && this.mIsRecentsAnimationRunning) {
                return;
            }
            if (!z) {
                setRotateSuggestionButtonState(false);
            } else if (i == intValue) {
                this.mMainThreadHandler.removeCallbacks(this.mRemoveRotationProposal);
                setRotateSuggestionButtonState(false);
            } else {
                this.mLastRotationSuggestion = i;
                boolean isRotationAnimationCCW = Utilities.isRotationAnimationCCW(intValue, i);
                if (intValue == 0 || intValue == 2) {
                    this.mIconResId = isRotationAnimationCCW ? this.mIconCcwStart0ResId : this.mIconCwStart0ResId;
                } else {
                    this.mIconResId = isRotationAnimationCCW ? this.mIconCcwStart90ResId : this.mIconCwStart90ResId;
                }
                this.mRotationButton.updateIcon(this.mLightIconColor, this.mDarkIconColor);
                if (canShowRotationButton()) {
                    showAndLogRotationSuggestion();
                    return;
                }
                this.mPendingRotationSuggestion = true;
                this.mMainThreadHandler.removeCallbacks(this.mCancelPendingRotationProposal);
                this.mMainThreadHandler.postDelayed(this.mCancelPendingRotationProposal, 20000);
            }
        }
    }

    public void onDisable2FlagChanged(int i) {
        if (hasDisable2RotateSuggestionFlag(i)) {
            onRotationSuggestionsDisabled();
        }
    }

    public void onBehaviorChanged(int i, int i2) {
        if (i == 0 && this.mBehavior != i2) {
            this.mBehavior = i2;
            showPendingRotationButtonIfNeeded();
        }
    }

    public void onNavigationBarWindowVisibilityChange(boolean z) {
        if (this.mIsNavigationBarShowing != z) {
            this.mIsNavigationBarShowing = z;
            showPendingRotationButtonIfNeeded();
        }
    }

    public final void showPendingRotationButtonIfNeeded() {
        if (canShowRotationButton() && this.mPendingRotationSuggestion) {
            showAndLogRotationSuggestion();
        }
    }

    @SuppressLint({"InlinedApi"})
    public final boolean canShowRotationButton() {
        return this.mIsNavigationBarShowing || this.mBehavior == 1;
    }

    public int getIconResId() {
        return this.mIconResId;
    }

    public int getLightIconColor() {
        return this.mLightIconColor;
    }

    public int getDarkIconColor() {
        return this.mDarkIconColor;
    }

    public RotationButton getRotationButton() {
        return this.mRotationButton;
    }

    public final void onRotateSuggestionClick(View view) {
        this.mUiEventLogger.log(RotationButtonEvent.ROTATION_SUGGESTION_ACCEPTED);
        incrementNumAcceptedRotationSuggestionsIfNeeded();
        setRotationLockedAtAngle(this.mLastRotationSuggestion);
        view.performHapticFeedback(1);
    }

    public final boolean onRotateSuggestionHover(View view, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        this.mHoveringRotationSuggestion = actionMasked == 9 || actionMasked == 7;
        rescheduleRotationTimeout(true);
        return false;
    }

    public final void onRotationSuggestionsDisabled() {
        setRotateSuggestionButtonState(false, true);
        this.mMainThreadHandler.removeCallbacks(this.mRemoveRotationProposal);
    }

    public final void showAndLogRotationSuggestion() {
        setRotateSuggestionButtonState(true);
        rescheduleRotationTimeout(false);
        this.mUiEventLogger.log(RotationButtonEvent.ROTATION_SUGGESTION_SHOWN);
    }

    public void setSkipOverrideUserLockPrefsOnce() {
        this.mSkipOverrideUserLockPrefsOnce = !this.mIsRecentsAnimationRunning;
    }

    public final boolean shouldOverrideUserLockPrefs(int i) {
        if (this.mSkipOverrideUserLockPrefsOnce) {
            this.mSkipOverrideUserLockPrefsOnce = false;
            return false;
        } else if (i == 0) {
            return true;
        } else {
            return false;
        }
    }

    public final void rescheduleRotationTimeout(boolean z) {
        Animator animator;
        if (!z || (((animator = this.mRotateHideAnimator) == null || !animator.isRunning()) && this.mRotationButton.isVisible())) {
            this.mMainThreadHandler.removeCallbacks(this.mRemoveRotationProposal);
            this.mMainThreadHandler.postDelayed(this.mRemoveRotationProposal, (long) computeRotationProposalTimeout());
        }
    }

    public final int computeRotationProposalTimeout() {
        return this.mAccessibilityManager.getRecommendedTimeoutMillis(this.mHoveringRotationSuggestion ? 16000 : 5000, 4);
    }

    public final boolean isRotateSuggestionIntroduced() {
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "num_rotation_suggestions_accepted", 0) >= 3) {
            return true;
        }
        return false;
    }

    public final void incrementNumAcceptedRotationSuggestionsIfNeeded() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int i = Settings.Secure.getInt(contentResolver, "num_rotation_suggestions_accepted", 0);
        if (i < 3) {
            Settings.Secure.putInt(contentResolver, "num_rotation_suggestions_accepted", i + 1);
        }
    }

    public class TaskStackListenerImpl implements TaskStackChangeListener {
        public TaskStackListenerImpl() {
        }

        public void onTaskStackChanged() {
            RotationButtonController.this.setRotateSuggestionButtonState(false);
        }

        public void onTaskRemoved(int i) {
            RotationButtonController.this.setRotateSuggestionButtonState(false);
        }

        public void onTaskMovedToFront(int i) {
            RotationButtonController.this.setRotateSuggestionButtonState(false);
        }

        public void onActivityRequestedOrientationChanged(int i, int i2) {
            Optional.ofNullable(ActivityManagerWrapper.getInstance()).map(new RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda0()).ifPresent(new RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda1(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onActivityRequestedOrientationChanged$0(int i, ActivityManager.RunningTaskInfo runningTaskInfo) {
            if (runningTaskInfo.id == i) {
                RotationButtonController.this.setRotateSuggestionButtonState(false);
            }
        }
    }

    public enum RotationButtonEvent implements UiEventLogger.UiEventEnum {
        ROTATION_SUGGESTION_SHOWN(206),
        ROTATION_SUGGESTION_ACCEPTED(207);
        
        private final int mId;

        /* access modifiers changed from: public */
        RotationButtonEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
