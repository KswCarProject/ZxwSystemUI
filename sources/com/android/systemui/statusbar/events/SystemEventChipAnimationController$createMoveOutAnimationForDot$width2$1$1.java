package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;

/* compiled from: SystemEventChipAnimationController.kt */
public final class SystemEventChipAnimationController$createMoveOutAnimationForDot$width2$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SystemEventChipAnimationController this$0;

    public SystemEventChipAnimationController$createMoveOutAnimationForDot$width2$1$1(SystemEventChipAnimationController systemEventChipAnimationController) {
        this.this$0 = systemEventChipAnimationController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SystemEventChipAnimationController systemEventChipAnimationController = this.this$0;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            systemEventChipAnimationController.updateAnimatedViewBoundsWidth(((Integer) animatedValue).intValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
