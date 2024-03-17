package com.android.systemui.controls.ui;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.util.MathUtils;
import com.android.internal.graphics.ColorUtils;

/* compiled from: ControlViewHolder.kt */
public final class ControlViewHolder$startBackgroundAnimation$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ Drawable $clipDrawable;
    public final /* synthetic */ int $newBaseColor;
    public final /* synthetic */ int $newClipColor;
    public final /* synthetic */ float $oldAlpha;
    public final /* synthetic */ int $oldBaseColor;
    public final /* synthetic */ int $oldClipColor;
    public final /* synthetic */ ControlViewHolder this$0;

    public ControlViewHolder$startBackgroundAnimation$1$1(int i, int i2, int i3, int i4, float f, ControlViewHolder controlViewHolder, Drawable drawable) {
        this.$oldClipColor = i;
        this.$newClipColor = i2;
        this.$oldBaseColor = i3;
        this.$newBaseColor = i4;
        this.$oldAlpha = f;
        this.this$0 = controlViewHolder;
        this.$clipDrawable = drawable;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            this.this$0.applyBackgroundChange(this.$clipDrawable, ((Integer) animatedValue).intValue(), ColorUtils.blendARGB(this.$oldClipColor, this.$newClipColor, valueAnimator.getAnimatedFraction()), ColorUtils.blendARGB(this.$oldBaseColor, this.$newBaseColor, valueAnimator.getAnimatedFraction()), MathUtils.lerp(this.$oldAlpha, 1.0f, valueAnimator.getAnimatedFraction()));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
