package com.android.wm.shell.splitscreen;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.split.SplitLayout;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda2 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ SplitLayout f$1;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda2(StageCoordinator stageCoordinator, SplitLayout splitLayout) {
        this.f$0 = stageCoordinator;
        this.f$1 = splitLayout;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$onLayoutSizeChanged$9(this.f$1, transaction);
    }
}
