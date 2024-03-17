package com.android.keyguard;

import android.animation.ValueAnimator;

/* compiled from: TextAnimator.kt */
public final class TextAnimator$animator$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ TextAnimator this$0;

    public TextAnimator$animator$1$1(TextAnimator textAnimator) {
        this.this$0 = textAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        TextInterpolator textInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core = this.this$0.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            textInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core.setProgress(((Float) animatedValue).floatValue());
            this.this$0.invalidateCallback.invoke();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
