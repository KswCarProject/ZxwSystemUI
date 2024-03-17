package com.android.keyguard;

import android.animation.ValueAnimator;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardPinBasedInputView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ View f$0;

    public /* synthetic */ KeyguardPinBasedInputView$$ExternalSyntheticLambda0(View view) {
        this.f$0 = view;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        KeyguardPinBasedInputView.lambda$startErrorAnimation$0(this.f$0, valueAnimator);
    }
}
