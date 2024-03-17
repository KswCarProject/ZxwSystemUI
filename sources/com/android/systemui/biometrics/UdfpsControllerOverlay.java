package com.android.systemui.biometrics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.os.RemoteException;
import android.util.Log;
import android.util.RotationUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.R$array;
import com.android.systemui.R$layout;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.time.SystemClock;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UdfpsControllerOverlay.kt */
public final class UdfpsControllerOverlay {
    @NotNull
    public final AccessibilityManager accessibilityManager;
    @NotNull
    public final ActivityLaunchAnimator activityLaunchAnimator;
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final Context context;
    @NotNull
    public final IUdfpsOverlayControllerCallback controllerCallback;
    @NotNull
    public final WindowManager.LayoutParams coreLayoutParams;
    @NotNull
    public final SystemUIDialogManager dialogManager;
    @NotNull
    public final DumpManager dumpManager;
    @Nullable
    public final UdfpsEnrollHelper enrollHelper;
    public final boolean halControlsIllumination;
    @NotNull
    public UdfpsHbmProvider hbmProvider;
    @NotNull
    public final LayoutInflater inflater;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    public final Function3<View, MotionEvent, Boolean, Boolean> onTouch;
    @NotNull
    public UdfpsOverlayParams overlayParams = new UdfpsOverlayParams((Rect) null, 0, 0, 0.0f, 0, 31, (DefaultConstructorMarker) null);
    @Nullable
    public AccessibilityManager.TouchExplorationStateChangeListener overlayTouchListener;
    @Nullable
    public UdfpsView overlayView;
    @NotNull
    public final PanelExpansionStateManager panelExpansionStateManager;
    public final long requestId;
    public final int requestReason;
    @NotNull
    public final StatusBarKeyguardViewManager statusBarKeyguardViewManager;
    @NotNull
    public final StatusBarStateController statusBarStateController;
    @NotNull
    public final SystemClock systemClock;
    public boolean touchExplorationEnabled;
    @NotNull
    public final LockscreenShadeTransitionController transitionController;
    @NotNull
    public final UnlockedScreenOffAnimationController unlockedScreenOffAnimationController;
    @NotNull
    public final WindowManager windowManager;

