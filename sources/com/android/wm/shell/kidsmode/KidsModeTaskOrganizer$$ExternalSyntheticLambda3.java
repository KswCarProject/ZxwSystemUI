package com.android.wm.shell.kidsmode;

import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KidsModeTaskOrganizer$$ExternalSyntheticLambda3 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ SurfaceControl f$0;
    public final /* synthetic */ Rect f$1;

    public /* synthetic */ KidsModeTaskOrganizer$$ExternalSyntheticLambda3(SurfaceControl surfaceControl, Rect rect) {
        this.f$0 = surfaceControl;
        this.f$1 = rect;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        KidsModeTaskOrganizer.lambda$updateBounds$3(this.f$0, this.f$1, transaction);
    }
}
