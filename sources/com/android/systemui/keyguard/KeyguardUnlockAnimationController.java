package com.android.systemui.keyguard;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import androidx.core.math.MathUtils;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController;
import com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController;
import com.android.systemui.shared.system.smartspace.SmartspaceState;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import java.util.ArrayList;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: KeyguardUnlockAnimationController.kt */
public final class KeyguardUnlockAnimationController extends ISysuiUnlockAnimationController.Stub implements KeyguardStateController.Callback {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final Lazy<BiometricUnlockController> biometricUnlockControllerLazy;
    @NotNull
    public final Context context;
    @NotNull
    public final FeatureFlags featureFlags;
    @NotNull
    public final Handler handler;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final KeyguardViewController keyguardViewController;
    @NotNull
    public final Lazy<KeyguardViewMediator> keyguardViewMediator;
    @Nullable
    public SmartspaceState launcherSmartspaceState;
    @Nullable
    public ILauncherUnlockAnimationController launcherUnlockController;
    @NotNull
    public final ArrayList<KeyguardUnlockAnimationListener> listeners = new ArrayList<>();
    @Nullable
    public View lockscreenSmartspace;
    @NotNull
    public final NotificationShadeWindowController notificationShadeWindowController;
    public boolean playingCannedUnlockAnimation;
    public float roundedCornerRadius;
    @NotNull
    public final SysuiStatusBarStateController statusBarStateController;
    public float surfaceBehindAlpha = 1.0f;
    public ValueAnimator surfaceBehindAlphaAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
    public final ValueAnimator surfaceBehindEntryAnimator;
    @NotNull
    public final Matrix surfaceBehindMatrix = new Matrix();
    @Nullable
    public SyncRtSurfaceTransactionApplier.SurfaceParams surfaceBehindParams;
    public long surfaceBehindRemoteAnimationStartTime;
    @Nullable
    public RemoteAnimationTarget surfaceBehindRemoteAnimationTarget;
    @Nullable
    public SyncRtSurfaceTransactionApplier surfaceTransactionApplier;
    public boolean willUnlockWithInWindowLauncherAnimations;
    public boolean willUnlockWithSmartspaceTransition;

    /* compiled from: KeyguardUnlockAnimationController.kt */
    public interface KeyguardUnlockAnimationListener {
        void onUnlockAnimationFinished() {
        }

        void onUnlockAnimationStarted(boolean z, boolean z2, long j, long j2) {
        }
    }

    public static /* synthetic */ void getSurfaceBehindAlphaAnimator$annotations() {
    }

    public static /* synthetic */ void getSurfaceBehindEntryAnimator$annotations() {
    }

    public static /* synthetic */ void getSurfaceTransactionApplier$annotations() {
    }

    public KeyguardUnlockAnimationController(@NotNull Context context2, @NotNull KeyguardStateController keyguardStateController2, @NotNull Lazy<KeyguardViewMediator> lazy, @NotNull KeyguardViewController keyguardViewController2, @NotNull FeatureFlags featureFlags2, @NotNull Lazy<BiometricUnlockController> lazy2, @NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull NotificationShadeWindowController notificationShadeWindowController2) {
        this.context = context2;
        this.keyguardStateController = keyguardStateController2;
        this.keyguardViewMediator = lazy;
        this.keyguardViewController = keyguardViewController2;
        this.featureFlags = featureFlags2;
        this.biometricUnlockControllerLazy = lazy2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.notificationShadeWindowController = notificationShadeWindowController2;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.surfaceBehindEntryAnimator = ofFloat;
        this.handler = new Handler();
        ValueAnimator valueAnimator = this.surfaceBehindAlphaAnimator;
        valueAnimator.setDuration(175);
        valueAnimator.setInterpolator(Interpolators.LINEAR);
        valueAnimator.addUpdateListener(new KeyguardUnlockAnimationController$1$1(this));
        valueAnimator.addListener(new KeyguardUnlockAnimationController$1$2(this));
        ofFloat.setDuration(200);
        ofFloat.setStartDelay(75);
        ofFloat.setInterpolator(Interpolators.TOUCH_RESPONSE);
        ofFloat.addUpdateListener(new KeyguardUnlockAnimationController$2$1(this));
        ofFloat.addListener(new KeyguardUnlockAnimationController$2$2(this));
        keyguardStateController2.addCallback(this);
        this.roundedCornerRadius = (float) context2.getResources().getDimensionPixelSize(17105512);
    }

    @Nullable
    public final View getLockscreenSmartspace() {
        return this.lockscreenSmartspace;
    }

    public final void setLockscreenSmartspace(@Nullable View view) {
        this.lockscreenSmartspace = view;
    }

    public final boolean getPlayingCannedUnlockAnimation() {
        return this.playingCannedUnlockAnimation;
    }

