package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;

/* compiled from: SystemEventChipAnimationController.kt */
public final class SystemEventChipAnimationController$createMoveOutAnimationForDot$height1$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $chipVerticalCenter;
    public final /* synthetic */ SystemEventChipAnimationController this$0;

    public SystemEventChipAnimationController$createMoveOutAnimationForDot$height1$1$1(SystemEventChipAnimationController systemEventChipAnimationController, int i) {
        this.this$0 = systemEventChipAnimationController;
        this.$chipVerticalCenter = i;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SystemEventChipAnimationController systemEventChipAnimationController = this.this$0;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            systemEventChipAnimationController.updateAnimatedViewBoundsHeight(((Integer) animatedValue).intValue(), this.$chipVerticalCenter);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
