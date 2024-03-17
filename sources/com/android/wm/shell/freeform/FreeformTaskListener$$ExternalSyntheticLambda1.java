package com.android.wm.shell.freeform;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class FreeformTaskListener$$ExternalSyntheticLambda1 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$0;
    public final /* synthetic */ SurfaceControl f$1;
    public final /* synthetic */ Rect f$2;

    public /* synthetic */ FreeformTaskListener$$ExternalSyntheticLambda1(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, Rect rect) {
        this.f$0 = runningTaskInfo;
        this.f$1 = surfaceControl;
        this.f$2 = rect;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        FreeformTaskListener.lambda$onTaskAppeared$0(this.f$0, this.f$1, this.f$2, transaction);
    }
}
