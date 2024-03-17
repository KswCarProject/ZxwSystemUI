package com.android.wm.shell.apppairs;

import android.app.ActivityManager;
import android.graphics.Point;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.SurfaceUtils;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.split.SplitLayout;
import com.android.wm.shell.common.split.SplitWindowManager;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;

public class AppPair implements ShellTaskOrganizer.TaskListener, SplitLayout.SplitLayoutHandler {
    public static final String TAG = "AppPair";
    public final AppPairsController mController;
    public SurfaceControl mDimLayer1;
    public SurfaceControl mDimLayer2;
    public final DisplayController mDisplayController;
    public final DisplayImeController mDisplayImeController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final SplitWindowManager.ParentContainerCallbacks mParentContainerCallbacks = new SplitWindowManager.ParentContainerCallbacks() {
        public void attachToParentSurface(SurfaceControl.Builder builder) {
            builder.setParent(AppPair.this.mRootTaskLeash);
        }

        public void onLeashReady(SurfaceControl surfaceControl) {
            AppPair.this.mSyncQueue.runInSync(new AppPair$1$$ExternalSyntheticLambda0(this, surfaceControl));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onLeashReady$0(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
            transaction.show(surfaceControl).setLayer(surfaceControl, Integer.MAX_VALUE).setPosition(surfaceControl, (float) AppPair.this.mSplitLayout.getDividerBounds().left, (float) AppPair.this.mSplitLayout.getDividerBounds().top);
        }
    };
    public ActivityManager.RunningTaskInfo mRootTaskInfo;
    public SurfaceControl mRootTaskLeash;
    public SplitLayout mSplitLayout;
    public final SurfaceSession mSurfaceSession = new SurfaceSession();
    public final SyncTransactionQueue mSyncQueue;
    public ActivityManager.RunningTaskInfo mTaskInfo1;
    public ActivityManager.RunningTaskInfo mTaskInfo2;
    public SurfaceControl mTaskLeash1;
    public SurfaceControl mTaskLeash2;

    public AppPair(AppPairsController appPairsController) {
        this.mController = appPairsController;
        this.mSyncQueue = appPairsController.getSyncTransactionQueue();
        this.mDisplayController = appPairsController.getDisplayController();
        this.mDisplayImeController = appPairsController.getDisplayImeController();
        this.mDisplayInsetsController = appPairsController.getDisplayInsetsController();
    }

    public int getRootTaskId() {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mRootTaskInfo;
        if (runningTaskInfo != null) {
            return runningTaskInfo.taskId;
        }
        return -1;
    }

    public final int getTaskId1() {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo1;
        if (runningTaskInfo != null) {
            return runningTaskInfo.taskId;
        }
        return -1;
    }

    public final int getTaskId2() {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo2;
        if (runningTaskInfo != null) {
            return runningTaskInfo.taskId;
        }
        return -1;
    }

    public boolean contains(int i) {
        return i == getRootTaskId() || i == getTaskId1() || i == getTaskId2();
    }

    public boolean pair(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        ActivityManager.RunningTaskInfo runningTaskInfo3 = runningTaskInfo;
        ActivityManager.RunningTaskInfo runningTaskInfo4 = runningTaskInfo2;
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            long j = (long) runningTaskInfo3.taskId;
            long j2 = (long) runningTaskInfo4.taskId;
            String valueOf = String.valueOf(this);
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -742394458, 5, (String) null, Long.valueOf(j), Long.valueOf(j2), valueOf);
        }
        boolean z = runningTaskInfo3.supportsMultiWindow;
        if (!z || !runningTaskInfo4.supportsMultiWindow) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                boolean z2 = runningTaskInfo4.supportsMultiWindow;
                ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -553798917, 15, (String) null, Boolean.valueOf(z), Boolean.valueOf(z2));
            }
            return false;
        }
        this.mTaskInfo1 = runningTaskInfo3;
        this.mTaskInfo2 = runningTaskInfo4;
        SplitLayout splitLayout = new SplitLayout(TAG + "SplitDivider", this.mDisplayController.getDisplayContext(this.mRootTaskInfo.displayId), this.mRootTaskInfo.configuration, this, this.mParentContainerCallbacks, this.mDisplayImeController, this.mController.getTaskOrganizer(), 1);
        this.mSplitLayout = splitLayout;
        this.mDisplayInsetsController.addInsetsChangedListener(this.mRootTaskInfo.displayId, splitLayout);
        WindowContainerToken windowContainerToken = runningTaskInfo3.token;
        WindowContainerToken windowContainerToken2 = runningTaskInfo4.token;
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setHidden(this.mRootTaskInfo.token, false).reparent(windowContainerToken, this.mRootTaskInfo.token, true).reparent(windowContainerToken2, this.mRootTaskInfo.token, true).setWindowingMode(windowContainerToken, 6).setWindowingMode(windowContainerToken2, 6).setBounds(windowContainerToken, this.mSplitLayout.getBounds1()).setBounds(windowContainerToken2, this.mSplitLayout.getBounds2()).reorder(this.mRootTaskInfo.token, true);
        this.mController.getTaskOrganizer().applyTransaction(windowContainerTransaction);
        return true;
    }

    public void unpair() {
        unpair((WindowContainerToken) null);
    }

    public final void unpair(WindowContainerToken windowContainerToken) {
        WindowContainerToken windowContainerToken2 = this.mTaskInfo1.token;
        WindowContainerToken windowContainerToken3 = this.mTaskInfo2.token;
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        boolean z = true;
        WindowContainerTransaction reparent = windowContainerTransaction.setHidden(this.mRootTaskInfo.token, true).reorder(this.mRootTaskInfo.token, false).reparent(windowContainerToken2, (WindowContainerToken) null, windowContainerToken2 == windowContainerToken);
        if (windowContainerToken3 != windowContainerToken) {
            z = false;
        }
        reparent.reparent(windowContainerToken3, (WindowContainerToken) null, z).setWindowingMode(windowContainerToken2, 0).setWindowingMode(windowContainerToken3, 0);
        this.mController.getTaskOrganizer().applyTransaction(windowContainerTransaction);
        this.mTaskInfo1 = null;
        this.mTaskInfo2 = null;
        this.mSplitLayout.release();
        this.mSplitLayout = null;
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        int i;
        ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mRootTaskInfo;
        if (runningTaskInfo2 == null || (i = runningTaskInfo.taskId) == runningTaskInfo2.taskId) {
            this.mRootTaskInfo = runningTaskInfo;
            this.mRootTaskLeash = surfaceControl;
        } else if (i == getTaskId1()) {
            this.mTaskInfo1 = runningTaskInfo;
            this.mTaskLeash1 = surfaceControl;
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda1(this));
        } else if (runningTaskInfo.taskId == getTaskId2()) {
            this.mTaskInfo2 = runningTaskInfo;
            this.mTaskLeash2 = surfaceControl;
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda2(this));
        } else {
            throw new IllegalStateException("Unknown task=" + runningTaskInfo.taskId);
        }
        if (this.mTaskLeash1 != null && this.mTaskLeash2 != null) {
            this.mSplitLayout.init();
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda3(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$0(SurfaceControl.Transaction transaction) {
        this.mDimLayer1 = SurfaceUtils.makeDimLayer(transaction, this.mTaskLeash1, "Dim layer", this.mSurfaceSession);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$1(SurfaceControl.Transaction transaction) {
        this.mDimLayer2 = SurfaceUtils.makeDimLayer(transaction, this.mTaskLeash2, "Dim layer", this.mSurfaceSession);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$2(SurfaceControl.Transaction transaction) {
        SurfaceControl.Transaction show = transaction.show(this.mRootTaskLeash).show(this.mTaskLeash1).show(this.mTaskLeash2);
        SurfaceControl surfaceControl = this.mTaskLeash1;
        Point point = this.mTaskInfo1.positionInParent;
        SurfaceControl.Transaction position = show.setPosition(surfaceControl, (float) point.x, (float) point.y);
        SurfaceControl surfaceControl2 = this.mTaskLeash2;
        Point point2 = this.mTaskInfo2.positionInParent;
        position.setPosition(surfaceControl2, (float) point2.x, (float) point2.y);
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!runningTaskInfo.supportsMultiWindow) {
            this.mController.unpair(this.mRootTaskInfo.taskId);
        } else if (runningTaskInfo.taskId == getRootTaskId()) {
            if (this.mRootTaskInfo.isVisible != runningTaskInfo.isVisible) {
                this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda0(this, runningTaskInfo));
            }
            this.mRootTaskInfo = runningTaskInfo;
            SplitLayout splitLayout = this.mSplitLayout;
            if (splitLayout != null && splitLayout.updateConfiguration(runningTaskInfo.configuration)) {
                onLayoutSizeChanged(this.mSplitLayout);
            }
        } else if (runningTaskInfo.taskId == getTaskId1()) {
            this.mTaskInfo1 = runningTaskInfo;
        } else if (runningTaskInfo.taskId == getTaskId2()) {
            this.mTaskInfo2 = runningTaskInfo;
        } else {
            throw new IllegalStateException("Unknown task=" + runningTaskInfo.taskId);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskInfoChanged$3(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl.Transaction transaction) {
        if (runningTaskInfo.isVisible) {
            transaction.show(this.mRootTaskLeash);
        } else {
            transaction.hide(this.mRootTaskLeash);
        }
    }

    public int getSplitItemPosition(WindowContainerToken windowContainerToken) {
        if (windowContainerToken == null) {
            return -1;
        }
        if (windowContainerToken.equals(this.mTaskInfo1.getToken())) {
            return 0;
        }
        if (windowContainerToken.equals(this.mTaskInfo2.getToken())) {
            return 1;
        }
        return -1;
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (runningTaskInfo.taskId == getRootTaskId()) {
            this.mController.unpair(this.mRootTaskInfo.taskId, false);
        } else if (runningTaskInfo.taskId == getTaskId1()) {
            this.mController.unpair(this.mRootTaskInfo.taskId);
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda4(this));
        } else if (runningTaskInfo.taskId == getTaskId2()) {
            this.mController.unpair(this.mRootTaskInfo.taskId);
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda5(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskVanished$4(SurfaceControl.Transaction transaction) {
        transaction.remove(this.mDimLayer1);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskVanished$5(SurfaceControl.Transaction transaction) {
        transaction.remove(this.mDimLayer2);
    }

    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        builder.setParent(findTaskSurface(i));
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.reparent(surfaceControl, findTaskSurface(i));
    }

    public final SurfaceControl findTaskSurface(int i) {
        if (getRootTaskId() == i) {
            return this.mRootTaskLeash;
        }
        if (getTaskId1() == i) {
            return this.mTaskLeash1;
        }
        if (getTaskId2() == i) {
            return this.mTaskLeash2;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + this);
        if (this.mRootTaskInfo != null) {
            printWriter.println(str2 + "Root taskId=" + this.mRootTaskInfo.taskId + " winMode=" + this.mRootTaskInfo.getWindowingMode());
        }
        if (this.mTaskInfo1 != null) {
            printWriter.println(str2 + "1 taskId=" + this.mTaskInfo1.taskId + " winMode=" + this.mTaskInfo1.getWindowingMode());
        }
        if (this.mTaskInfo2 != null) {
            printWriter.println(str2 + "2 taskId=" + this.mTaskInfo2.taskId + " winMode=" + this.mTaskInfo2.getWindowingMode());
        }
    }

    public String toString() {
        return TAG + "#" + getRootTaskId();
    }

    public void onSnappedToDismiss(boolean z) {
        unpair((z ? this.mTaskInfo1 : this.mTaskInfo2).token);
    }

    public void onLayoutPositionChanging(SplitLayout splitLayout) {
        this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda8(this, splitLayout));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onLayoutPositionChanging$6(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        splitLayout.applySurfaceChanges(transaction, this.mTaskLeash1, this.mTaskLeash2, this.mDimLayer1, this.mDimLayer2, true);
    }

    public void onLayoutSizeChanging(SplitLayout splitLayout) {
        this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda6(this, splitLayout));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onLayoutSizeChanging$7(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        splitLayout.applySurfaceChanges(transaction, this.mTaskLeash1, this.mTaskLeash2, this.mDimLayer1, this.mDimLayer2, true);
    }

    public void onLayoutSizeChanged(SplitLayout splitLayout) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        splitLayout.applyTaskChanges(windowContainerTransaction, this.mTaskInfo1, this.mTaskInfo2);
        this.mSyncQueue.queue(windowContainerTransaction);
        this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda7(this, splitLayout));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onLayoutSizeChanged$8(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        splitLayout.applySurfaceChanges(transaction, this.mTaskLeash1, this.mTaskLeash2, this.mDimLayer1, this.mDimLayer2, false);
    }

    public void setLayoutOffsetTarget(int i, int i2, SplitLayout splitLayout) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        splitLayout.applyLayoutOffsetTarget(windowContainerTransaction, i, i2, this.mTaskInfo1, this.mTaskInfo2);
        this.mController.getTaskOrganizer().applyTransaction(windowContainerTransaction);
    }
}
