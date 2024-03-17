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

public class MainStage extends StageTaskListener {
    public boolean mIsActive = false;

    public MainStage(Context context, ShellTaskOrganizer shellTaskOrganizer, int i, StageTaskListener.StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, IconProvider iconProvider, StageTaskUnfoldController stageTaskUnfoldController) {
        super(context, shellTaskOrganizer, i, stageListenerCallbacks, syncTransactionQueue, surfaceSession, iconProvider, stageTaskUnfoldController);
    }

    public boolean isActive() {
        return this.mIsActive;
    }

    public void activate(WindowContainerTransaction windowContainerTransaction, boolean z) {
        if (!this.mIsActive) {
            WindowContainerToken windowContainerToken = this.mRootTaskInfo.token;
            if (z) {
                windowContainerTransaction.reparentTasks((WindowContainerToken) null, windowContainerToken, StageTaskListener.CONTROLLED_WINDOWING_MODES, StageTaskListener.CONTROLLED_ACTIVITY_TYPES, true, true);
            }
            this.mIsActive = true;
        }
    }

    public void deactivate(WindowContainerTransaction windowContainerTransaction, boolean z) {
        if (this.mIsActive) {
            this.mIsActive = false;
            ActivityManager.RunningTaskInfo runningTaskInfo = this.mRootTaskInfo;
            if (runningTaskInfo != null) {
                windowContainerTransaction.reparentTasks(runningTaskInfo.token, (WindowContainerToken) null, StageTaskListener.CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE, StageTaskListener.CONTROLLED_ACTIVITY_TYPES, z);
            }
        }
    }
}
