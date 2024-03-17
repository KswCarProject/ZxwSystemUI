package com.android.wm.shell.pip;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda5 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda5(Runnable runnable) {
        this.f$0 = runnable;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.run();
    }
}
