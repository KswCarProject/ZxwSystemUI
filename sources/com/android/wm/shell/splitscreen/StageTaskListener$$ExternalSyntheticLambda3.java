package com.android.wm.shell.splitscreen;

import android.graphics.Point;
import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageTaskListener$$ExternalSyntheticLambda3 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ SurfaceControl f$0;
    public final /* synthetic */ Point f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ StageTaskListener$$ExternalSyntheticLambda3(SurfaceControl surfaceControl, Point point, boolean z) {
        this.f$0 = surfaceControl;
        this.f$1 = point;
        this.f$2 = z;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        StageTaskListener.lambda$updateChildTaskSurface$3(this.f$0, this.f$1, this.f$2, transaction);
    }
}
