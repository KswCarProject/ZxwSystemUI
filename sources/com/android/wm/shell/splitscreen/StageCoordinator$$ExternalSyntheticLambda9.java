package com.android.wm.shell.splitscreen;

import android.app.ActivityManager;
import com.android.wm.shell.recents.RecentTasksController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda9 implements Consumer {
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$0;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda9(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.f$0 = runningTaskInfo;
    }

    public final void accept(Object obj) {
        ((RecentTasksController) obj).removeSplitPair(this.f$0.taskId);
    }
}
