package com.android.wm.shell.apppairs;

import android.view.SurfaceControl;
import com.android.wm.shell.apppairs.AppPair;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AppPair$1$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ AppPair.AnonymousClass1 f$0;
    public final /* synthetic */ SurfaceControl f$1;

    public /* synthetic */ AppPair$1$$ExternalSyntheticLambda0(AppPair.AnonymousClass1 r1, SurfaceControl surfaceControl) {
        this.f$0 = r1;
        this.f$1 = surfaceControl;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.lambda$onLeashReady$0(this.f$1, transaction);
    }
}
