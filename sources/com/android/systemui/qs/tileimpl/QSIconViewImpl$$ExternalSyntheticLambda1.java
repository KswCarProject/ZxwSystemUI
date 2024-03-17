package com.android.systemui.qs.tileimpl;

import android.animation.ValueAnimator;
import android.widget.ImageView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QSIconViewImpl$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ QSIconViewImpl f$0;
    public final /* synthetic */ ImageView f$1;

    public /* synthetic */ QSIconViewImpl$$ExternalSyntheticLambda1(QSIconViewImpl qSIconViewImpl, ImageView imageView) {
        this.f$0 = qSIconViewImpl;
        this.f$1 = imageView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$animateGrayScale$1(this.f$1, valueAnimator);
    }
}
