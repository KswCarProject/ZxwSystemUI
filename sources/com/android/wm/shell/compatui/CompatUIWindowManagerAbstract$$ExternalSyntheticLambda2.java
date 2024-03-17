package com.android.wm.shell.compatui;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CompatUIWindowManagerAbstract$$ExternalSyntheticLambda2 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ CompatUIWindowManagerAbstract f$0;
    public final /* synthetic */ SurfaceControl f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ CompatUIWindowManagerAbstract$$ExternalSyntheticLambda2(CompatUIWindowManagerAbstract compatUIWindowManagerAbstract, SurfaceControl surfaceControl, int i) {
        this.f$0 = compatUIWindowManagerAbstract;
        this.f$1 = surfaceControl;
        this.f$2 = i;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$initSurface$0(this.f$1, this.f$2, transaction);
    }
}
