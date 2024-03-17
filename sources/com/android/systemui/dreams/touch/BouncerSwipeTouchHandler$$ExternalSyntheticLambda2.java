package com.android.systemui.dreams.touch;

import android.animation.ValueAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BouncerSwipeTouchHandler$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ BouncerSwipeTouchHandler f$0;
    public final /* synthetic */ float f$1;

    public /* synthetic */ BouncerSwipeTouchHandler$$ExternalSyntheticLambda2(BouncerSwipeTouchHandler bouncerSwipeTouchHandler, float f) {
        this.f$0 = bouncerSwipeTouchHandler;
        this.f$1 = f;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$createExpansionAnimator$2(this.f$1, valueAnimator);
    }
}
