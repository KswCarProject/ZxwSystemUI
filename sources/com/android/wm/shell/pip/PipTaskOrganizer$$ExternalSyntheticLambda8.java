package com.android.wm.shell.pip;

import android.graphics.Rect;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ PipTaskOrganizer f$0;
    public final /* synthetic */ Rect f$1;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda8(PipTaskOrganizer pipTaskOrganizer, Rect rect) {
        this.f$0 = pipTaskOrganizer;
        this.f$1 = rect;
    }

    public final void run() {
        this.f$0.lambda$onFixedRotationFinished$5(this.f$1);
    }
}