    public final void setPlayingCannedUnlockAnimation(boolean z) {
        this.playingCannedUnlockAnimation = z;
    }

    public void setLauncherUnlockController(@Nullable ILauncherUnlockAnimationController iLauncherUnlockAnimationController) {
        this.launcherUnlockController = iLauncherUnlockAnimationController;
    }

    public void onLauncherSmartspaceStateUpdated(@Nullable SmartspaceState smartspaceState) {
        this.launcherSmartspaceState = smartspaceState;
    }

    public final void addKeyguardUnlockAnimationListener(@NotNull KeyguardUnlockAnimationListener keyguardUnlockAnimationListener) {
        this.listeners.add(keyguardUnlockAnimationListener);
    }

    public final void removeKeyguardUnlockAnimationListener(@NotNull KeyguardUnlockAnimationListener keyguardUnlockAnimationListener) {
        this.listeners.remove(keyguardUnlockAnimationListener);
    }

    public final boolean canPerformInWindowLauncherAnimations() {
        Companion companion = Companion;
        return companion.isNexusLauncherUnderneath() && !this.notificationShadeWindowController.isLaunchingActivity() && this.launcherUnlockController != null && !this.keyguardStateController.isDismissingFromSwipe() && !companion.isFoldable(this.context);
    }

    public void onKeyguardGoingAwayChanged() {
        if (this.keyguardStateController.isKeyguardGoingAway() && !this.statusBarStateController.leaveOpenOnKeyguardHide()) {
            prepareForInWindowLauncherAnimations();
        }
    }

    public final void prepareForInWindowLauncherAnimations() {
        int i;
        boolean canPerformInWindowLauncherAnimations = canPerformInWindowLauncherAnimations();
        this.willUnlockWithInWindowLauncherAnimations = canPerformInWindowLauncherAnimations;
        if (canPerformInWindowLauncherAnimations) {
            this.willUnlockWithSmartspaceTransition = shouldPerformSmartspaceTransition();
            Rect rect = new Rect();
            if (this.willUnlockWithSmartspaceTransition) {
                rect = new Rect();
                View lockscreenSmartspace2 = getLockscreenSmartspace();
                Intrinsics.checkNotNull(lockscreenSmartspace2);
                lockscreenSmartspace2.getBoundsOnScreen(rect);
                View lockscreenSmartspace3 = getLockscreenSmartspace();
                Intrinsics.checkNotNull(lockscreenSmartspace3);
                int paddingLeft = lockscreenSmartspace3.getPaddingLeft();
                View lockscreenSmartspace4 = getLockscreenSmartspace();
                Intrinsics.checkNotNull(lockscreenSmartspace4);
                rect.offset(paddingLeft, lockscreenSmartspace4.getPaddingTop());
                View lockscreenSmartspace5 = getLockscreenSmartspace();
                BcSmartspaceDataPlugin.SmartspaceView smartspaceView = lockscreenSmartspace5 instanceof BcSmartspaceDataPlugin.SmartspaceView ? (BcSmartspaceDataPlugin.SmartspaceView) lockscreenSmartspace5 : null;
                if (smartspaceView == null) {
                    i = 0;
                } else {
                    i = smartspaceView.getCurrentCardTopPadding();
                }
                rect.offset(0, i);
            }
            BcSmartspaceDataPlugin.SmartspaceView smartspaceView2 = (BcSmartspaceDataPlugin.SmartspaceView) this.lockscreenSmartspace;
            int selectedPage = smartspaceView2 == null ? -1 : smartspaceView2.getSelectedPage();
            try {
                ILauncherUnlockAnimationController iLauncherUnlockAnimationController = this.launcherUnlockController;
                if (iLauncherUnlockAnimationController != null) {
                    iLauncherUnlockAnimationController.prepareForUnlock(this.willUnlockWithSmartspaceTransition, rect, selectedPage);
                }
            } catch (RemoteException e) {
                Log.e("KeyguardUnlock", "Remote exception in prepareForInWindowUnlockAnimations.", e);
            }
        }
    }

    public final void notifyStartSurfaceBehindRemoteAnimation(@NotNull RemoteAnimationTarget remoteAnimationTarget, long j, boolean z) {
        if (this.surfaceTransactionApplier == null) {
            this.surfaceTransactionApplier = new SyncRtSurfaceTransactionApplier(this.keyguardViewController.getViewRootImpl().getView());
        }
        this.surfaceBehindParams = null;
        this.surfaceBehindRemoteAnimationTarget = remoteAnimationTarget;
        this.surfaceBehindRemoteAnimationStartTime = j;
        if (!z) {
            playCannedUnlockAnimation();
        } else if (!this.keyguardStateController.isFlingingToDismissKeyguard()) {
            fadeInSurfaceBehind();
        } else {
            playCannedUnlockAnimation();
        }
        for (KeyguardUnlockAnimationListener onUnlockAnimationStarted : this.listeners) {
            onUnlockAnimationStarted.onUnlockAnimationStarted(getPlayingCannedUnlockAnimation(), this.biometricUnlockControllerLazy.get().isWakeAndUnlock(), 100, 633);
        }
        finishKeyguardExitRemoteAnimationIfReachThreshold();
    }

