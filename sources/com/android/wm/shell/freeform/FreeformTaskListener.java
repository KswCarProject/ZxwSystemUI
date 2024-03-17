package com.android.wm.shell.freeform;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.Slog;
import android.util.SparseArray;
import android.view.SurfaceControl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;

public class FreeformTaskListener implements ShellTaskOrganizer.TaskListener {
    public final SyncTransactionQueue mSyncQueue;
    public final SparseArray<State> mTasks = new SparseArray<>();

    public String toString() {
        return "FreeformTaskListener";
    }

    public static class State {
        public SurfaceControl mLeash;
        public ActivityManager.RunningTaskInfo mTaskInfo;

        public State() {
        }
    }

    public FreeformTaskListener(SyncTransactionQueue syncTransactionQueue) {
        this.mSyncQueue = syncTransactionQueue;
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mTasks.get(runningTaskInfo.taskId) == null) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                long j = (long) runningTaskInfo.taskId;
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -166960725, 1, (String) null, Long.valueOf(j));
            }
            State state = new State();
            state.mTaskInfo = runningTaskInfo;
            state.mLeash = surfaceControl;
            this.mTasks.put(runningTaskInfo.taskId, state);
            this.mSyncQueue.runInSync(new FreeformTaskListener$$ExternalSyntheticLambda1(runningTaskInfo, surfaceControl, runningTaskInfo.configuration.windowConfiguration.getBounds()));
            return;
        }
        throw new RuntimeException("Task appeared more than once: #" + runningTaskInfo.taskId);
    }

    public static /* synthetic */ void lambda$onTaskAppeared$0(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, Rect rect, SurfaceControl.Transaction transaction) {
        Point point = runningTaskInfo.positionInParent;
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y).setWindowCrop(surfaceControl, rect.width(), rect.height()).show(surfaceControl);
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (this.mTasks.get(runningTaskInfo.taskId) == null) {
            Slog.e("FreeformTaskListener", "Task already vanished: #" + runningTaskInfo.taskId);
            return;
        }
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 1899149317, 1, (String) null, Long.valueOf((long) runningTaskInfo.taskId));
        }
        this.mTasks.remove(runningTaskInfo.taskId);
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        State state = this.mTasks.get(runningTaskInfo.taskId);
        if (state != null) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -272049475, 1, (String) null, Long.valueOf((long) runningTaskInfo.taskId));
            }
            state.mTaskInfo = runningTaskInfo;
            Rect bounds = runningTaskInfo.configuration.windowConfiguration.getBounds();
            this.mSyncQueue.runInSync(new FreeformTaskListener$$ExternalSyntheticLambda0(runningTaskInfo, state.mLeash, bounds));
            return;
        }
        throw new RuntimeException("Task info changed before appearing: #" + runningTaskInfo.taskId);
    }

    public static /* synthetic */ void lambda$onTaskInfoChanged$1(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, Rect rect, SurfaceControl.Transaction transaction) {
        Point point = runningTaskInfo.positionInParent;
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y).setWindowCrop(surfaceControl, rect.width(), rect.height()).show(surfaceControl);
    }

    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        builder.setParent(findTaskSurface(i));
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.reparent(surfaceControl, findTaskSurface(i));
    }

    public final SurfaceControl findTaskSurface(int i) {
        if (this.mTasks.contains(i)) {
            return this.mTasks.get(i).mLeash;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + this);
        printWriter.println((str + "  ") + this.mTasks.size() + " tasks");
    }

    public static boolean isFreeformEnabled(Context context) {
        if (context.getPackageManager().hasSystemFeature("android.software.freeform_window_management") || Settings.Global.getInt(context.getContentResolver(), "enable_freeform_support", 0) != 0) {
            return true;
        }
        return false;
    }
}
