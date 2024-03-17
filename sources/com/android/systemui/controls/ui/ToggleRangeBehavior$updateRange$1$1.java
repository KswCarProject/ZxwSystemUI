package com.android.systemui.controls.ui;

import android.animation.ValueAnimator;
import android.graphics.drawable.ClipDrawable;

/* compiled from: ToggleRangeBehavior.kt */
public final class ToggleRangeBehavior$updateRange$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ToggleRangeBehavior this$0;

    public ToggleRangeBehavior$updateRange$1$1(ToggleRangeBehavior toggleRangeBehavior) {
        this.this$0 = toggleRangeBehavior;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ClipDrawable clipLayer = this.this$0.getCvh().getClipLayer();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            clipLayer.setLevel(((Integer) animatedValue).intValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
