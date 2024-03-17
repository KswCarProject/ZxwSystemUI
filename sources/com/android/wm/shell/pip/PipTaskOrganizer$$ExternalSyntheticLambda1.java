package com.android.wm.shell.pip;

import android.graphics.Rect;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PipTaskOrganizer f$0;
    public final /* synthetic */ Rect f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda1(PipTaskOrganizer pipTaskOrganizer, Rect rect, long j) {
        this.f$0 = pipTaskOrganizer;
        this.f$1 = rect;
        this.f$2 = j;
    }

    public final void run() {
        this.f$0.lambda$enterPipWithAlphaAnimation$2(this.f$1, this.f$2);
    }
}
