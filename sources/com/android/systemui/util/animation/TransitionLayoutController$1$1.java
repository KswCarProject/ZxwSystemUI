package com.android.systemui.util.animation;

import android.animation.ValueAnimator;

/* compiled from: TransitionLayoutController.kt */
public final class TransitionLayoutController$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ TransitionLayoutController this$0;

    public TransitionLayoutController$1$1(TransitionLayoutController transitionLayoutController) {
        this.this$0 = transitionLayoutController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.this$0.updateStateFromAnimation();
    }
}
