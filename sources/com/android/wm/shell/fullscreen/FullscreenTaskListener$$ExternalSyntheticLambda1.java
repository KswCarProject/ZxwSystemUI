package com.android.wm.shell.fullscreen;

import android.graphics.Point;
import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.fullscreen.FullscreenTaskListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class FullscreenTaskListener$$ExternalSyntheticLambda1 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ FullscreenTaskListener.TaskData f$0;
    public final /* synthetic */ Point f$1;

    public /* synthetic */ FullscreenTaskListener$$ExternalSyntheticLambda1(FullscreenTaskListener.TaskData taskData, Point point) {
        this.f$0 = taskData;
        this.f$1 = point;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        transaction.setPosition(this.f$0.surface, (float) this.f$1.x, (float) this.f$1.y);
    }
}
