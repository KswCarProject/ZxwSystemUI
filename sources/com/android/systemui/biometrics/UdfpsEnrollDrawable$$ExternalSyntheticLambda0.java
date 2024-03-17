package com.android.systemui.biometrics;

import android.animation.ValueAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UdfpsEnrollDrawable$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ UdfpsEnrollDrawable f$0;

    public /* synthetic */ UdfpsEnrollDrawable$$ExternalSyntheticLambda0(UdfpsEnrollDrawable udfpsEnrollDrawable) {
        this.f$0 = udfpsEnrollDrawable;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onEnrollmentProgress$0(valueAnimator);
    }
}
