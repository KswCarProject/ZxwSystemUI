package com.android.wm.shell.kidsmode;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KidsModeTaskOrganizer$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ SurfaceControl f$0;

    public /* synthetic */ KidsModeTaskOrganizer$$ExternalSyntheticLambda0(SurfaceControl surfaceControl) {
        this.f$0 = surfaceControl;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        KidsModeTaskOrganizer.lambda$onTaskAppeared$1(this.f$0, transaction);
    }
}
