package com.android.wm.shell.splitscreen;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenTransitions$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SplitScreenTransitions f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;
    public final /* synthetic */ SurfaceControl f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ ValueAnimator f$4;

    public /* synthetic */ SplitScreenTransitions$$ExternalSyntheticLambda2(SplitScreenTransitions splitScreenTransitions, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, ValueAnimator valueAnimator) {
        this.f$0 = splitScreenTransitions;
        this.f$1 = transaction;
        this.f$2 = surfaceControl;
        this.f$3 = f;
        this.f$4 = valueAnimator;
    }

    public final void run() {
        this.f$0.lambda$startExampleAnimation$2(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
