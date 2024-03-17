package com.android.wm.shell.pip;

import android.graphics.Rect;
import android.view.SurfaceControl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PipTaskOrganizer f$0;
    public final /* synthetic */ Rect f$1;
    public final /* synthetic */ SurfaceControl f$2;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda2(PipTaskOrganizer pipTaskOrganizer, Rect rect, SurfaceControl surfaceControl) {
        this.f$0 = pipTaskOrganizer;
        this.f$1 = rect;
        this.f$2 = surfaceControl;
    }

    public final void run() {
        this.f$0.lambda$onEndOfSwipePipToHomeTransition$3(this.f$1, this.f$2);
    }
}
