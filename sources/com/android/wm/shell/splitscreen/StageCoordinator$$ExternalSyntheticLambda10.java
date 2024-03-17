package com.android.wm.shell.splitscreen;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda10 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ SurfaceControl f$1;
    public final /* synthetic */ SurfaceControl.Transaction f$2;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda10(StageCoordinator stageCoordinator, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        this.f$0 = stageCoordinator;
        this.f$1 = surfaceControl;
        this.f$2 = transaction;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$applyDividerVisibility$7(this.f$1, this.f$2, valueAnimator);
    }
}