    public final void playCannedUnlockAnimation() {
        this.playingCannedUnlockAnimation = true;
        if (this.willUnlockWithInWindowLauncherAnimations) {
            unlockToLauncherWithInWindowAnimations();
        } else if (this.biometricUnlockControllerLazy.get().isWakeAndUnlock()) {
            setSurfaceBehindAppearAmount(1.0f);
            this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
        } else {
            this.surfaceBehindEntryAnimator.start();
        }
    }

    public final void unlockToLauncherWithInWindowAnimations() {
        setSurfaceBehindAppearAmount(1.0f);
        ILauncherUnlockAnimationController iLauncherUnlockAnimationController = this.launcherUnlockController;
        if (iLauncherUnlockAnimationController != null) {
            iLauncherUnlockAnimationController.playUnlockAnimation(true, 633, 100);
        }
        View view = this.lockscreenSmartspace;
        Intrinsics.checkNotNull(view);
        view.setVisibility(4);
        this.handler.postDelayed(new KeyguardUnlockAnimationController$unlockToLauncherWithInWindowAnimations$1(this), 100);
    }

    public final void updateSurfaceBehindAppearAmount() {
        if (this.surfaceBehindRemoteAnimationTarget == null || this.playingCannedUnlockAnimation) {
            return;
        }
        if (this.keyguardStateController.isFlingingToDismissKeyguard()) {
            setSurfaceBehindAppearAmount(this.keyguardStateController.getDismissAmount());
        } else if (this.keyguardStateController.isDismissingFromSwipe() || this.keyguardStateController.isSnappingKeyguardBackAfterSwipe()) {
            setSurfaceBehindAppearAmount((this.keyguardStateController.getDismissAmount() - 0.15f) / 0.15f);
        }
    }

    public void onKeyguardDismissAmountChanged() {
        if (willHandleUnlockAnimation() && this.keyguardViewController.isShowing() && !this.playingCannedUnlockAnimation) {
            showOrHideSurfaceIfDismissAmountThresholdsReached();
            if ((this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard() || this.keyguardViewMediator.get().isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe()) && !this.playingCannedUnlockAnimation) {
                updateSurfaceBehindAppearAmount();
            }
        }
    }

    public final void showOrHideSurfaceIfDismissAmountThresholdsReached() {
        if (this.featureFlags.isEnabled(Flags.NEW_UNLOCK_SWIPE_ANIMATION) && !this.playingCannedUnlockAnimation && this.keyguardStateController.isShowing()) {
            float dismissAmount = this.keyguardStateController.getDismissAmount();
            if (dismissAmount >= 0.15f && !this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard()) {
                this.keyguardViewMediator.get().showSurfaceBehindKeyguard();
            } else if (dismissAmount < 0.15f && this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard()) {
                this.keyguardViewMediator.get().hideSurfaceBehindKeyguard();
                fadeOutSurfaceBehind();
            }
            finishKeyguardExitRemoteAnimationIfReachThreshold();
        }
    }

    public final void finishKeyguardExitRemoteAnimationIfReachThreshold() {
        if (KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation && this.keyguardViewController.isShowing() && this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard() && this.keyguardViewMediator.get().isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe()) {
            float dismissAmount = this.keyguardStateController.getDismissAmount();
            if (dismissAmount >= 1.0f || (this.keyguardStateController.isDismissingFromSwipe() && !this.keyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture() && dismissAmount >= 0.3f)) {
                setSurfaceBehindAppearAmount(1.0f);
                this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
            }
        }
    }

