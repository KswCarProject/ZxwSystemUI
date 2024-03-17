package com.android.wm.shell.splitscreen;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.RemoteTransition;
import android.window.WindowContainerTransaction;
import com.android.internal.logging.InstanceId;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SingleInstanceRemoteListener;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.common.split.SplitLayout;
import com.android.wm.shell.draganddrop.DragAndDropPolicy;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.ISplitScreen;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.transition.LegacyTransitions$ILegacyTransition;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public class SplitScreenController implements DragAndDropPolicy.Starter, RemoteCallable<SplitScreenController> {
    public static final String TAG = "SplitScreenController";
    public final Context mContext;
    public final DisplayController mDisplayController;
    public final DisplayImeController mDisplayImeController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final IconProvider mIconProvider;
    public final SplitScreenImpl mImpl = new SplitScreenImpl();
    public final SplitscreenEventLogger mLogger;
    public final ShellExecutor mMainExecutor;
    public final Optional<RecentTasksController> mRecentTasksOptional;
    public final RootTaskDisplayAreaOrganizer mRootTDAOrganizer;
    public SurfaceControl mSplitTasksContainerLayer;
    public StageCoordinator mStageCoordinator;
    public final SyncTransactionQueue mSyncQueue;
    public final ShellTaskOrganizer mTaskOrganizer;
    public final TransactionPool mTransactionPool;
    public final Transitions mTransitions;
    public final Provider<Optional<StageTaskUnfoldController>> mUnfoldControllerProvider;

    public SplitScreenController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellExecutor shellExecutor, DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, Transitions transitions, TransactionPool transactionPool, IconProvider iconProvider, Optional<RecentTasksController> optional, Provider<Optional<StageTaskUnfoldController>> provider) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSyncQueue = syncTransactionQueue;
        this.mContext = context;
        this.mRootTDAOrganizer = rootTaskDisplayAreaOrganizer;
        this.mMainExecutor = shellExecutor;
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mTransitions = transitions;
        this.mTransactionPool = transactionPool;
        this.mUnfoldControllerProvider = provider;
        this.mLogger = new SplitscreenEventLogger();
        this.mIconProvider = iconProvider;
        this.mRecentTasksOptional = optional;
    }

    public SplitScreen asSplitScreen() {
        return this.mImpl;
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public void onOrganizerRegistered() {
        if (this.mStageCoordinator == null) {
            this.mStageCoordinator = new StageCoordinator(this.mContext, 0, this.mSyncQueue, this.mTaskOrganizer, this.mDisplayController, this.mDisplayImeController, this.mDisplayInsetsController, this.mTransitions, this.mTransactionPool, this.mLogger, this.mIconProvider, this.mMainExecutor, this.mRecentTasksOptional, this.mUnfoldControllerProvider);
        }
    }

    public boolean isSplitScreenVisible() {
        return this.mStageCoordinator.isSplitScreenVisible();
    }

    public ActivityManager.RunningTaskInfo getTaskInfo(int i) {
        if (!isSplitScreenVisible() || i == -1) {
            return null;
        }
        return this.mTaskOrganizer.getRunningTaskInfo(this.mStageCoordinator.getTaskId(i));
    }

    public boolean isTaskInSplitScreen(int i) {
        return isSplitScreenVisible() && this.mStageCoordinator.getStageOfTask(i) != -1;
    }

    public boolean moveToSideStage(int i, int i2) {
        return moveToStage(i, 1, i2, new WindowContainerTransaction());
    }

    public final boolean moveToStage(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskOrganizer.getRunningTaskInfo(i);
        if (runningTaskInfo != null) {
            return this.mStageCoordinator.moveToStage(runningTaskInfo, i2, i3, windowContainerTransaction);
        }
        throw new IllegalArgumentException("Unknown taskId" + i);
    }

    public boolean removeFromSideStage(int i) {
        return this.mStageCoordinator.removeFromSideStage(i);
    }

    public void setSideStagePosition(int i) {
        this.mStageCoordinator.setSideStagePosition(i, (WindowContainerTransaction) null);
    }

    public void prepareEnterSplitScreen(WindowContainerTransaction windowContainerTransaction, ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
        this.mStageCoordinator.prepareEnterSplitScreen(windowContainerTransaction, runningTaskInfo, i);
    }

    public void finishEnterSplitScreen(SurfaceControl.Transaction transaction) {
        this.mStageCoordinator.finishEnterSplitScreen(transaction);
    }

    public void enterSplitScreen(int i, boolean z, WindowContainerTransaction windowContainerTransaction) {
        moveToStage(i, isSplitScreenVisible() ? -1 : 1, z ^ true ? 1 : 0, windowContainerTransaction);
    }

    public void exitSplitScreen(int i, int i2) {
        this.mStageCoordinator.exitSplitScreen(i, i2);
    }

    public void onKeyguardVisibilityChanged(boolean z) {
        this.mStageCoordinator.onKeyguardVisibilityChanged(z);
    }

    public void onFinishedWakingUp() {
        this.mStageCoordinator.onFinishedWakingUp();
    }

    public void exitSplitScreenOnHide(boolean z) {
        this.mStageCoordinator.exitSplitScreenOnHide(z);
    }

    public void getStageBounds(Rect rect, Rect rect2) {
        this.mStageCoordinator.getStageBounds(rect, rect2);
    }

    public void registerSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        this.mStageCoordinator.registerSplitScreenListener(splitScreenListener);
    }

    public void unregisterSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        this.mStageCoordinator.unregisterSplitScreenListener(splitScreenListener);
    }

    public void startTask(int i, int i2, Bundle bundle) {
        Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i2, bundle, (WindowContainerTransaction) null);
        try {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mStageCoordinator.prepareEvictChildTasks(i2, windowContainerTransaction);
            int startActivityFromRecents = ActivityTaskManager.getService().startActivityFromRecents(i, resolveStartStage);
            if (startActivityFromRecents == 0 || startActivityFromRecents == 2) {
                this.mSyncQueue.queue(windowContainerTransaction);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to launch task", e);
        }
    }

    public void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) {
        Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i, bundle, (WindowContainerTransaction) null);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        this.mStageCoordinator.prepareEvictChildTasks(i, windowContainerTransaction);
        try {
            ((LauncherApps) this.mContext.getSystemService(LauncherApps.class)).startShortcut(str, str2, (Rect) null, resolveStartStage, userHandle);
            this.mSyncQueue.queue(windowContainerTransaction);
        } catch (ActivityNotFoundException e) {
            Slog.e(TAG, "Failed to launch shortcut", e);
        }
    }

    public void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            startIntentLegacy(pendingIntent, intent, i, bundle);
            return;
        }
        try {
            Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i, bundle, (WindowContainerTransaction) null);
            if (intent == null) {
                intent = new Intent();
            }
            Intent intent2 = intent;
            intent2.addFlags(262144);
            pendingIntent.send(this.mContext, 0, intent2, (PendingIntent.OnFinished) null, (Handler) null, (String) null, resolveStartStage);
        } catch (PendingIntent.CanceledException e) {
            Slog.e(TAG, "Failed to launch task", e);
        }
    }

    public final void startIntentLegacy(final PendingIntent pendingIntent, Intent intent, final int i, Bundle bundle) {
        final WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        this.mStageCoordinator.prepareEvictChildTasks(i, windowContainerTransaction);
        AnonymousClass1 r1 = new LegacyTransitions$ILegacyTransition() {
            public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, SurfaceControl.Transaction transaction) {
                ComponentName componentName = null;
                if (remoteAnimationTargetArr == null || remoteAnimationTargetArr.length == 0) {
                    ActivityManager.RunningTaskInfo taskInfo = SplitScreenController.this.getTaskInfo(SplitLayout.reversePosition(i));
                    ComponentName componentName2 = taskInfo != null ? taskInfo.baseActivity : null;
                    if (pendingIntent.getIntent() != null) {
                        componentName = pendingIntent.getIntent().getComponent();
                    }
                    if (componentName2 != null && componentName2.equals(componentName)) {
                        SplitScreenController splitScreenController = SplitScreenController.this;
                        splitScreenController.setSideStagePosition(SplitLayout.reversePosition(splitScreenController.mStageCoordinator.getSideStagePosition()));
                    }
                    transaction.apply();
                    return;
                }
                SplitScreenController.this.mStageCoordinator.updateSurfaceBounds((SplitLayout) null, transaction, false);
                for (RemoteAnimationTarget remoteAnimationTarget : remoteAnimationTargetArr) {
                    if (remoteAnimationTarget.mode == 0) {
                        transaction.show(remoteAnimationTarget.leash);
                    }
                }
                transaction.apply();
                if (iRemoteAnimationFinishedCallback != null) {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException e) {
                        Slog.e(SplitScreenController.TAG, "Error finishing legacy transition: ", e);
                    }
                }
                SplitScreenController.this.mSyncQueue.queue(windowContainerTransaction);
            }
        };
        WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
        Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i, bundle, windowContainerTransaction2);
        if (intent == null) {
            intent = new Intent();
        }
        intent.addFlags(262144);
        windowContainerTransaction2.sendPendingIntent(pendingIntent, intent, resolveStartStage);
        this.mSyncQueue.queue(r1, 1, windowContainerTransaction2);
    }

    public RemoteAnimationTarget[] onGoingToRecentsLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) {
        if (isSplitScreenVisible()) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mStageCoordinator.prepareEvictInvisibleChildTasks(windowContainerTransaction);
            this.mSyncQueue.queue(windowContainerTransaction);
        }
        return reparentSplitTasksForAnimation(remoteAnimationTargetArr, true);
    }

    public RemoteAnimationTarget[] onStartingSplitLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) {
        return reparentSplitTasksForAnimation(remoteAnimationTargetArr, false);
    }

    public final RemoteAnimationTarget[] reparentSplitTasksForAnimation(RemoteAnimationTarget[] remoteAnimationTargetArr, boolean z) {
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            return null;
        }
        if (z && !isSplitScreenVisible()) {
            return null;
        }
        if (!z && remoteAnimationTargetArr.length < 2) {
            return null;
        }
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        SurfaceControl surfaceControl = this.mSplitTasksContainerLayer;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
        }
        SurfaceControl.Builder callsite = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("RecentsAnimationSplitTasks").setHidden(false).setCallsite("SplitScreenController#onGoingtoRecentsLegacy");
        this.mRootTDAOrganizer.attachToDisplayArea(0, callsite);
        this.mSplitTasksContainerLayer = callsite.build();
        Arrays.sort(remoteAnimationTargetArr, new SplitScreenController$$ExternalSyntheticLambda0());
        int length = remoteAnimationTargetArr.length;
        int i = 0;
        int i2 = 1;
        while (i < length) {
            RemoteAnimationTarget remoteAnimationTarget = remoteAnimationTargetArr[i];
            transaction.reparent(remoteAnimationTarget.leash, this.mSplitTasksContainerLayer);
            SurfaceControl surfaceControl2 = remoteAnimationTarget.leash;
            Rect rect = remoteAnimationTarget.screenSpaceBounds;
            transaction.setPosition(surfaceControl2, (float) rect.left, (float) rect.top);
            transaction.setLayer(remoteAnimationTarget.leash, i2);
            i++;
            i2++;
        }
        transaction.apply();
        transaction.close();
        return new RemoteAnimationTarget[]{this.mStageCoordinator.getDividerBarLegacyTarget()};
    }

    public static /* synthetic */ int lambda$reparentSplitTasksForAnimation$0(RemoteAnimationTarget remoteAnimationTarget, RemoteAnimationTarget remoteAnimationTarget2) {
        return remoteAnimationTarget.prefixOrderIndex - remoteAnimationTarget2.prefixOrderIndex;
    }

    public void logOnDroppedToSplit(int i, InstanceId instanceId) {
        this.mStageCoordinator.logOnDroppedToSplit(i, instanceId);
    }

    public static String exitReasonToString(int i) {
        switch (i) {
            case 0:
                return "UNKNOWN_EXIT";
            case 1:
                return "APP_DOES_NOT_SUPPORT_MULTIWINDOW";
            case 2:
                return "APP_FINISHED";
            case 3:
                return "DEVICE_FOLDED";
            case 4:
                return "DRAG_DIVIDER";
            case 5:
                return "RETURN_HOME";
            case 6:
                return "ROOT_TASK_VANISHED";
            case 7:
                return "SCREEN_LOCKED";
            case 8:
                return "SCREEN_LOCKED_SHOW_ON_TOP";
            case 9:
                return "CHILD_TASK_ENTER_PIP";
            default:
                return "unknown reason, reason int = " + i;
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + TAG);
        StageCoordinator stageCoordinator = this.mStageCoordinator;
        if (stageCoordinator != null) {
            stageCoordinator.dump(printWriter, str);
        }
    }

    public class SplitScreenImpl implements SplitScreen {
        public final ArrayMap<SplitScreen.SplitScreenListener, Executor> mExecutors;
        public ISplitScreenImpl mISplitScreen;
        public final SplitScreen.SplitScreenListener mListener;

        public SplitScreenImpl() {
            this.mExecutors = new ArrayMap<>();
            this.mListener = new SplitScreen.SplitScreenListener() {
                public void onStagePositionChanged(int i, int i2) {
                    for (int i3 = 0; i3 < SplitScreenImpl.this.mExecutors.size(); i3++) {
                        ((Executor) SplitScreenImpl.this.mExecutors.valueAt(i3)).execute(new SplitScreenController$SplitScreenImpl$1$$ExternalSyntheticLambda1(this, i3, i, i2));
                    }
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onStagePositionChanged$0(int i, int i2, int i3) {
                    ((SplitScreen.SplitScreenListener) SplitScreenImpl.this.mExecutors.keyAt(i)).onStagePositionChanged(i2, i3);
                }

                public void onTaskStageChanged(int i, int i2, boolean z) {
                    for (int i3 = 0; i3 < SplitScreenImpl.this.mExecutors.size(); i3++) {
                        ((Executor) SplitScreenImpl.this.mExecutors.valueAt(i3)).execute(new SplitScreenController$SplitScreenImpl$1$$ExternalSyntheticLambda2(this, i3, i, i2, z));
                    }
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onTaskStageChanged$1(int i, int i2, int i3, boolean z) {
                    ((SplitScreen.SplitScreenListener) SplitScreenImpl.this.mExecutors.keyAt(i)).onTaskStageChanged(i2, i3, z);
                }

                public void onSplitVisibilityChanged(boolean z) {
                    for (int i = 0; i < SplitScreenImpl.this.mExecutors.size(); i++) {
                        ((Executor) SplitScreenImpl.this.mExecutors.valueAt(i)).execute(new SplitScreenController$SplitScreenImpl$1$$ExternalSyntheticLambda0(this, i, z));
                    }
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onSplitVisibilityChanged$2(int i, boolean z) {
                    ((SplitScreen.SplitScreenListener) SplitScreenImpl.this.mExecutors.keyAt(i)).onSplitVisibilityChanged(z);
                }
            };
        }

        public ISplitScreen createExternalInterface() {
            ISplitScreenImpl iSplitScreenImpl = this.mISplitScreen;
            if (iSplitScreenImpl != null) {
                iSplitScreenImpl.invalidate();
            }
            ISplitScreenImpl iSplitScreenImpl2 = new ISplitScreenImpl(SplitScreenController.this);
            this.mISplitScreen = iSplitScreenImpl2;
            return iSplitScreenImpl2;
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onKeyguardVisibilityChanged$3(boolean z) {
            SplitScreenController.this.onKeyguardVisibilityChanged(z);
        }

        public void onFinishedWakingUp() {
            SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onFinishedWakingUp$4() {
            SplitScreenController.this.onFinishedWakingUp();
        }
    }

    public static class ISplitScreenImpl extends ISplitScreen.Stub {
        public SplitScreenController mController;
        public final SingleInstanceRemoteListener<SplitScreenController, ISplitScreenListener> mListener;
        public final SplitScreen.SplitScreenListener mSplitScreenListener = new SplitScreen.SplitScreenListener() {
            public void onStagePositionChanged(int i, int i2) {
                ISplitScreenImpl.this.mListener.call(new SplitScreenController$ISplitScreenImpl$1$$ExternalSyntheticLambda0(i, i2));
            }

            public void onTaskStageChanged(int i, int i2, boolean z) {
                ISplitScreenImpl.this.mListener.call(new SplitScreenController$ISplitScreenImpl$1$$ExternalSyntheticLambda1(i, i2, z));
            }
        };

        public ISplitScreenImpl(SplitScreenController splitScreenController) {
            this.mController = splitScreenController;
            this.mListener = new SingleInstanceRemoteListener<>(splitScreenController, new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda10(this), new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda11(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(SplitScreenController splitScreenController) {
            splitScreenController.registerSplitScreenListener(this.mSplitScreenListener);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(SplitScreenController splitScreenController) {
            splitScreenController.unregisterSplitScreenListener(this.mSplitScreenListener);
        }

        public void invalidate() {
            this.mController = null;
        }

        public void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "registerSplitScreenListener", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7(this, iSplitScreenListener));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerSplitScreenListener$2(ISplitScreenListener iSplitScreenListener, SplitScreenController splitScreenController) {
            this.mListener.register(iSplitScreenListener);
        }

        public void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "unregisterSplitScreenListener", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda14(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$unregisterSplitScreenListener$3(SplitScreenController splitScreenController) {
            this.mListener.unregister();
        }

        public void exitSplitScreen(int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "exitSplitScreen", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda5(i));
        }

        public void exitSplitScreenOnHide(boolean z) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "exitSplitScreenOnHide", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda13(z));
        }

        public void removeFromSideStage(int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "removeFromSideStage", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda12(i));
        }

        public void startTask(int i, int i2, Bundle bundle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTask", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0(i, i2, bundle));
        }

        public void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteAnimationAdapter remoteAnimationAdapter) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTasks", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda6(i, bundle, i2, bundle2, i3, f, remoteAnimationAdapter));
        }

        public void startIntentAndTaskWithLegacyTransition(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle, Bundle bundle2, int i2, float f, RemoteAnimationAdapter remoteAnimationAdapter) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startIntentAndTaskWithLegacyTransition", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3(pendingIntent, intent, i, bundle, bundle2, i2, f, remoteAnimationAdapter));
        }

        public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteTransition remoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTasks", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda4(i, bundle, i2, bundle2, i3, f, remoteTransition));
        }

        public void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startShortcut", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8(str, str2, i, bundle, userHandle));
        }

        public void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startIntent", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda9(pendingIntent, intent, i, bundle));
        }

        public RemoteAnimationTarget[] onGoingToRecentsLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) {
            RemoteAnimationTarget[][] remoteAnimationTargetArr2 = {null};
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "onGoingToRecentsLegacy", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda1(remoteAnimationTargetArr2, remoteAnimationTargetArr), true);
            return remoteAnimationTargetArr2[0];
        }

        public static /* synthetic */ void lambda$onGoingToRecentsLegacy$13(RemoteAnimationTarget[][] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, SplitScreenController splitScreenController) {
            remoteAnimationTargetArr[0] = splitScreenController.onGoingToRecentsLegacy(remoteAnimationTargetArr2);
        }

        public RemoteAnimationTarget[] onStartingSplitLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) {
            RemoteAnimationTarget[][] remoteAnimationTargetArr2 = {null};
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "onStartingSplitLegacy", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2(remoteAnimationTargetArr2, remoteAnimationTargetArr), true);
            return remoteAnimationTargetArr2[0];
        }

        public static /* synthetic */ void lambda$onStartingSplitLegacy$14(RemoteAnimationTarget[][] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, SplitScreenController splitScreenController) {
            remoteAnimationTargetArr[0] = splitScreenController.onStartingSplitLegacy(remoteAnimationTargetArr2);
        }
    }
}
