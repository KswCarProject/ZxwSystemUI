package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScrimController$$ExternalSyntheticLambda6 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ScrimController f$0;
    public final /* synthetic */ View f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ ScrimController$$ExternalSyntheticLambda6(ScrimController scrimController, View view, int i) {
        this.f$0 = scrimController;
        this.f$1 = view;
        this.f$2 = i;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$startScrimAnimation$3(this.f$1, this.f$2, valueAnimator);
    }
}
