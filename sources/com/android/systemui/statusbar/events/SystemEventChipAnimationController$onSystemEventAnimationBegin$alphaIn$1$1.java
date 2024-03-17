package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;
import android.view.View;

/* compiled from: SystemEventChipAnimationController.kt */
public final class SystemEventChipAnimationController$onSystemEventAnimationBegin$alphaIn$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ValueAnimator $this_apply;
    public final /* synthetic */ SystemEventChipAnimationController this$0;

    public SystemEventChipAnimationController$onSystemEventAnimationBegin$alphaIn$1$1(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator) {
        this.this$0 = systemEventChipAnimationController;
        this.$this_apply = valueAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        BackgroundAnimatableView access$getCurrentAnimatedView$p = this.this$0.currentAnimatedView;
        View view = access$getCurrentAnimatedView$p == null ? null : access$getCurrentAnimatedView$p.getView();
        if (view != null) {
            Object animatedValue = this.$this_apply.getAnimatedValue();
            if (animatedValue != null) {
                view.setAlpha(((Float) animatedValue).floatValue());
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
        }
    }
}
