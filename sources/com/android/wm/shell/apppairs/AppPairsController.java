package com.android.wm.shell.apppairs;

import android.app.ActivityManager;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;

public class AppPairsController {
    public static final String TAG = "AppPairsController";
    public final SparseArray<AppPair> mActiveAppPairs = new SparseArray<>();
    public final DisplayController mDisplayController;
    public final DisplayImeController mDisplayImeController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final AppPairsImpl mImpl = new AppPairsImpl();
    public final ShellExecutor mMainExecutor;
    public AppPairsPool mPairsPool;
    public final SyncTransactionQueue mSyncQueue;
    public final ShellTaskOrganizer mTaskOrganizer;

    public AppPairsController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, ShellExecutor shellExecutor, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSyncQueue = syncTransactionQueue;
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mMainExecutor = shellExecutor;
    }

    public AppPairs asAppPairs() {
        return this.mImpl;
    }

    public void onOrganizerRegistered() {
        if (this.mPairsPool == null) {
            setPairsPool(new AppPairsPool(this));
        }
    }

    @VisibleForTesting
    public void setPairsPool(AppPairsPool appPairsPool) {
        this.mPairsPool = appPairsPool;
    }

    public boolean pair(int i, int i2) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskOrganizer.getRunningTaskInfo(i);
        ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mTaskOrganizer.getRunningTaskInfo(i2);
        if (runningTaskInfo == null || runningTaskInfo2 == null) {
            return false;
        }
        return pair(runningTaskInfo, runningTaskInfo2);
    }

    public boolean pair(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        return pairInner(runningTaskInfo, runningTaskInfo2) != null;
    }

    @VisibleForTesting
    public AppPair pairInner(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        AppPair acquire = this.mPairsPool.acquire();
        if (!acquire.pair(runningTaskInfo, runningTaskInfo2)) {
            this.mPairsPool.release(acquire);
            return null;
        }
        this.mActiveAppPairs.put(acquire.getRootTaskId(), acquire);
        return acquire;
    }

    public void unpair(int i) {
        unpair(i, true);
    }

    public void unpair(int i, boolean z) {
        AppPair appPair = this.mActiveAppPairs.get(i);
        if (appPair == null) {
            int size = this.mActiveAppPairs.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                AppPair valueAt = this.mActiveAppPairs.valueAt(size);
                if (valueAt.contains(i)) {
                    appPair = valueAt;
                    break;
                }
                size--;
            }
        }
        if (appPair != null) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                long j = (long) i;
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -234284913, 1, (String) null, Long.valueOf(j), String.valueOf(appPair));
            }
            this.mActiveAppPairs.remove(appPair.getRootTaskId());
            appPair.unpair();
            if (z) {
                this.mPairsPool.release(appPair);
            }
        } else if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 950299522, 1, (String) null, Long.valueOf((long) i));
        }
    }

    public ShellTaskOrganizer getTaskOrganizer() {
        return this.mTaskOrganizer;
    }

    public SyncTransactionQueue getSyncTransactionQueue() {
        return this.mSyncQueue;
    }

    public DisplayController getDisplayController() {
        return this.mDisplayController;
    }

    public DisplayImeController getDisplayImeController() {
        return this.mDisplayImeController;
    }

    public DisplayInsetsController getDisplayInsetsController() {
        return this.mDisplayInsetsController;
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = (str + "  ") + "  ";
        printWriter.println(str + this);
        for (int size = this.mActiveAppPairs.size() + -1; size >= 0; size--) {
            this.mActiveAppPairs.valueAt(size).dump(printWriter, str2);
        }
        AppPairsPool appPairsPool = this.mPairsPool;
        if (appPairsPool != null) {
            appPairsPool.dump(printWriter, str);
        }
    }

    public String toString() {
        return TAG + "#" + this.mActiveAppPairs.size();
    }

    public class AppPairsImpl implements AppPairs {
        public AppPairsImpl() {
        }
    }
}