    public final void setSurfaceBehindAppearAmount(float f) {
        RemoteAnimationTarget remoteAnimationTarget = this.surfaceBehindRemoteAnimationTarget;
        if (remoteAnimationTarget != null) {
            Intrinsics.checkNotNull(remoteAnimationTarget);
            int height = remoteAnimationTarget.screenSpaceBounds.height();
            float clamp = (MathUtils.clamp(f, 0.0f, 1.0f) * 0.050000012f) + 0.95f;
            Matrix matrix = this.surfaceBehindMatrix;
            RemoteAnimationTarget remoteAnimationTarget2 = this.surfaceBehindRemoteAnimationTarget;
            Intrinsics.checkNotNull(remoteAnimationTarget2);
            float f2 = (float) height;
            matrix.setScale(clamp, clamp, ((float) remoteAnimationTarget2.screenSpaceBounds.width()) / 2.0f, 0.66f * f2);
            this.surfaceBehindMatrix.postTranslate(0.0f, f2 * 0.05f * (1.0f - f));
            if (!this.keyguardStateController.isSnappingKeyguardBackAfterSwipe()) {
                f = this.surfaceBehindAlpha;
            }
            RemoteAnimationTarget remoteAnimationTarget3 = this.surfaceBehindRemoteAnimationTarget;
            Intrinsics.checkNotNull(remoteAnimationTarget3);
            applyParamsToSurface(new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget3.leash).withMatrix(this.surfaceBehindMatrix).withCornerRadius(this.roundedCornerRadius).withAlpha(f).build());
        }
    }

    public final void notifyFinishedKeyguardExitAnimation(boolean z) {
        this.handler.removeCallbacksAndMessages((Object) null);
        setSurfaceBehindAppearAmount(1.0f);
        ILauncherUnlockAnimationController iLauncherUnlockAnimationController = this.launcherUnlockController;
        if (iLauncherUnlockAnimationController != null) {
            iLauncherUnlockAnimationController.setUnlockAmount(1.0f, false);
        }
        this.surfaceBehindRemoteAnimationTarget = null;
        this.surfaceBehindParams = null;
        this.playingCannedUnlockAnimation = false;
        this.willUnlockWithInWindowLauncherAnimations = false;
        this.willUnlockWithSmartspaceTransition = false;
        View view = this.lockscreenSmartspace;
        if (view != null) {
            view.setVisibility(0);
        }
        for (KeyguardUnlockAnimationListener onUnlockAnimationFinished : this.listeners) {
            onUnlockAnimationFinished.onUnlockAnimationFinished();
        }
    }

    public final void hideKeyguardViewAfterRemoteAnimation() {
        if (this.keyguardViewController.isShowing()) {
            this.keyguardViewController.hide(this.surfaceBehindRemoteAnimationStartTime, 0);
        } else {
            Log.e("KeyguardUnlock", "#hideKeyguardViewAfterRemoteAnimation called when keyguard view is not showing. Ignoring...");
        }
    }

    public final void applyParamsToSurface(SyncRtSurfaceTransactionApplier.SurfaceParams surfaceParams) {
        SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier = this.surfaceTransactionApplier;
        Intrinsics.checkNotNull(syncRtSurfaceTransactionApplier);
        syncRtSurfaceTransactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{surfaceParams});
        this.surfaceBehindParams = surfaceParams;
    }

    public final void fadeInSurfaceBehind() {
        this.surfaceBehindAlphaAnimator.cancel();
        this.surfaceBehindAlphaAnimator.start();
    }

    public final void fadeOutSurfaceBehind() {
        this.surfaceBehindAlphaAnimator.cancel();
        this.surfaceBehindAlphaAnimator.reverse();
    }

    public final boolean shouldPerformSmartspaceTransition() {
        SmartspaceState smartspaceState;
        boolean z;
        if (!this.featureFlags.isEnabled(Flags.SMARTSPACE_SHARED_ELEMENT_TRANSITION_ENABLED) || this.launcherUnlockController == null || this.lockscreenSmartspace == null || (smartspaceState = this.launcherSmartspaceState) == null) {
            return false;
        }
        if (smartspaceState != null && smartspaceState.getVisibleOnScreen()) {
            z = true;
        } else {
            z = false;
        }
        if (!z || !Companion.isNexusLauncherUnderneath() || this.biometricUnlockControllerLazy.get().isWakeAndUnlock()) {
            return false;
        }
        if ((this.keyguardStateController.canDismissLockScreen() || this.biometricUnlockControllerLazy.get().isBiometricUnlock()) && !this.keyguardStateController.isBouncerShowing() && !this.keyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture() && !Utilities.isTablet(this.context)) {
            return true;
        }
        return false;
    }

    public final boolean isUnlockingWithSmartSpaceTransition() {
        return this.willUnlockWithSmartspaceTransition;
    }

    public final boolean willHandleUnlockAnimation() {
        return KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation;
    }

    public final boolean isAnimatingBetweenKeyguardAndSurfaceBehind() {
        return this.keyguardViewMediator.get().isAnimatingBetweenKeyguardAndSurfaceBehind();
    }

    public final boolean isPlayingCannedUnlockAnimation() {
        return this.playingCannedUnlockAnimation;
    }

    /* compiled from: KeyguardUnlockAnimationController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final boolean isNexusLauncherUnderneath() {
            ComponentName componentName;
            String className;
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
            if (runningTask == null || (componentName = runningTask.topActivity) == null || (className = componentName.getClassName()) == null) {
                return false;
            }
            return className.equals("com.google.android.apps.nexuslauncher.NexusLauncherActivity");
        }

        public final boolean isFoldable(@NotNull Context context) {
            return !(context.getResources().getIntArray(17236068).length == 0);
        }
    }
}
