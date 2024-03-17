package com.android.systemui.biometrics;

import android.animation.ValueAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AuthBiometricView$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AuthBiometricView f$0;

    public /* synthetic */ AuthBiometricView$$ExternalSyntheticLambda2(AuthBiometricView authBiometricView) {
        this.f$0 = authBiometricView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }
}
