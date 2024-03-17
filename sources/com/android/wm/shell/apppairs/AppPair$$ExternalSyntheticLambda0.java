package com.android.wm.shell.apppairs;

import android.app.ActivityManager;
import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AppPair$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ AppPair f$0;
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$1;

    public /* synthetic */ AppPair$$ExternalSyntheticLambda0(AppPair appPair, ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.f$0 = appPair;
        this.f$1 = runningTaskInfo;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$onTaskInfoChanged$3(this.f$1, transaction);
    }
}
