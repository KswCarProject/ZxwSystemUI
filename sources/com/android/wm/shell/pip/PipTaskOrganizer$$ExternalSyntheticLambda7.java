package com.android.wm.shell.pip;

import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda7 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ PipTaskOrganizer f$0;
    public final /* synthetic */ SurfaceControl f$1;
    public final /* synthetic */ Rect f$2;
    public final /* synthetic */ Rect f$3;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda7(PipTaskOrganizer pipTaskOrganizer, SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        this.f$0 = pipTaskOrganizer;
        this.f$1 = surfaceControl;
        this.f$2 = rect;
        this.f$3 = rect2;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$finishResize$6(this.f$1, this.f$2, this.f$3, transaction);
    }
}
