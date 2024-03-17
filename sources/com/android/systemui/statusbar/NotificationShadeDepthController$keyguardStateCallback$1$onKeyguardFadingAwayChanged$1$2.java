package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationShadeDepthController.kt */
public final class NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ NotificationShadeDepthController this$0;

    public NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$2(NotificationShadeDepthController notificationShadeDepthController) {
        this.this$0 = notificationShadeDepthController;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.keyguardAnimator = null;
        this.this$0.setWakeAndUnlockBlurRadius(0.0f);
        NotificationShadeDepthController.scheduleUpdate$default(this.this$0, (View) null, 1, (Object) null);
    }
}
