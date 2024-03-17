package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import com.android.systemui.screenshot.DraggableConstraintLayout;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DraggableConstraintLayout$SwipeDismissHandler$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ DraggableConstraintLayout.SwipeDismissHandler f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;

    public /* synthetic */ DraggableConstraintLayout$SwipeDismissHandler$$ExternalSyntheticLambda0(DraggableConstraintLayout.SwipeDismissHandler swipeDismissHandler, float f, float f2) {
        this.f$0 = swipeDismissHandler;
        this.f$1 = f;
        this.f$2 = f2;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$createSwipeReturnAnimation$1(this.f$1, this.f$2, valueAnimator);
    }
}
