package com.android.wm.shell.splitscreen;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageTaskListener$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ StageTaskListener f$0;

    public /* synthetic */ StageTaskListener$$ExternalSyntheticLambda0(StageTaskListener stageTaskListener) {
        this.f$0 = stageTaskListener;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$onTaskVanished$2(transaction);
    }
}
