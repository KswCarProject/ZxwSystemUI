package com.android.systemui.shared.system;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.TaskStackListener;
import android.content.ComponentName;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Trace;
import android.util.Log;
import android.window.TaskSnapshot;
import com.android.internal.os.SomeArgs;
import com.android.systemui.shared.recents.model.ThumbnailData;
import java.util.ArrayList;
import java.util.List;

public class TaskStackChangeListeners {
    public static final TaskStackChangeListeners INSTANCE = new TaskStackChangeListeners();
    public static final String TAG = "TaskStackChangeListeners";
    public final Impl mImpl = new Impl(Looper.getMainLooper());

    public static TaskStackChangeListeners getInstance() {
        return INSTANCE;
    }

    public void registerTaskStackListener(TaskStackChangeListener taskStackChangeListener) {
        synchronized (this.mImpl) {
            this.mImpl.addListener(taskStackChangeListener);
        }
    }

    public void unregisterTaskStackListener(TaskStackChangeListener taskStackChangeListener) {
        synchronized (this.mImpl) {
            this.mImpl.removeListener(taskStackChangeListener);
        }
    }

    public static class Impl extends TaskStackListener implements Handler.Callback {
        public final Handler mHandler;
        public boolean mRegistered;
        public final List<TaskStackChangeListener> mTaskStackListeners = new ArrayList();
        public final List<TaskStackChangeListener> mTmpListeners = new ArrayList();

        public Impl(Looper looper) {
            this.mHandler = new Handler(looper, this);
        }

        public void addListener(TaskStackChangeListener taskStackChangeListener) {
            synchronized (this.mTaskStackListeners) {
                this.mTaskStackListeners.add(taskStackChangeListener);
            }
            if (!this.mRegistered) {
                try {
                    ActivityTaskManager.getService().registerTaskStackListener(this);
                    this.mRegistered = true;
                } catch (Exception e) {
                    Log.w(TaskStackChangeListeners.TAG, "Failed to call registerTaskStackListener", e);
                }
            }
        }

        public void removeListener(TaskStackChangeListener taskStackChangeListener) {
            boolean isEmpty;
            synchronized (this.mTaskStackListeners) {
                this.mTaskStackListeners.remove(taskStackChangeListener);
                isEmpty = this.mTaskStackListeners.isEmpty();
            }
            if (isEmpty && this.mRegistered) {
                try {
                    ActivityTaskManager.getService().unregisterTaskStackListener(this);
                    this.mRegistered = false;
                } catch (Exception e) {
                    Log.w(TaskStackChangeListeners.TAG, "Failed to call unregisterTaskStackListener", e);
                }
            }
        }

        public void onTaskStackChanged() {
            synchronized (this.mTaskStackListeners) {
                this.mTmpListeners.addAll(this.mTaskStackListeners);
            }
            for (int size = this.mTmpListeners.size() - 1; size >= 0; size--) {
                this.mTmpListeners.get(size).onTaskStackChangedBackground();
            }
            this.mTmpListeners.clear();
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessage(1);
        }

        public void onActivityPinned(String str, int i, int i2, int i3) {
            this.mHandler.removeMessages(3);
            this.mHandler.obtainMessage(3, new PinnedActivityInfo(str, i, i2, i3)).sendToTarget();
        }

        public void onActivityUnpinned() {
            this.mHandler.removeMessages(10);
            this.mHandler.sendEmptyMessage(10);
        }

        public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = runningTaskInfo;
            obtain.argi1 = z ? 1 : 0;
            obtain.argi2 = z2 ? 1 : 0;
            obtain.argi3 = z3 ? 1 : 0;
            this.mHandler.removeMessages(4);
            this.mHandler.obtainMessage(4, obtain).sendToTarget();
        }

        public void onActivityForcedResizable(String str, int i, int i2) {
            this.mHandler.obtainMessage(6, i, i2, str).sendToTarget();
        }

        public void onActivityDismissingDockedTask() {
            this.mHandler.sendEmptyMessage(7);
        }

