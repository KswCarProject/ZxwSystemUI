package com.android.keyguard;

import android.animation.TimeInterpolator;

/* compiled from: AnimatableClockView.kt */
public final class AnimatableClockView$setTextStyle$1 implements Runnable {
    public final /* synthetic */ Integer $color;
    public final /* synthetic */ long $delay;
    public final /* synthetic */ long $duration;
    public final /* synthetic */ TimeInterpolator $interpolator;
    public final /* synthetic */ Runnable $onAnimationEnd;
    public final /* synthetic */ float $textSize;
    public final /* synthetic */ int $weight;
    public final /* synthetic */ AnimatableClockView this$0;

    public AnimatableClockView$setTextStyle$1(AnimatableClockView animatableClockView, int i, float f, Integer num, long j, TimeInterpolator timeInterpolator, long j2, Runnable runnable) {
        this.this$0 = animatableClockView;
        this.$weight = i;
        this.$textSize = f;
        this.$color = num;
        this.$duration = j;
        this.$interpolator = timeInterpolator;
        this.$delay = j2;
        this.$onAnimationEnd = runnable;
    }

    public final void run() {
        TextAnimator access$getTextAnimator$p = this.this$0.textAnimator;
        if (access$getTextAnimator$p != null) {
            access$getTextAnimator$p.setTextStyle(this.$weight, this.$textSize, this.$color, false, this.$duration, this.$interpolator, this.$delay, this.$onAnimationEnd);
        }
    }
}
