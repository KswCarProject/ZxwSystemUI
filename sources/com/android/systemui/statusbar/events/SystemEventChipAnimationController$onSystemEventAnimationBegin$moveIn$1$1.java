package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;

/* compiled from: SystemEventChipAnimationController.kt */
public final class SystemEventChipAnimationController$onSystemEventAnimationBegin$moveIn$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ValueAnimator $this_apply;
    public final /* synthetic */ SystemEventChipAnimationController this$0;

    public SystemEventChipAnimationController$onSystemEventAnimationBegin$moveIn$1$1(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator) {
        this.this$0 = systemEventChipAnimationController;
        this.$this_apply = valueAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SystemEventChipAnimationController systemEventChipAnimationController = this.this$0;
        Object animatedValue = this.$this_apply.getAnimatedValue();
        if (animatedValue != null) {
            systemEventChipAnimationController.updateAnimatedViewBoundsWidth(((Integer) animatedValue).intValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
