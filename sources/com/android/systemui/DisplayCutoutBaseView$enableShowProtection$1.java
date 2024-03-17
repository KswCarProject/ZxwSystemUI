package com.android.systemui;

import android.animation.ValueAnimator;
import org.jetbrains.annotations.NotNull;

/* compiled from: DisplayCutoutBaseView.kt */
public final class DisplayCutoutBaseView$enableShowProtection$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ DisplayCutoutBaseView this$0;

    public DisplayCutoutBaseView$enableShowProtection$1(DisplayCutoutBaseView displayCutoutBaseView) {
        this.this$0 = displayCutoutBaseView;
    }

    public final void onAnimationUpdate(@NotNull ValueAnimator valueAnimator) {
        DisplayCutoutBaseView displayCutoutBaseView = this.this$0;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            displayCutoutBaseView.setCameraProtectionProgress(((Float) animatedValue).floatValue());
            this.this$0.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
