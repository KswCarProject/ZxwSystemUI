package com.android.systemui.qs.tileimpl;

import android.animation.ValueAnimator;

/* compiled from: QSTileViewImpl.kt */
public final class QSTileViewImpl$singleAnimator$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ QSTileViewImpl this$0;

    public QSTileViewImpl$singleAnimator$1$1(QSTileViewImpl qSTileViewImpl) {
        this.this$0 = qSTileViewImpl;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        QSTileViewImpl qSTileViewImpl = this.this$0;
        Object animatedValue = valueAnimator.getAnimatedValue("background");
        if (animatedValue != null) {
            int intValue = ((Integer) animatedValue).intValue();
            Object animatedValue2 = valueAnimator.getAnimatedValue("label");
            if (animatedValue2 != null) {
                int intValue2 = ((Integer) animatedValue2).intValue();
                Object animatedValue3 = valueAnimator.getAnimatedValue("secondaryLabel");
                if (animatedValue3 != null) {
                    int intValue3 = ((Integer) animatedValue3).intValue();
                    Object animatedValue4 = valueAnimator.getAnimatedValue("chevron");
                    if (animatedValue4 != null) {
                        qSTileViewImpl.setAllColors(intValue, intValue2, intValue3, ((Integer) animatedValue4).intValue());
                        return;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
