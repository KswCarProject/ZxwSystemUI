package com.android.wm.shell.compatui;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CompatUIWindowManagerAbstract$$ExternalSyntheticLambda1 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ SurfaceControl f$0;

    public /* synthetic */ CompatUIWindowManagerAbstract$$ExternalSyntheticLambda1(SurfaceControl surfaceControl) {
        this.f$0 = surfaceControl;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        transaction.remove(this.f$0);
    }
}
