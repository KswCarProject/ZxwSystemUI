package com.android.wm.shell.compatui;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CompatUIWindowManagerAbstract$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ CompatUIWindowManagerAbstract f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ CompatUIWindowManagerAbstract$$ExternalSyntheticLambda0(CompatUIWindowManagerAbstract compatUIWindowManagerAbstract, int i, int i2) {
        this.f$0 = compatUIWindowManagerAbstract;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$updateSurfacePosition$2(this.f$1, this.f$2, transaction);
    }
}
