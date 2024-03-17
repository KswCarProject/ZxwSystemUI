package com.android.systemui.media;

import android.animation.ValueAnimator;

/* compiled from: SquigglyProgress.kt */
public final class SquigglyProgress$animate$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SquigglyProgress this$0;

    public SquigglyProgress$animate$1$1(SquigglyProgress squigglyProgress) {
        this.this$0 = squigglyProgress;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SquigglyProgress squigglyProgress = this.this$0;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            squigglyProgress.heightFraction = ((Float) animatedValue).floatValue();
            this.this$0.invalidateSelf();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