        public void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
            this.mHandler.obtainMessage(11, i, 0, runningTaskInfo).sendToTarget();
        }

        public void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
            this.mHandler.obtainMessage(16, i, 0, runningTaskInfo).sendToTarget();
        }

        public void onTaskProfileLocked(ActivityManager.RunningTaskInfo runningTaskInfo) {
            this.mHandler.obtainMessage(8, runningTaskInfo).sendToTarget();
        }

        public void onTaskSnapshotChanged(int i, TaskSnapshot taskSnapshot) {
            this.mHandler.obtainMessage(2, i, 0, taskSnapshot).sendToTarget();
        }

        public void onTaskCreated(int i, ComponentName componentName) {
            this.mHandler.obtainMessage(12, i, 0, componentName).sendToTarget();
        }

        public void onTaskRemoved(int i) {
            this.mHandler.obtainMessage(13, i, 0).sendToTarget();
        }

        public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
            this.mHandler.obtainMessage(14, runningTaskInfo).sendToTarget();
        }

        public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
            this.mHandler.obtainMessage(17, runningTaskInfo).sendToTarget();
        }

        public void onActivityRequestedOrientationChanged(int i, int i2) {
            this.mHandler.obtainMessage(15, i, i2).sendToTarget();
        }

        public void onTaskDisplayChanged(int i, int i2) {
            this.mHandler.obtainMessage(18, i, i2).sendToTarget();
        }

        public void onRecentTaskListUpdated() {
            this.mHandler.obtainMessage(19).sendToTarget();
        }

        public void onRecentTaskListFrozenChanged(boolean z) {
            this.mHandler.obtainMessage(20, z ? 1 : 0, 0).sendToTarget();
        }

        public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
            this.mHandler.obtainMessage(21, runningTaskInfo).sendToTarget();
        }

        public void onActivityRotation(int i) {
            this.mHandler.obtainMessage(22, i, 0).sendToTarget();
        }

        public void onLockTaskModeChanged(int i) {
            this.mHandler.obtainMessage(23, i, 0).sendToTarget();
        }

        public boolean handleMessage(Message message) {
            synchronized (this.mTaskStackListeners) {
                boolean z = false;
                switch (message.what) {
                    case 1:
                        Trace.beginSection("onTaskStackChanged");
                        for (int size = this.mTaskStackListeners.size() - 1; size >= 0; size--) {
                            this.mTaskStackListeners.get(size).onTaskStackChanged();
                        }
                        Trace.endSection();
                        break;
                    case 2:
                        Trace.beginSection("onTaskSnapshotChanged");
                        TaskSnapshot taskSnapshot = (TaskSnapshot) message.obj;
                        ThumbnailData thumbnailData = new ThumbnailData(taskSnapshot);
                        for (int size2 = this.mTaskStackListeners.size() - 1; size2 >= 0; size2--) {
                            z |= this.mTaskStackListeners.get(size2).onTaskSnapshotChanged(message.arg1, thumbnailData);
                        }
                        if (!z) {
                            thumbnailData.recycleBitmap();
                            if (taskSnapshot.getHardwareBuffer() != null) {
                                taskSnapshot.getHardwareBuffer().close();
                            }
                        }
                        Trace.endSection();
                        break;
                    case 3:
                        PinnedActivityInfo pinnedActivityInfo = (PinnedActivityInfo) message.obj;
                        for (int size3 = this.mTaskStackListeners.size() - 1; size3 >= 0; size3--) {
                            this.mTaskStackListeners.get(size3).onActivityPinned(pinnedActivityInfo.mPackageName, pinnedActivityInfo.mUserId, pinnedActivityInfo.mTaskId, pinnedActivityInfo.mStackId);
                        }
                        break;
                    case 4:
                        SomeArgs someArgs = (SomeArgs) message.obj;
                        ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) someArgs.arg1;
                        boolean z2 = someArgs.argi1 != 0;
                        boolean z3 = someArgs.argi2 != 0;
                        if (someArgs.argi3 != 0) {
                            z = true;
                        }
                        for (int size4 = this.mTaskStackListeners.size() - 1; size4 >= 0; size4--) {
                            this.mTaskStackListeners.get(size4).onActivityRestartAttempt(runningTaskInfo, z2, z3, z);
                        }
                        break;
                    case 6:
                        for (int size5 = this.mTaskStackListeners.size() - 1; size5 >= 0; size5--) {
                            this.mTaskStackListeners.get(size5).onActivityForcedResizable((String) message.obj, message.arg1, message.arg2);
                        }
                        break;
                    case 7:
                        for (int size6 = this.mTaskStackListeners.size() - 1; size6 >= 0; size6--) {
                            this.mTaskStackListeners.get(size6).onActivityDismissingDockedStack();
                        }
                        break;
                    case 8:
                        ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) message.obj;
                        for (int size7 = this.mTaskStackListeners.size() - 1; size7 >= 0; size7--) {
                            this.mTaskStackListeners.get(size7).onTaskProfileLocked(runningTaskInfo2);
                        }
                        break;
                    case 10:
                        for (int size8 = this.mTaskStackListeners.size() - 1; size8 >= 0; size8--) {
                            this.mTaskStackListeners.get(size8).onActivityUnpinned();
                        }
                        break;
                    case 11:
                        ActivityManager.RunningTaskInfo runningTaskInfo3 = (ActivityManager.RunningTaskInfo) message.obj;
                        for (int size9 = this.mTaskStackListeners.size() - 1; size9 >= 0; size9--) {
                            this.mTaskStackListeners.get(size9).onActivityLaunchOnSecondaryDisplayFailed(runningTaskInfo3);
                        }
                        break;
                    case 12:
                        for (int size10 = this.mTaskStackListeners.size() - 1; size10 >= 0; size10--) {
                            this.mTaskStackListeners.get(size10).onTaskCreated(message.arg1, (ComponentName) message.obj);
                        }
                        break;
                    case 13:
                        for (int size11 = this.mTaskStackListeners.size() - 1; size11 >= 0; size11--) {
                            this.mTaskStackListeners.get(size11).onTaskRemoved(message.arg1);
                        }
                        break;
                    case 14:
                        ActivityManager.RunningTaskInfo runningTaskInfo4 = (ActivityManager.RunningTaskInfo) message.obj;
                        for (int size12 = this.mTaskStackListeners.size() - 1; size12 >= 0; size12--) {
                            this.mTaskStackListeners.get(size12).onTaskMovedToFront(runningTaskInfo4);
                        }
                        break;
                    case 15:
                        for (int size13 = this.mTaskStackListeners.size() - 1; size13 >= 0; size13--) {
                            this.mTaskStackListeners.get(size13).onActivityRequestedOrientationChanged(message.arg1, message.arg2);
                        }
                        break;
                    case 16:
                        ActivityManager.RunningTaskInfo runningTaskInfo5 = (ActivityManager.RunningTaskInfo) message.obj;
                        for (int size14 = this.mTaskStackListeners.size() - 1; size14 >= 0; size14--) {
                            this.mTaskStackListeners.get(size14).onActivityLaunchOnSecondaryDisplayRerouted(runningTaskInfo5);
                        }
                        break;
                    case 17:
                        for (int size15 = this.mTaskStackListeners.size() - 1; size15 >= 0; size15--) {
                            this.mTaskStackListeners.get(size15).onBackPressedOnTaskRoot((ActivityManager.RunningTaskInfo) message.obj);
                        }
                        break;
                    case 18:
                        for (int size16 = this.mTaskStackListeners.size() - 1; size16 >= 0; size16--) {
                            this.mTaskStackListeners.get(size16).onTaskDisplayChanged(message.arg1, message.arg2);
                        }
                        break;
                    case 19:
                        for (int size17 = this.mTaskStackListeners.size() - 1; size17 >= 0; size17--) {
                            this.mTaskStackListeners.get(size17).onRecentTaskListUpdated();
                        }
                        break;
                    case 20:
                        for (int size18 = this.mTaskStackListeners.size() - 1; size18 >= 0; size18--) {
                            this.mTaskStackListeners.get(size18).onRecentTaskListFrozenChanged(message.arg1 != 0);
                        }
                        break;
                    case 21:
                        ActivityManager.RunningTaskInfo runningTaskInfo6 = (ActivityManager.RunningTaskInfo) message.obj;
                        for (int size19 = this.mTaskStackListeners.size() - 1; size19 >= 0; size19--) {
                            this.mTaskStackListeners.get(size19).onTaskDescriptionChanged(runningTaskInfo6);
                        }
                        break;
                    case 22:
                        for (int size20 = this.mTaskStackListeners.size() - 1; size20 >= 0; size20--) {
                            this.mTaskStackListeners.get(size20).onActivityRotation(message.arg1);
                        }
                        break;
                    case 23:
                        for (int size21 = this.mTaskStackListeners.size() - 1; size21 >= 0; size21--) {
                            this.mTaskStackListeners.get(size21).onLockTaskModeChanged(message.arg1);
                        }
                        break;
                }
            }
            Object obj = message.obj;
            if (obj instanceof SomeArgs) {
                ((SomeArgs) obj).recycle();
            }
            return true;
        }
    }

    public static class PinnedActivityInfo {
        public final String mPackageName;
        public final int mStackId;
        public final int mTaskId;
        public final int mUserId;

        public PinnedActivityInfo(String str, int i, int i2, int i3) {
            this.mPackageName = str;
            this.mUserId = i;
            this.mTaskId = i2;
            this.mStackId = i3;
        }
    }
}
