package com.android.wm.shell;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda3 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ TaskView f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda3(TaskView taskView, int i) {
        this.f$0 = taskView;
        this.f$1 = i;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$onTaskAppeared$7(this.f$1, transaction);
    }
}
