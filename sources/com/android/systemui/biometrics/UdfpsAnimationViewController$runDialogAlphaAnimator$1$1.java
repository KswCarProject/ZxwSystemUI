package com.android.systemui.biometrics;

import android.animation.ValueAnimator;

/* compiled from: UdfpsAnimationViewController.kt */
public final class UdfpsAnimationViewController$runDialogAlphaAnimator$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ UdfpsAnimationViewController<T> this$0;

    public UdfpsAnimationViewController$runDialogAlphaAnimator$1$1(UdfpsAnimationViewController<T> udfpsAnimationViewController) {
        this.this$0 = udfpsAnimationViewController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        UdfpsAnimationView access$getView = this.this$0.getView();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            access$getView.setDialogSuggestedAlpha(((Float) animatedValue).floatValue());
            this.this$0.updateAlpha();
            this.this$0.updatePauseAuth();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
