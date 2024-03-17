package com.android.wm.shell.splitscreen;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda13 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda13(StageCoordinator stageCoordinator, boolean z, boolean z2) {
        this.f$0 = stageCoordinator;
        this.f$1 = z;
        this.f$2 = z2;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$onStageVisibilityChanged$5(this.f$1, this.f$2, transaction);
    }
}
