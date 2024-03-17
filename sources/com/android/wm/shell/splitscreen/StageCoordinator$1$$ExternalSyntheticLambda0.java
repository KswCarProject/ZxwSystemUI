package com.android.wm.shell.splitscreen;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.splitscreen.StageCoordinator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$1$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ StageCoordinator.AnonymousClass1 f$0;

    public /* synthetic */ StageCoordinator$1$$ExternalSyntheticLambda0(StageCoordinator.AnonymousClass1 r1) {
        this.f$0 = r1;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$onLeashReady$0(transaction);
    }
}
