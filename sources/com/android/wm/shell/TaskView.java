package com.android.wm.shell;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.CloseGuard;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.concurrent.Executor;

public class TaskView extends SurfaceView implements SurfaceHolder.Callback, ShellTaskOrganizer.TaskListener, ViewTreeObserver.OnComputeInternalInsetsListener {
    public final CloseGuard mGuard;
    public boolean mIsInitialized;
    public Listener mListener;
    public Executor mListenerExecutor;
    public Region mObscuredTouchRegion;
    public final Executor mShellExecutor;
    public boolean mSurfaceCreated;
    public final SyncTransactionQueue mSyncQueue;
    public ActivityManager.RunningTaskInfo mTaskInfo;
    public SurfaceControl mTaskLeash;
    public final ShellTaskOrganizer mTaskOrganizer;
    public WindowContainerToken mTaskToken;
    public final TaskViewTransitions mTaskViewTransitions;
    public final int[] mTmpLocation = new int[2];
    public final Rect mTmpRect = new Rect();
    public final Rect mTmpRootRect = new Rect();
    public final SurfaceControl.Transaction mTransaction = new SurfaceControl.Transaction();

    public interface Listener {
        void onBackPressedOnTaskRoot(int i) {
        }

        void onInitialized() {
        }

        void onReleased() {
        }

        void onTaskCreated(int i, ComponentName componentName) {
        }

        void onTaskRemovalStarted(int i) {
        }

        void onTaskVisibilityChanged(int i, boolean z) {
        }
    }

    public TaskView(Context context, ShellTaskOrganizer shellTaskOrganizer, TaskViewTransitions taskViewTransitions, SyncTransactionQueue syncTransactionQueue) {
        super(context, (AttributeSet) null, 0, 0, true);
        CloseGuard closeGuard = new CloseGuard();
        this.mGuard = closeGuard;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mShellExecutor = shellTaskOrganizer.getExecutor();
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskViewTransitions = taskViewTransitions;
        if (taskViewTransitions != null) {
            taskViewTransitions.addTaskView(this);
        }
        setUseAlpha();
        getHolder().addCallback(this);
        closeGuard.open("release");
    }

    public final boolean isUsingShellTransitions() {
        return this.mTaskViewTransitions != null && Transitions.ENABLE_SHELL_TRANSITIONS;
    }

    public void setListener(Executor executor, Listener listener) {
        if (this.mListener == null) {
            this.mListener = listener;
            this.mListenerExecutor = executor;
            return;
        }
        throw new IllegalStateException("Trying to set a listener when one has already been set");
    }

