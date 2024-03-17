package com.android.wm.shell.pip;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PipTaskOrganizer f$0;
    public final /* synthetic */ SurfaceControl f$1;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda3(PipTaskOrganizer pipTaskOrganizer, SurfaceControl surfaceControl) {
        this.f$0 = pipTaskOrganizer;
        this.f$1 = surfaceControl;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$fadeOutAndRemoveOverlay$8(this.f$1, valueAnimator);
    }
}
