package com.android.wm.shell.pip.phone;

import android.content.ComponentName;
import android.graphics.Rect;
import android.view.SurfaceControl;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipController$IPipImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ ComponentName f$1;
    public final /* synthetic */ Rect f$2;
    public final /* synthetic */ SurfaceControl f$3;

    public /* synthetic */ PipController$IPipImpl$$ExternalSyntheticLambda0(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
        this.f$0 = i;
        this.f$1 = componentName;
        this.f$2 = rect;
        this.f$3 = surfaceControl;
    }

    public final void accept(Object obj) {
        ((PipController) obj).stopSwipePipToHome(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
