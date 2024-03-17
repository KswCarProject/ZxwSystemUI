package com.android.wm.shell.draganddrop;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DragLayout$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SurfaceControl.Transaction f$0;
    public final /* synthetic */ SurfaceControl f$1;

    public /* synthetic */ DragLayout$$ExternalSyntheticLambda0(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        this.f$0 = transaction;
        this.f$1 = surfaceControl;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        DragLayout.lambda$hideDragSurface$0(this.f$0, this.f$1, valueAnimator);
    }
}