    public void startShortcutActivity(ShortcutInfo shortcutInfo, ActivityOptions activityOptions, Rect rect) {
        prepareActivityOptions(activityOptions, rect);
        LauncherApps launcherApps = (LauncherApps) this.mContext.getSystemService(LauncherApps.class);
        if (isUsingShellTransitions()) {
            this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda12(this, shortcutInfo, activityOptions));
            return;
        }
        try {
            launcherApps.startShortcut(shortcutInfo, (Rect) null, activityOptions.toBundle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startShortcutActivity$0(ShortcutInfo shortcutInfo, ActivityOptions activityOptions) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.startShortcut(this.mContext.getPackageName(), shortcutInfo, activityOptions.toBundle());
        this.mTaskViewTransitions.startTaskView(windowContainerTransaction, this);
    }

    public void startActivity(PendingIntent pendingIntent, Intent intent, ActivityOptions activityOptions, Rect rect) {
        prepareActivityOptions(activityOptions, rect);
        if (isUsingShellTransitions()) {
            this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda5(this, pendingIntent, intent, activityOptions));
            return;
        }
        try {
            pendingIntent.send(this.mContext, 0, intent, (PendingIntent.OnFinished) null, (Handler) null, (String) null, activityOptions.toBundle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startActivity$1(PendingIntent pendingIntent, Intent intent, ActivityOptions activityOptions) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.sendPendingIntent(pendingIntent, intent, activityOptions.toBundle());
        this.mTaskViewTransitions.startTaskView(windowContainerTransaction, this);
    }

    public final void prepareActivityOptions(ActivityOptions activityOptions, Rect rect) {
        Binder binder = new Binder();
        this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda10(this, binder));
        activityOptions.setLaunchBounds(rect);
        activityOptions.setLaunchCookie(binder);
        activityOptions.setLaunchWindowingMode(6);
        activityOptions.setRemoveWithTaskOrganizer(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareActivityOptions$2(Binder binder) {
        this.mTaskOrganizer.setPendingLaunchCookieListener(binder, this);
    }

    public void setObscuredTouchRect(Rect rect) {
        this.mObscuredTouchRegion = rect != null ? new Region(rect) : null;
    }

    public final void onLocationChanged(WindowContainerTransaction windowContainerTransaction) {
        getBoundsOnScreen(this.mTmpRect);
        getRootView().getBoundsOnScreen(this.mTmpRootRect);
        if (!this.mTmpRootRect.contains(this.mTmpRect)) {
            this.mTmpRect.offsetTo(0, 0);
        }
        windowContainerTransaction.setBounds(this.mTaskToken, this.mTmpRect);
    }

    public void onLocationChanged() {
        if (this.mTaskToken != null) {
            if (!isUsingShellTransitions() || !this.mTaskViewTransitions.hasPending()) {
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                onLocationChanged(windowContainerTransaction);
                this.mSyncQueue.queue(windowContainerTransaction);
            }
        }
    }

    public void release() {
        performRelease();
    }

    public void finalize() throws Throwable {
        try {
            CloseGuard closeGuard = this.mGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
                performRelease();
            }
        } finally {
            super.finalize();
        }
    }

    public final void performRelease() {
        getHolder().removeCallback(this);
        TaskViewTransitions taskViewTransitions = this.mTaskViewTransitions;
        if (taskViewTransitions != null) {
            taskViewTransitions.removeTaskView(this);
        }
        this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda7(this));
        this.mGuard.close();
        if (this.mListener != null && this.mIsInitialized) {
            this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda8(this));
            this.mIsInitialized = false;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$performRelease$3() {
        this.mTaskOrganizer.removeListener(this);
        resetTaskInfo();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$performRelease$4() {
        this.mListener.onReleased();
    }

    public final void resetTaskInfo() {
        this.mTaskInfo = null;
        this.mTaskToken = null;
        this.mTaskLeash = null;
    }

    public final void updateTaskVisibility() {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setHidden(this.mTaskToken, !this.mSurfaceCreated);
        this.mSyncQueue.queue(windowContainerTransaction);
        if (this.mListener != null) {
            this.mSyncQueue.runInSync(new TaskView$$ExternalSyntheticLambda11(this, this.mTaskInfo.taskId));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTaskVisibility$6(int i, SurfaceControl.Transaction transaction) {
        this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda13(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTaskVisibility$5(int i) {
        this.mListener.onTaskVisibilityChanged(i, this.mSurfaceCreated);
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (!isUsingShellTransitions()) {
            this.mTaskInfo = runningTaskInfo;
            this.mTaskToken = runningTaskInfo.token;
            this.mTaskLeash = surfaceControl;
            if (this.mSurfaceCreated) {
                this.mTransaction.reparent(surfaceControl, getSurfaceControl()).show(this.mTaskLeash).apply();
            } else {
                updateTaskVisibility();
            }
            this.mTaskOrganizer.setInterceptBackPressedOnTaskRoot(this.mTaskToken, true);
            onLocationChanged();
            ActivityManager.TaskDescription taskDescription = runningTaskInfo.taskDescription;
            if (taskDescription != null) {
                this.mSyncQueue.runInSync(new TaskView$$ExternalSyntheticLambda3(this, taskDescription.getBackgroundColor()));
            }
            if (this.mListener != null) {
                this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda4(this, runningTaskInfo.taskId, runningTaskInfo.baseActivity));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$7(int i, SurfaceControl.Transaction transaction) {
        setResizeBackgroundColor(transaction, i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$8(int i, ComponentName componentName) {
        this.mListener.onTaskCreated(i, componentName);
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        WindowContainerToken windowContainerToken = this.mTaskToken;
        if (windowContainerToken != null && windowContainerToken.equals(runningTaskInfo.token)) {
            if (this.mListener != null) {
                this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda9(this, runningTaskInfo.taskId));
            }
            this.mTaskOrganizer.setInterceptBackPressedOnTaskRoot(this.mTaskToken, false);
            this.mTransaction.reparent(this.mTaskLeash, (SurfaceControl) null).apply();
            resetTaskInfo();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskVanished$9(int i) {
        this.mListener.onTaskRemovalStarted(i);
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        ActivityManager.TaskDescription taskDescription = runningTaskInfo.taskDescription;
        if (taskDescription != null) {
            setResizeBackgroundColor(taskDescription.getBackgroundColor());
        }
    }

    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        WindowContainerToken windowContainerToken = this.mTaskToken;
        if (windowContainerToken != null && windowContainerToken.equals(runningTaskInfo.token) && this.mListener != null) {
            this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda6(this, runningTaskInfo.taskId));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBackPressedOnTaskRoot$10(int i) {
        this.mListener.onBackPressedOnTaskRoot(i);
    }

    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        builder.setParent(findTaskSurface(i));
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.reparent(surfaceControl, findTaskSurface(i));
    }

    public final SurfaceControl findTaskSurface(int i) {
        SurfaceControl surfaceControl;
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
        if (runningTaskInfo != null && (surfaceControl = this.mTaskLeash) != null && runningTaskInfo.taskId == i) {
            return surfaceControl;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    public void dump(PrintWriter printWriter, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("  ");
        printWriter.println(str + this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TaskView:");
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
        sb.append(runningTaskInfo != null ? Integer.valueOf(runningTaskInfo.taskId) : "null");
        return sb.toString();
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mSurfaceCreated = true;
        if (this.mListener != null && !this.mIsInitialized) {
            this.mIsInitialized = true;
            this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda0(this));
        }
        this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$surfaceCreated$11() {
        this.mListener.onInitialized();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$surfaceCreated$12() {
        if (this.mTaskToken != null) {
            if (isUsingShellTransitions()) {
                this.mTaskViewTransitions.setTaskViewVisible(this, true);
                return;
            }
            this.mTransaction.reparent(this.mTaskLeash, getSurfaceControl()).show(this.mTaskLeash).apply();
            updateTaskVisibility();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (this.mTaskToken != null) {
            onLocationChanged();
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mSurfaceCreated = false;
        this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$surfaceDestroyed$13() {
        if (this.mTaskToken != null) {
            if (isUsingShellTransitions()) {
                this.mTaskViewTransitions.setTaskViewVisible(this, false);
                return;
            }
            this.mTransaction.reparent(this.mTaskLeash, (SurfaceControl) null).apply();
            updateTaskVisibility();
        }
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        if (internalInsetsInfo.touchableRegion.isEmpty()) {
            internalInsetsInfo.setTouchableInsets(3);
            View rootView = getRootView();
            rootView.getLocationInWindow(this.mTmpLocation);
            Rect rect = this.mTmpRootRect;
            int[] iArr = this.mTmpLocation;
            rect.set(iArr[0], iArr[1], rootView.getWidth(), rootView.getHeight());
            internalInsetsInfo.touchableRegion.set(this.mTmpRootRect);
        }
        getLocationInWindow(this.mTmpLocation);
        Rect rect2 = this.mTmpRect;
        int[] iArr2 = this.mTmpLocation;
        int i = iArr2[0];
        rect2.set(i, iArr2[1], getWidth() + i, this.mTmpLocation[1] + getHeight());
        internalInsetsInfo.touchableRegion.op(this.mTmpRect, Region.Op.DIFFERENCE);
        Region region = this.mObscuredTouchRegion;
        if (region != null) {
            internalInsetsInfo.touchableRegion.op(region, Region.Op.UNION);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
    }

    public ActivityManager.RunningTaskInfo getTaskInfo() {
        return this.mTaskInfo;
    }

    public void prepareHideAnimation(SurfaceControl.Transaction transaction) {
        if (this.mTaskToken != null) {
            transaction.reparent(this.mTaskLeash, (SurfaceControl) null).apply();
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onTaskVisibilityChanged(this.mTaskInfo.taskId, this.mSurfaceCreated);
            }
        }
    }

    public void prepareCloseAnimation() {
        if (this.mTaskToken != null) {
            if (this.mListener != null) {
                this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda14(this, this.mTaskInfo.taskId));
            }
            this.mTaskOrganizer.setInterceptBackPressedOnTaskRoot(this.mTaskToken, false);
        }
        resetTaskInfo();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareCloseAnimation$14(int i) {
        this.mListener.onTaskRemovalStarted(i);
    }

    public void prepareOpenAnimation(boolean z, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, WindowContainerTransaction windowContainerTransaction) {
        this.mTaskInfo = runningTaskInfo;
        WindowContainerToken windowContainerToken = runningTaskInfo.token;
        this.mTaskToken = windowContainerToken;
        this.mTaskLeash = surfaceControl;
        if (this.mSurfaceCreated) {
            transaction.reparent(surfaceControl, getSurfaceControl()).show(this.mTaskLeash).apply();
            transaction2.reparent(this.mTaskLeash, getSurfaceControl()).setPosition(this.mTaskLeash, 0.0f, 0.0f).apply();
            onLocationChanged(windowContainerTransaction);
        } else {
            windowContainerTransaction.setHidden(windowContainerToken, true);
        }
        if (z) {
            this.mTaskOrganizer.setInterceptBackPressedOnTaskRoot(this.mTaskToken, true);
        }
        ActivityManager.TaskDescription taskDescription = this.mTaskInfo.taskDescription;
        if (taskDescription != null) {
            setResizeBackgroundColor(transaction, taskDescription.getBackgroundColor());
        }
        if (this.mListener != null) {
            ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mTaskInfo;
            this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda15(this, z, runningTaskInfo2.taskId, runningTaskInfo2.baseActivity));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareOpenAnimation$15(boolean z, int i, ComponentName componentName) {
        if (z) {
            this.mListener.onTaskCreated(i, componentName);
        }
        if (!z || !this.mSurfaceCreated) {
            this.mListener.onTaskVisibilityChanged(i, this.mSurfaceCreated);
        }
    }
}
