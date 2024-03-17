package com.android.wm.shell.common.split;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitDecorManager$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SplitDecorManager f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ SplitDecorManager$$ExternalSyntheticLambda0(SplitDecorManager splitDecorManager, SurfaceControl.Transaction transaction, boolean z) {
        this.f$0 = splitDecorManager;
        this.f$1 = transaction;
        this.f$2 = z;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$startFadeAnimation$0(this.f$1, this.f$2, valueAnimator);
    }
}
