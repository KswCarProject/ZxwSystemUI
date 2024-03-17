package com.android.wm.shell.splitscreen;

import android.app.ActivityManager;
import android.content.Context;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.splitscreen.StageTaskListener;

public class SideStage extends StageTaskListener {
    public SideStage(Context context, ShellTaskOrganizer shellTaskOrganizer, int i, StageTaskListener.StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, IconProvider iconProvider, StageTaskUnfoldController stageTaskUnfoldController) {
        super(context, shellTaskOrganizer, i, stageListenerCallbacks, syncTransactionQueue, surfaceSession, iconProvider, stageTaskUnfoldController);
    }

    public boolean removeAllTasks(WindowContainerTransaction windowContainerTransaction, boolean z) {
        if (this.mChildrenTaskInfo.size() == 0) {
            return false;
        }
        windowContainerTransaction.reparentTasks(this.mRootTaskInfo.token, (WindowContainerToken) null, StageTaskListener.CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE, StageTaskListener.CONTROLLED_ACTIVITY_TYPES, z);
        return true;
    }

    public boolean removeTask(int i, WindowContainerToken windowContainerToken, WindowContainerTransaction windowContainerTransaction) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mChildrenTaskInfo.get(i);
        if (runningTaskInfo == null) {
            return false;
        }
        windowContainerTransaction.reparent(runningTaskInfo.token, windowContainerToken, false);
        return true;
    }
}