    public UdfpsControllerOverlay(@NotNull Context context2, @NotNull FingerprintManager fingerprintManager, @NotNull LayoutInflater layoutInflater, @NotNull WindowManager windowManager2, @NotNull AccessibilityManager accessibilityManager2, @NotNull StatusBarStateController statusBarStateController2, @NotNull PanelExpansionStateManager panelExpansionStateManager2, @NotNull StatusBarKeyguardViewManager statusBarKeyguardViewManager2, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor2, @NotNull SystemUIDialogManager systemUIDialogManager, @NotNull DumpManager dumpManager2, @NotNull LockscreenShadeTransitionController lockscreenShadeTransitionController, @NotNull ConfigurationController configurationController2, @NotNull SystemClock systemClock2, @NotNull KeyguardStateController keyguardStateController2, @NotNull UnlockedScreenOffAnimationController unlockedScreenOffAnimationController2, boolean z, @NotNull UdfpsHbmProvider udfpsHbmProvider, long j, int i, @NotNull IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback, @NotNull Function3<? super View, ? super MotionEvent, ? super Boolean, Boolean> function3, @NotNull ActivityLaunchAnimator activityLaunchAnimator2) {
        UdfpsEnrollHelper udfpsEnrollHelper;
        int i2 = i;
        this.context = context2;
        this.inflater = layoutInflater;
        this.windowManager = windowManager2;
        this.accessibilityManager = accessibilityManager2;
        this.statusBarStateController = statusBarStateController2;
        this.panelExpansionStateManager = panelExpansionStateManager2;
        this.statusBarKeyguardViewManager = statusBarKeyguardViewManager2;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.dialogManager = systemUIDialogManager;
        this.dumpManager = dumpManager2;
        this.transitionController = lockscreenShadeTransitionController;
        this.configurationController = configurationController2;
        this.systemClock = systemClock2;
        this.keyguardStateController = keyguardStateController2;
        this.unlockedScreenOffAnimationController = unlockedScreenOffAnimationController2;
        this.halControlsIllumination = z;
        this.hbmProvider = udfpsHbmProvider;
        this.requestId = j;
        this.requestReason = i2;
        this.controllerCallback = iUdfpsOverlayControllerCallback;
        this.onTouch = function3;
        this.activityLaunchAnimator = activityLaunchAnimator2;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2009, 0, -3);
        layoutParams.setTitle("UdfpsControllerOverlay");
        layoutParams.setFitInsetsTypes(0);
        layoutParams.gravity = 51;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.flags = 25166120;
        layoutParams.privateFlags = 536870912;
        layoutParams.accessibilityTitle = " ";
        this.coreLayoutParams = layoutParams;
        if (UdfpsControllerOverlayKt.isEnrollmentReason(i)) {
            FingerprintManager fingerprintManager2 = fingerprintManager;
            udfpsEnrollHelper = new UdfpsEnrollHelper(context2, fingerprintManager, i2);
        } else {
            udfpsEnrollHelper = null;
        }
        this.enrollHelper = udfpsEnrollHelper;
    }

    public final long getRequestId() {
        return this.requestId;
    }

    public final int getRequestReason() {
        return this.requestReason;
    }

    @Nullable
    public final UdfpsView getOverlayView() {
        return this.overlayView;
    }

    public final boolean isShowing() {
        return this.overlayView != null;
    }

    public final boolean isHiding() {
        return this.overlayView == null;
    }

    @Nullable
    public final UdfpsAnimationViewController<?> getAnimationViewController() {
        UdfpsView udfpsView = this.overlayView;
        if (udfpsView == null) {
            return null;
        }
        return udfpsView.getAnimationViewController();
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public final boolean show(@NotNull UdfpsController udfpsController, @NotNull UdfpsOverlayParams udfpsOverlayParams) {
        if (this.overlayView == null) {
            this.overlayParams = udfpsOverlayParams;
            try {
                View inflate = this.inflater.inflate(R$layout.udfps_view, (ViewGroup) null, false);
                if (inflate != null) {
                    UdfpsView udfpsView = (UdfpsView) inflate;
                    udfpsView.setOverlayParams(udfpsOverlayParams);
                    udfpsView.setHalControlsIllumination(this.halControlsIllumination);
                    udfpsView.setHbmProvider(this.hbmProvider);
                    UdfpsAnimationViewController<?> inflateUdfpsAnimation = inflateUdfpsAnimation(udfpsView, udfpsController);
                    if (inflateUdfpsAnimation != null) {
                        inflateUdfpsAnimation.init();
                        udfpsView.setAnimationViewController(inflateUdfpsAnimation);
                    }
                    if (UdfpsControllerOverlayKt.isImportantForAccessibility(getRequestReason())) {
                        udfpsView.setImportantForAccessibility(2);
                    }
                    this.windowManager.addView(udfpsView, updateDimensions(this.coreLayoutParams, inflateUdfpsAnimation));
                    this.touchExplorationEnabled = this.accessibilityManager.isTouchExplorationEnabled();
                    UdfpsControllerOverlay$show$1$1 udfpsControllerOverlay$show$1$1 = new UdfpsControllerOverlay$show$1$1(this, udfpsView);
                    this.overlayTouchListener = udfpsControllerOverlay$show$1$1;
                    AccessibilityManager accessibilityManager2 = this.accessibilityManager;
                    Intrinsics.checkNotNull(udfpsControllerOverlay$show$1$1);
                    accessibilityManager2.addTouchExplorationStateChangeListener(udfpsControllerOverlay$show$1$1);
                    AccessibilityManager.TouchExplorationStateChangeListener touchExplorationStateChangeListener = this.overlayTouchListener;
                    if (touchExplorationStateChangeListener != null) {
                        touchExplorationStateChangeListener.onTouchExplorationStateChanged(true);
                    }
                    this.overlayView = udfpsView;
                    return true;
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsView");
            } catch (RuntimeException e) {
                Log.e("UdfpsControllerOverlay", "showUdfpsOverlay | failed to add window", e);
            }
        } else {
            Log.v("UdfpsControllerOverlay", "showUdfpsOverlay | the overlay is already showing");
            return false;
        }
    }

    @Nullable
    public final UdfpsAnimationViewController<?> inflateUdfpsAnimation(@NotNull UdfpsView udfpsView, @NotNull UdfpsController udfpsController) {
        UdfpsView udfpsView2 = udfpsView;
        switch (this.requestReason) {
            case 1:
            case 2:
                View inflate = this.inflater.inflate(R$layout.udfps_enroll_view, (ViewGroup) null);
                if (inflate != null) {
                    UdfpsEnrollView udfpsEnrollView = (UdfpsEnrollView) inflate;
                    udfpsView2.addView(udfpsEnrollView);
                    udfpsEnrollView.updateSensorLocation(this.overlayParams.getSensorBounds());
                    UdfpsEnrollHelper udfpsEnrollHelper = this.enrollHelper;
                    if (udfpsEnrollHelper != null) {
                        return new UdfpsEnrollViewController(udfpsEnrollView, udfpsEnrollHelper, this.statusBarStateController, this.panelExpansionStateManager, this.dialogManager, this.dumpManager, this.overlayParams.getScaleFactor());
                    }
                    throw new IllegalStateException("no enrollment helper");
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsEnrollView");
            case 3:
                View inflate2 = this.inflater.inflate(R$layout.udfps_bp_view, (ViewGroup) null);
                if (inflate2 != null) {
                    UdfpsBpView udfpsBpView = (UdfpsBpView) inflate2;
                    udfpsView2.addView(udfpsBpView);
                    return new UdfpsBpViewController(udfpsBpView, this.statusBarStateController, this.panelExpansionStateManager, this.dialogManager, this.dumpManager);
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsBpView");
            case 4:
                View inflate3 = this.inflater.inflate(R$layout.udfps_keyguard_view, (ViewGroup) null);
                if (inflate3 != null) {
                    UdfpsKeyguardView udfpsKeyguardView = (UdfpsKeyguardView) inflate3;
                    udfpsView2.addView(udfpsKeyguardView);
                    return new UdfpsKeyguardViewController(udfpsKeyguardView, this.statusBarStateController, this.panelExpansionStateManager, this.statusBarKeyguardViewManager, this.keyguardUpdateMonitor, this.dumpManager, this.transitionController, this.configurationController, this.systemClock, this.keyguardStateController, this.unlockedScreenOffAnimationController, this.dialogManager, udfpsController, this.activityLaunchAnimator);
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsKeyguardView");
            case 5:
            case 6:
                View inflate4 = this.inflater.inflate(R$layout.udfps_fpm_other_view, (ViewGroup) null);
                if (inflate4 != null) {
                    UdfpsFpmOtherView udfpsFpmOtherView = (UdfpsFpmOtherView) inflate4;
                    udfpsView2.addView(udfpsFpmOtherView);
                    return new UdfpsFpmOtherViewController(udfpsFpmOtherView, this.statusBarStateController, this.panelExpansionStateManager, this.dialogManager, this.dumpManager);
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsFpmOtherView");
            default:
                Log.e("UdfpsControllerOverlay", "Animation for reason " + this.requestReason + " not supported yet");
                return null;
        }
    }

    public final boolean hide() {
        boolean isShowing = isShowing();
        UdfpsView udfpsView = this.overlayView;
        if (udfpsView != null) {
            if (udfpsView.isIlluminationRequested()) {
                udfpsView.stopIllumination();
            }
            this.windowManager.removeView(udfpsView);
            udfpsView.setOnTouchListener((View.OnTouchListener) null);
            udfpsView.setOnHoverListener((View.OnHoverListener) null);
            udfpsView.setAnimationViewController((UdfpsAnimationViewController<?>) null);
            AccessibilityManager.TouchExplorationStateChangeListener touchExplorationStateChangeListener = this.overlayTouchListener;
            if (touchExplorationStateChangeListener != null) {
                this.accessibilityManager.removeTouchExplorationStateChangeListener(touchExplorationStateChangeListener);
            }
        }
        this.overlayView = null;
        this.overlayTouchListener = null;
        return isShowing;
    }

    public final void onEnrollmentProgress(int i) {
        UdfpsEnrollHelper udfpsEnrollHelper = this.enrollHelper;
        if (udfpsEnrollHelper != null) {
            udfpsEnrollHelper.onEnrollmentProgress(i);
        }
    }

    public final void onAcquiredGood() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.enrollHelper;
        if (udfpsEnrollHelper != null) {
            udfpsEnrollHelper.animateIfLastStep();
        }
    }

    public final void onEnrollmentHelp() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.enrollHelper;
        if (udfpsEnrollHelper != null) {
            udfpsEnrollHelper.onEnrollmentHelp();
        }
    }

    public final void onTouchOutsideOfSensorArea(float f, float f2, float f3, float f4, int i) {
        if (this.touchExplorationEnabled) {
            String[] stringArray = this.context.getResources().getStringArray(R$array.udfps_accessibility_touch_hints);
            if (stringArray.length != 4) {
                Log.e("UdfpsControllerOverlay", "expected exactly 4 touch hints, got " + stringArray + ".size?");
                return;
            }
            String onTouchOutsideOfSensorAreaImpl = onTouchOutsideOfSensorAreaImpl(f, f2, f3, f4, i);
            Log.v("UdfpsControllerOverlay", Intrinsics.stringPlus("Announcing touch outside : ", onTouchOutsideOfSensorAreaImpl));
            UdfpsAnimationViewController<?> animationViewController = getAnimationViewController();
            if (animationViewController != null) {
                animationViewController.doAnnounceForAccessibility(onTouchOutsideOfSensorAreaImpl);
            }
        }
    }

    @NotNull
    public final String onTouchOutsideOfSensorAreaImpl(float f, float f2, float f3, float f4, int i) {
        String[] stringArray = this.context.getResources().getStringArray(R$array.udfps_accessibility_touch_hints);
        double atan2 = Math.atan2((double) (f4 - f2), (double) (f - f3));
        if (atan2 < 0.0d) {
            atan2 += 6.283185307179586d;
        }
        double degrees = Math.toDegrees(atan2);
        double length = 360.0d / ((double) stringArray.length);
        int length2 = ((int) (((degrees + (length / 2.0d)) % ((double) 360)) / length)) % stringArray.length;
        if (i == 1) {
            length2 = (length2 + 1) % stringArray.length;
        }
        if (i == 3) {
            length2 = (length2 + 3) % stringArray.length;
        }
        return stringArray[length2];
    }

    public final void cancel() {
        try {
            this.controllerCallback.onUserCanceled();
        } catch (RemoteException e) {
            Log.e("UdfpsControllerOverlay", "Remote exception", e);
        }
    }

    public final boolean matchesRequestId(long j) {
        long j2 = this.requestId;
        return j2 == -1 || j2 == j;
    }

    public final WindowManager.LayoutParams updateDimensions(WindowManager.LayoutParams layoutParams, UdfpsAnimationViewController<?> udfpsAnimationViewController) {
        int i = 0;
        int paddingX = udfpsAnimationViewController == null ? 0 : udfpsAnimationViewController.getPaddingX();
        if (udfpsAnimationViewController != null) {
            i = udfpsAnimationViewController.getPaddingY();
        }
        if (udfpsAnimationViewController != null && udfpsAnimationViewController.listenForTouchesOutsideView()) {
            layoutParams.flags |= 262144;
        }
        Rect rect = new Rect(this.overlayParams.getSensorBounds());
        int rotation = this.overlayParams.getRotation();
        if (rotation == 1 || rotation == 3) {
            if (!shouldRotate(udfpsAnimationViewController)) {
                Log.v("UdfpsControllerOverlay", "Skip rotating UDFPS bounds " + Surface.rotationToString(rotation) + " animation=" + udfpsAnimationViewController + " isGoingToSleep=" + this.keyguardUpdateMonitor.isGoingToSleep() + " isOccluded=" + this.keyguardStateController.isOccluded());
            } else {
                Log.v("UdfpsControllerOverlay", Intrinsics.stringPlus("Rotate UDFPS bounds ", Surface.rotationToString(rotation)));
                RotationUtils.rotateBounds(rect, this.overlayParams.getNaturalDisplayWidth(), this.overlayParams.getNaturalDisplayHeight(), rotation);
            }
        }
        layoutParams.x = rect.left - paddingX;
        layoutParams.y = rect.top - i;
        layoutParams.height = rect.height() + (paddingX * 2);
        layoutParams.width = rect.width() + (i * 2);
        return layoutParams;
    }

    public final boolean shouldRotate(UdfpsAnimationViewController<?> udfpsAnimationViewController) {
        if (!(udfpsAnimationViewController instanceof UdfpsKeyguardViewController)) {
            return true;
        }
        if (this.keyguardUpdateMonitor.isGoingToSleep() || !this.keyguardStateController.isOccluded()) {
            return false;
        }
        return true;
    }
}
