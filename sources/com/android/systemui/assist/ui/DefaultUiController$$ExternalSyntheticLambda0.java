package com.android.systemui.assist.ui;

import android.animation.ValueAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DefaultUiController$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ DefaultUiController f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ DefaultUiController$$ExternalSyntheticLambda0(DefaultUiController defaultUiController, int i) {
        this.f$0 = defaultUiController;
        this.f$1 = i;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$animateInvocationCompletion$0(this.f$1, valueAnimator);
    }
}
