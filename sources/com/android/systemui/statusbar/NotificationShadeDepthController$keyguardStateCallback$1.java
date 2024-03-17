package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.policy.KeyguardStateController;

/* compiled from: NotificationShadeDepthController.kt */
public final class NotificationShadeDepthController$keyguardStateCallback$1 implements KeyguardStateController.Callback {
    public final /* synthetic */ NotificationShadeDepthController this$0;

    public NotificationShadeDepthController$keyguardStateCallback$1(NotificationShadeDepthController notificationShadeDepthController) {
        this.this$0 = notificationShadeDepthController;
    }

    public void onKeyguardFadingAwayChanged() {
        if (this.this$0.keyguardStateController.isKeyguardFadingAway() && this.this$0.biometricUnlockController.getMode() == 1) {
            Animator access$getKeyguardAnimator$p = this.this$0.keyguardAnimator;
            if (access$getKeyguardAnimator$p != null) {
                access$getKeyguardAnimator$p.cancel();
            }
            NotificationShadeDepthController notificationShadeDepthController = this.this$0;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            NotificationShadeDepthController notificationShadeDepthController2 = this.this$0;
            ofFloat.setDuration(notificationShadeDepthController2.dozeParameters.getWallpaperFadeOutDuration());
            ofFloat.setStartDelay(notificationShadeDepthController2.keyguardStateController.getKeyguardFadingAwayDelay());
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofFloat.addUpdateListener(new NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1(notificationShadeDepthController2));
            ofFloat.addListener(new NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$2(notificationShadeDepthController2));
            ofFloat.start();
            notificationShadeDepthController.keyguardAnimator = ofFloat;
        }
    }

    public void onKeyguardShowingChanged() {
        if (this.this$0.keyguardStateController.isShowing()) {
            Animator access$getKeyguardAnimator$p = this.this$0.keyguardAnimator;
            if (access$getKeyguardAnimator$p != null) {
                access$getKeyguardAnimator$p.cancel();
            }
            Animator access$getNotificationAnimator$p = this.this$0.notificationAnimator;
            if (access$getNotificationAnimator$p != null) {
                access$getNotificationAnimator$p.cancel();
            }
        }
    }
}
