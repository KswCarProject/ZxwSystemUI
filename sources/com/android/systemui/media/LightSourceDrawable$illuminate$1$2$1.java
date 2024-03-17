package com.android.systemui.media;

import android.animation.ValueAnimator;

/* compiled from: LightSourceDrawable.kt */
public final class LightSourceDrawable$illuminate$1$2$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LightSourceDrawable this$0;

    public LightSourceDrawable$illuminate$1$2$1(LightSourceDrawable lightSourceDrawable) {
        this.this$0 = lightSourceDrawable;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        RippleData access$getRippleData$p = this.this$0.rippleData;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            access$getRippleData$p.setProgress(((Float) animatedValue).floatValue());
            this.this$0.invalidateSelf();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
