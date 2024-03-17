package com.android.wm.shell.apppairs;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.split.SplitLayout;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AppPair$$ExternalSyntheticLambda8 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ AppPair f$0;
    public final /* synthetic */ SplitLayout f$1;

    public /* synthetic */ AppPair$$ExternalSyntheticLambda8(AppPair appPair, SplitLayout splitLayout) {
        this.f$0 = appPair;
        this.f$1 = splitLayout;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$onLayoutPositionChanging$6(this.f$1, transaction);
    }
}
