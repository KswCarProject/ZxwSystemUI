package com.android.systemui.statusbar;

import android.animation.ValueAnimator;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationShadeDepthController.kt */
public final class NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ NotificationShadeDepthController this$0;

    public NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1(NotificationShadeDepthController notificationShadeDepthController) {
        this.this$0 = notificationShadeDepthController;
    }

    public final void onAnimationUpdate(@NotNull ValueAnimator valueAnimator) {
        NotificationShadeDepthController notificationShadeDepthController = this.this$0;
        BlurUtils access$getBlurUtils$p = notificationShadeDepthController.blurUtils;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            notificationShadeDepthController.setWakeAndUnlockBlurRadius(access$getBlurUtils$p.blurRadiusOfRatio(((Float) animatedValue).floatValue()));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
