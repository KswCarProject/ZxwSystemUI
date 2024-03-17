package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;

/* compiled from: SystemEventChipAnimationController.kt */
public final class SystemEventChipAnimationController$createMoveOutAnimationForDot$moveOut$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ValueAnimator $this_apply;
    public final /* synthetic */ SystemEventChipAnimationController this$0;

    public SystemEventChipAnimationController$createMoveOutAnimationForDot$moveOut$1$1(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator) {
        this.this$0 = systemEventChipAnimationController;
        this.$this_apply = valueAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        int i;
        if (this.this$0.animationDirection == 1) {
            Object animatedValue = this.$this_apply.getAnimatedValue();
            if (animatedValue != null) {
                i = ((Integer) animatedValue).intValue();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
        } else {
            Object animatedValue2 = this.$this_apply.getAnimatedValue();
            if (animatedValue2 != null) {
                i = -((Integer) animatedValue2).intValue();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
        }
        this.this$0.updateAnimatedBoundsX(i);
    }
}
