package com.android.wm.shell.common.split;

import android.animation.ValueAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitLayout$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SplitLayout f$0;

    public /* synthetic */ SplitLayout$$ExternalSyntheticLambda3(SplitLayout splitLayout) {
        this.f$0 = splitLayout;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$flingDividePosition$3(valueAnimator);
    }
}
