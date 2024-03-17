package com.android.wm.shell.legacysplitscreen;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LegacySplitScreenController$$ExternalSyntheticLambda3 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ LegacySplitScreenController f$0;

    public /* synthetic */ LegacySplitScreenController$$ExternalSyntheticLambda3(LegacySplitScreenController legacySplitScreenController) {
        this.f$0 = legacySplitScreenController;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$updateVisibility$2(transaction);
    }
}
