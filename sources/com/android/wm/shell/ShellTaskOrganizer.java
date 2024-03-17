package com.android.wm.shell;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.ActivityInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.window.ITaskOrganizerController;
import android.window.StartingWindowInfo;
import android.window.StartingWindowRemovalInfo;
import android.window.TaskAppearedInfo;
import android.window.TaskOrganizer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.compatui.CompatUIController;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.startingsurface.StartingWindowController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ShellTaskOrganizer extends TaskOrganizer implements CompatUIController.CompatUICallback {
    public final CompatUIController mCompatUI;
    public final ArraySet<FocusListener> mFocusListeners;
    public ActivityManager.RunningTaskInfo mLastFocusedTaskInfo;
    public final ArrayMap<IBinder, TaskListener> mLaunchCookieToListener;
    public final Object mLock;
    public final ArraySet<LocusIdListener> mLocusIdListeners;
    public final Optional<RecentTasksController> mRecentTasks;
    public StartingWindowController mStartingWindow;
    public final SparseArray<TaskListener> mTaskListeners;
    public final SparseArray<TaskAppearedInfo> mTasks;
    public final SparseArray<LocusId> mVisibleTasksWithLocusId;

    public interface FocusListener {
        void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo);
    }

    public interface LocusIdListener {
        void onVisibilityChanged(int i, LocusId locusId, boolean z);
    }

    public @interface TaskListenerType {
    }

    public interface TaskListener {
        void dump(PrintWriter printWriter, String str) {
        }

        void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        }

        void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        boolean supportCompatUI() {
            return true;
        }

        void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
            throw new IllegalStateException("This task listener doesn't support child surface attachment.");
        }

        void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
            throw new IllegalStateException("This task listener doesn't support child surface reparent.");
        }
    }

    public ShellTaskOrganizer(ShellExecutor shellExecutor, Context context, CompatUIController compatUIController, Optional<RecentTasksController> optional) {
        this((ITaskOrganizerController) null, shellExecutor, context, compatUIController, optional);
    }

    @VisibleForTesting
    public ShellTaskOrganizer(ITaskOrganizerController iTaskOrganizerController, ShellExecutor shellExecutor, Context context, CompatUIController compatUIController, Optional<RecentTasksController> optional) {
        super(iTaskOrganizerController, shellExecutor);
        this.mTaskListeners = new SparseArray<>();
        this.mTasks = new SparseArray<>();
        this.mLaunchCookieToListener = new ArrayMap<>();
        this.mVisibleTasksWithLocusId = new SparseArray<>();
        this.mLocusIdListeners = new ArraySet<>();
        this.mFocusListeners = new ArraySet<>();
        this.mLock = new Object();
        this.mCompatUI = compatUIController;
        this.mRecentTasks = optional;
        if (compatUIController != null) {
            compatUIController.setCompatUICallback(this);
        }
    }

    public List<TaskAppearedInfo> registerOrganizer() {
        List<TaskAppearedInfo> registerOrganizer;
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 580605218, 0, (String) null, (Object[]) null);
            }
            registerOrganizer = ShellTaskOrganizer.super.registerOrganizer();
            for (int i = 0; i < registerOrganizer.size(); i++) {
                TaskAppearedInfo taskAppearedInfo = registerOrganizer.get(i);
                if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1683614271, 1, (String) null, Long.valueOf((long) taskAppearedInfo.getTaskInfo().taskId), String.valueOf(taskAppearedInfo.getTaskInfo().baseIntent));
                }
                onTaskAppeared(taskAppearedInfo);
            }
        }
        return registerOrganizer;
    }

    public void unregisterOrganizer() {
        ShellTaskOrganizer.super.unregisterOrganizer();
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.clearAllWindows();
        }
    }

    public void createRootTask(int i, int i2, TaskListener taskListener) {
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            String valueOf = String.valueOf(taskListener.toString());
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1312360667, 5, (String) null, Long.valueOf((long) i), Long.valueOf((long) i2), valueOf);
        }
        Binder binder = new Binder();
        setPendingLaunchCookieListener(binder, taskListener);
        ShellTaskOrganizer.super.createRootTask(i, i2, binder);
    }

    public void initStartingWindow(StartingWindowController startingWindowController) {
        this.mStartingWindow = startingWindowController;
    }

    public void addListenerForType(TaskListener taskListener, @TaskListenerType int... iArr) {
        synchronized (this.mLock) {
            int i = 0;
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 1990759023, 0, (String) null, String.valueOf(Arrays.toString(iArr)), String.valueOf(taskListener));
            }
            int length = iArr.length;
            while (i < length) {
                int i2 = iArr[i];
                if (this.mTaskListeners.get(i2) == null) {
                    this.mTaskListeners.put(i2, taskListener);
                    i++;
                } else {
                    throw new IllegalArgumentException("Listener for listenerType=" + i2 + " already exists");
                }
            }
            for (int size = this.mTasks.size() - 1; size >= 0; size--) {
                TaskAppearedInfo valueAt = this.mTasks.valueAt(size);
                if (getTaskListener(valueAt.getTaskInfo()) == taskListener) {
                    taskListener.onTaskAppeared(valueAt.getTaskInfo(), valueAt.getLeash());
                }
            }
        }
    }

    public void removeListener(TaskListener taskListener) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1340279385, 0, (String) null, String.valueOf(taskListener));
            }
            if (this.mTaskListeners.indexOfValue(taskListener) == -1) {
                Log.w("ShellTaskOrganizer", "No registered listener found");
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (int size = this.mTasks.size() - 1; size >= 0; size--) {
                TaskAppearedInfo valueAt = this.mTasks.valueAt(size);
                if (getTaskListener(valueAt.getTaskInfo()) == taskListener) {
                    arrayList.add(valueAt);
                }
            }
            for (int size2 = this.mTaskListeners.size() - 1; size2 >= 0; size2--) {
                if (this.mTaskListeners.valueAt(size2) == taskListener) {
                    this.mTaskListeners.removeAt(size2);
                }
            }
            for (int size3 = arrayList.size() - 1; size3 >= 0; size3--) {
                TaskAppearedInfo taskAppearedInfo = (TaskAppearedInfo) arrayList.get(size3);
                updateTaskListenerIfNeeded(taskAppearedInfo.getTaskInfo(), taskAppearedInfo.getLeash(), (TaskListener) null, getTaskListener(taskAppearedInfo.getTaskInfo()));
            }
        }
    }

    public void setPendingLaunchCookieListener(IBinder iBinder, TaskListener taskListener) {
        synchronized (this.mLock) {
            this.mLaunchCookieToListener.put(iBinder, taskListener);
        }
    }

    public void addLocusIdListener(LocusIdListener locusIdListener) {
        synchronized (this.mLock) {
            this.mLocusIdListeners.add(locusIdListener);
            for (int i = 0; i < this.mVisibleTasksWithLocusId.size(); i++) {
                locusIdListener.onVisibilityChanged(this.mVisibleTasksWithLocusId.keyAt(i), this.mVisibleTasksWithLocusId.valueAt(i), true);
            }
        }
    }

    public void addFocusListener(FocusListener focusListener) {
        synchronized (this.mLock) {
            this.mFocusListeners.add(focusListener);
            ActivityManager.RunningTaskInfo runningTaskInfo = this.mLastFocusedTaskInfo;
            if (runningTaskInfo != null) {
                focusListener.onFocusTaskChanged(runningTaskInfo);
            }
        }
    }

    public void addStartingWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.addStartingWindow(startingWindowInfo, iBinder);
        }
    }

    public void removeStartingWindow(StartingWindowRemovalInfo startingWindowRemovalInfo) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.removeStartingWindow(startingWindowRemovalInfo);
        }
    }

    public void copySplashScreenView(int i) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.copySplashScreenView(i);
        }
    }

    public void onAppSplashScreenViewRemoved(int i) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.onAppSplashScreenViewRemoved(i);
        }
    }

    public void onImeDrawnOnTask(int i) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.onImeDrawnOnTask(i);
        }
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        synchronized (this.mLock) {
            onTaskAppeared(new TaskAppearedInfo(runningTaskInfo, surfaceControl));
        }
    }

    public final void onTaskAppeared(TaskAppearedInfo taskAppearedInfo) {
        int i = taskAppearedInfo.getTaskInfo().taskId;
        this.mTasks.put(i, taskAppearedInfo);
        TaskListener taskListener = getTaskListener(taskAppearedInfo.getTaskInfo(), true);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            long j = (long) i;
            String valueOf = String.valueOf(taskListener);
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1325223370, 1, (String) null, Long.valueOf(j), valueOf);
        }
        if (taskListener != null) {
            taskListener.onTaskAppeared(taskAppearedInfo.getTaskInfo(), taskAppearedInfo.getLeash());
        }
        notifyLocusVisibilityIfNeeded(taskAppearedInfo.getTaskInfo());
        notifyCompatUI(taskAppearedInfo.getTaskInfo(), taskListener);
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x009d A[LOOP:0: B:35:0x009d->B:37:0x00a5, LOOP_START, PHI: r2 
      PHI: (r2v1 int) = (r2v0 int), (r2v2 int) binds: [B:34:0x009b, B:37:0x00a5] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onTaskInfoChanged(android.app.ActivityManager.RunningTaskInfo r11) {
        /*
            r10 = this;
            java.lang.Object r0 = r10.mLock
            monitor-enter(r0)
            boolean r1 = com.android.wm.shell.protolog.ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled     // Catch:{ all -> 0x00b7 }
            r2 = 0
            r3 = 1
            if (r1 == 0) goto L_0x001d
            int r1 = r11.taskId     // Catch:{ all -> 0x00b7 }
            long r4 = (long) r1     // Catch:{ all -> 0x00b7 }
            com.android.wm.shell.protolog.ShellProtoLogGroup r1 = com.android.wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TASK_ORG     // Catch:{ all -> 0x00b7 }
            r6 = 157713005(0x966826d, float:2.7746569E-33)
            r7 = 0
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b7 }
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x00b7 }
            r8[r2] = r4     // Catch:{ all -> 0x00b7 }
            com.android.wm.shell.protolog.ShellProtoLogImpl.v(r1, r6, r3, r7, r8)     // Catch:{ all -> 0x00b7 }
        L_0x001d:
            android.util.SparseArray<android.window.TaskAppearedInfo> r1 = r10.mTasks     // Catch:{ all -> 0x00b7 }
            int r4 = r11.taskId     // Catch:{ all -> 0x00b7 }
            java.lang.Object r1 = r1.get(r4)     // Catch:{ all -> 0x00b7 }
            android.window.TaskAppearedInfo r1 = (android.window.TaskAppearedInfo) r1     // Catch:{ all -> 0x00b7 }
            android.app.ActivityManager$RunningTaskInfo r4 = r1.getTaskInfo()     // Catch:{ all -> 0x00b7 }
            com.android.wm.shell.ShellTaskOrganizer$TaskListener r4 = r10.getTaskListener(r4)     // Catch:{ all -> 0x00b7 }
            com.android.wm.shell.ShellTaskOrganizer$TaskListener r5 = r10.getTaskListener(r11)     // Catch:{ all -> 0x00b7 }
            android.util.SparseArray<android.window.TaskAppearedInfo> r6 = r10.mTasks     // Catch:{ all -> 0x00b7 }
            int r7 = r11.taskId     // Catch:{ all -> 0x00b7 }
            android.window.TaskAppearedInfo r8 = new android.window.TaskAppearedInfo     // Catch:{ all -> 0x00b7 }
            android.view.SurfaceControl r9 = r1.getLeash()     // Catch:{ all -> 0x00b7 }
            r8.<init>(r11, r9)     // Catch:{ all -> 0x00b7 }
            r6.put(r7, r8)     // Catch:{ all -> 0x00b7 }
            android.view.SurfaceControl r6 = r1.getLeash()     // Catch:{ all -> 0x00b7 }
            boolean r4 = r10.updateTaskListenerIfNeeded(r11, r6, r4, r5)     // Catch:{ all -> 0x00b7 }
            if (r4 != 0) goto L_0x0052
            if (r5 == 0) goto L_0x0052
            r5.onTaskInfoChanged(r11)     // Catch:{ all -> 0x00b7 }
        L_0x0052:
            r10.notifyLocusVisibilityIfNeeded(r11)     // Catch:{ all -> 0x00b7 }
            if (r4 != 0) goto L_0x0061
            android.app.ActivityManager$RunningTaskInfo r4 = r1.getTaskInfo()     // Catch:{ all -> 0x00b7 }
            boolean r4 = r11.equalsForCompatUi(r4)     // Catch:{ all -> 0x00b7 }
            if (r4 != 0) goto L_0x0064
        L_0x0061:
            r10.notifyCompatUI(r11, r5)     // Catch:{ all -> 0x00b7 }
        L_0x0064:
            android.app.ActivityManager$RunningTaskInfo r1 = r1.getTaskInfo()     // Catch:{ all -> 0x00b7 }
            int r1 = r1.getWindowingMode()     // Catch:{ all -> 0x00b7 }
            int r4 = r11.getWindowingMode()     // Catch:{ all -> 0x00b7 }
            if (r1 == r4) goto L_0x007c
            java.util.Optional<com.android.wm.shell.recents.RecentTasksController> r1 = r10.mRecentTasks     // Catch:{ all -> 0x00b7 }
            com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda1 r4 = new com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda1     // Catch:{ all -> 0x00b7 }
            r4.<init>(r11)     // Catch:{ all -> 0x00b7 }
            r1.ifPresent(r4)     // Catch:{ all -> 0x00b7 }
        L_0x007c:
            boolean r1 = r11.isFocused     // Catch:{ all -> 0x00b7 }
            if (r1 != 0) goto L_0x008c
            int r1 = r11.topActivityType     // Catch:{ all -> 0x00b7 }
            r4 = 2
            if (r1 != r4) goto L_0x008a
            boolean r1 = r11.isVisible     // Catch:{ all -> 0x00b7 }
            if (r1 == 0) goto L_0x008a
            goto L_0x008c
        L_0x008a:
            r1 = r2
            goto L_0x008d
        L_0x008c:
            r1 = r3
        L_0x008d:
            android.app.ActivityManager$RunningTaskInfo r4 = r10.mLastFocusedTaskInfo     // Catch:{ all -> 0x00b7 }
            if (r4 == 0) goto L_0x0097
            int r4 = r4.taskId     // Catch:{ all -> 0x00b7 }
            int r5 = r11.taskId     // Catch:{ all -> 0x00b7 }
            if (r4 == r5) goto L_0x009a
        L_0x0097:
            if (r1 == 0) goto L_0x009a
            goto L_0x009b
        L_0x009a:
            r3 = r2
        L_0x009b:
            if (r3 == 0) goto L_0x00b5
        L_0x009d:
            android.util.ArraySet<com.android.wm.shell.ShellTaskOrganizer$FocusListener> r1 = r10.mFocusListeners     // Catch:{ all -> 0x00b7 }
            int r1 = r1.size()     // Catch:{ all -> 0x00b7 }
            if (r2 >= r1) goto L_0x00b3
            android.util.ArraySet<com.android.wm.shell.ShellTaskOrganizer$FocusListener> r1 = r10.mFocusListeners     // Catch:{ all -> 0x00b7 }
            java.lang.Object r1 = r1.valueAt(r2)     // Catch:{ all -> 0x00b7 }
            com.android.wm.shell.ShellTaskOrganizer$FocusListener r1 = (com.android.wm.shell.ShellTaskOrganizer.FocusListener) r1     // Catch:{ all -> 0x00b7 }
            r1.onFocusTaskChanged(r11)     // Catch:{ all -> 0x00b7 }
            int r2 = r2 + 1
            goto L_0x009d
        L_0x00b3:
            r10.mLastFocusedTaskInfo = r11     // Catch:{ all -> 0x00b7 }
        L_0x00b5:
            monitor-exit(r0)     // Catch:{ all -> 0x00b7 }
            return
        L_0x00b7:
            r10 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00b7 }
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.ShellTaskOrganizer.onTaskInfoChanged(android.app.ActivityManager$RunningTaskInfo):void");
    }

    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 980952660, 1, (String) null, Long.valueOf((long) runningTaskInfo.taskId));
            }
            TaskListener taskListener = getTaskListener(runningTaskInfo);
            if (taskListener != null) {
                taskListener.onBackPressedOnTaskRoot(runningTaskInfo);
            }
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                long j = (long) runningTaskInfo.taskId;
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -880817403, 1, (String) null, Long.valueOf(j));
            }
            int i = runningTaskInfo.taskId;
            TaskListener taskListener = getTaskListener(this.mTasks.get(i).getTaskInfo());
            this.mTasks.remove(i);
            if (taskListener != null) {
                taskListener.onTaskVanished(runningTaskInfo);
            }
            notifyLocusVisibilityIfNeeded(runningTaskInfo);
            notifyCompatUI(runningTaskInfo, (TaskListener) null);
            this.mRecentTasks.ifPresent(new ShellTaskOrganizer$$ExternalSyntheticLambda0(runningTaskInfo));
        }
    }

    public ActivityManager.RunningTaskInfo getRunningTaskInfo(int i) {
        ActivityManager.RunningTaskInfo taskInfo;
        synchronized (this.mLock) {
            TaskAppearedInfo taskAppearedInfo = this.mTasks.get(i);
            taskInfo = taskAppearedInfo != null ? taskAppearedInfo.getTaskInfo() : null;
        }
        return taskInfo;
    }

    public final boolean updateTaskListenerIfNeeded(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, TaskListener taskListener, TaskListener taskListener2) {
        if (taskListener == taskListener2) {
            return false;
        }
        if (taskListener != null) {
            taskListener.onTaskVanished(runningTaskInfo);
        }
        if (taskListener2 == null) {
            return true;
        }
        taskListener2.onTaskAppeared(runningTaskInfo, surfaceControl);
        return true;
    }

    public final void notifyLocusVisibilityIfNeeded(TaskInfo taskInfo) {
        int i = taskInfo.taskId;
        LocusId locusId = this.mVisibleTasksWithLocusId.get(i);
        boolean equals = Objects.equals(locusId, taskInfo.mTopActivityLocusId);
        if (locusId == null) {
            LocusId locusId2 = taskInfo.mTopActivityLocusId;
            if (locusId2 != null && taskInfo.isVisible) {
                this.mVisibleTasksWithLocusId.put(i, locusId2);
                notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, true);
            }
        } else if (equals && !taskInfo.isVisible) {
            this.mVisibleTasksWithLocusId.remove(i);
            notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, false);
        } else if (equals) {
        } else {
            if (taskInfo.isVisible) {
                this.mVisibleTasksWithLocusId.put(i, taskInfo.mTopActivityLocusId);
                notifyLocusIdChange(i, locusId, false);
                notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, true);
                return;
            }
            this.mVisibleTasksWithLocusId.remove(taskInfo.taskId);
            notifyLocusIdChange(i, locusId, false);
        }
    }

    public final void notifyLocusIdChange(int i, LocusId locusId, boolean z) {
        for (int i2 = 0; i2 < this.mLocusIdListeners.size(); i2++) {
            this.mLocusIdListeners.valueAt(i2).onVisibilityChanged(i, locusId, z);
        }
    }

    public void onSizeCompatRestartButtonAppeared(int i) {
        TaskAppearedInfo taskAppearedInfo;
        synchronized (this.mLock) {
            taskAppearedInfo = this.mTasks.get(i);
        }
        if (taskAppearedInfo != null) {
            logSizeCompatRestartButtonEventReported(taskAppearedInfo, 1);
        }
    }

    public void onSizeCompatRestartButtonClicked(int i) {
        TaskAppearedInfo taskAppearedInfo;
        synchronized (this.mLock) {
            taskAppearedInfo = this.mTasks.get(i);
        }
        if (taskAppearedInfo != null) {
            logSizeCompatRestartButtonEventReported(taskAppearedInfo, 2);
            restartTaskTopActivityProcessIfVisible(taskAppearedInfo.getTaskInfo().token);
        }
    }

    public void onCameraControlStateUpdated(int i, int i2) {
        TaskAppearedInfo taskAppearedInfo;
        synchronized (this.mLock) {
            taskAppearedInfo = this.mTasks.get(i);
        }
        if (taskAppearedInfo != null) {
            updateCameraCompatControlState(taskAppearedInfo.getTaskInfo().token, i2);
        }
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        TaskListener taskListener;
        synchronized (this.mLock) {
            taskListener = this.mTasks.contains(i) ? getTaskListener(this.mTasks.get(i).getTaskInfo()) : null;
        }
        if (taskListener != null) {
            taskListener.reparentChildSurfaceToTask(i, surfaceControl, transaction);
        } else if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 965672371, 1, (String) null, Long.valueOf((long) i));
        }
    }

    public final void logSizeCompatRestartButtonEventReported(TaskAppearedInfo taskAppearedInfo, int i) {
        ActivityInfo activityInfo = taskAppearedInfo.getTaskInfo().topActivityInfo;
        if (activityInfo != null) {
            FrameworkStatsLog.write(387, activityInfo.applicationInfo.uid, i);
        }
    }

    public final void notifyCompatUI(ActivityManager.RunningTaskInfo runningTaskInfo, TaskListener taskListener) {
        if (this.mCompatUI != null) {
            if (taskListener == null || !taskListener.supportCompatUI() || !runningTaskInfo.hasCompatUI() || !runningTaskInfo.isVisible) {
                this.mCompatUI.onCompatInfoChanged(runningTaskInfo, (TaskListener) null);
            } else {
                this.mCompatUI.onCompatInfoChanged(runningTaskInfo, taskListener);
            }
        }
    }

    public final TaskListener getTaskListener(ActivityManager.RunningTaskInfo runningTaskInfo) {
        return getTaskListener(runningTaskInfo, false);
    }

    public final TaskListener getTaskListener(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z) {
        TaskListener taskListener;
        int i = runningTaskInfo.taskId;
        ArrayList arrayList = runningTaskInfo.launchCookies;
        int size = arrayList.size() - 1;
        while (size >= 0) {
            IBinder iBinder = (IBinder) arrayList.get(size);
            TaskListener taskListener2 = this.mLaunchCookieToListener.get(iBinder);
            if (taskListener2 == null) {
                size--;
            } else {
                if (z) {
                    this.mLaunchCookieToListener.remove(iBinder);
                    this.mTaskListeners.put(i, taskListener2);
                }
                return taskListener2;
            }
        }
        TaskListener taskListener3 = this.mTaskListeners.get(i);
        if (taskListener3 != null) {
            return taskListener3;
        }
        if (runningTaskInfo.hasParentTask() && (taskListener = this.mTaskListeners.get(runningTaskInfo.parentTaskId)) != null) {
            return taskListener;
        }
        return this.mTaskListeners.get(taskInfoToTaskListenerType(runningTaskInfo));
    }

    @VisibleForTesting
    @TaskListenerType
    public static int taskInfoToTaskListenerType(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int windowingMode = runningTaskInfo.getWindowingMode();
        if (windowingMode == 1) {
            return -2;
        }
        if (windowingMode == 2) {
            return -4;
        }
        if (windowingMode != 5) {
            return windowingMode != 6 ? -1 : -3;
        }
        return -5;
    }

    public static String taskListenerTypeToString(@TaskListenerType int i) {
        if (i == -5) {
            return "TASK_LISTENER_TYPE_FREEFORM";
        }
        if (i == -4) {
            return "TASK_LISTENER_TYPE_PIP";
        }
        if (i == -3) {
            return "TASK_LISTENER_TYPE_MULTI_WINDOW";
        }
        if (i == -2) {
            return "TASK_LISTENER_TYPE_FULLSCREEN";
        }
        if (i == -1) {
            return "TASK_LISTENER_TYPE_UNDEFINED";
        }
        return "taskId#" + i;
    }

    public void dump(PrintWriter printWriter, String str) {
        synchronized (this.mLock) {
            String str2 = str + "  ";
            String str3 = str2 + "  ";
            printWriter.println(str + "ShellTaskOrganizer");
            printWriter.println(str2 + this.mTaskListeners.size() + " Listeners");
            for (int size = this.mTaskListeners.size() + -1; size >= 0; size += -1) {
                int keyAt = this.mTaskListeners.keyAt(size);
                printWriter.println(str2 + "#" + size + " " + taskListenerTypeToString(keyAt));
                this.mTaskListeners.valueAt(size).dump(printWriter, str3);
            }
            printWriter.println();
            printWriter.println(str2 + this.mTasks.size() + " Tasks");
            for (int size2 = this.mTasks.size() + -1; size2 >= 0; size2 += -1) {
                printWriter.println(str2 + "#" + size2 + " task=" + this.mTasks.keyAt(size2) + " listener=" + getTaskListener(this.mTasks.valueAt(size2).getTaskInfo()));
            }
            printWriter.println();
            printWriter.println(str2 + this.mLaunchCookieToListener.size() + " Launch Cookies");
            for (int size3 = this.mLaunchCookieToListener.size() + -1; size3 >= 0; size3 += -1) {
                printWriter.println(str2 + "#" + size3 + " cookie=" + this.mLaunchCookieToListener.keyAt(size3) + " listener=" + this.mLaunchCookieToListener.valueAt(size3));
            }
        }
    }
}
