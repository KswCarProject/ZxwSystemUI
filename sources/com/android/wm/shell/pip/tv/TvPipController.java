package com.android.wm.shell.pip.tv;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.app.TaskInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.RemoteException;
import com.android.wm.shell.R;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipAppOpsListener;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.tv.TvPipBoundsController;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TvPipController implements PipTransitionController.PipTransitionCallback, TvPipBoundsController.PipBoundsListener, TvPipMenuController.Delegate, TvPipNotificationController.Delegate, DisplayController.OnDisplaysChangedListener {
    public final PipAppOpsListener mAppOpsListener;
    public RemoteAction mCloseAction;
    public final Context mContext;
    public int mEduTextWindowExitAnimationDurationMs;
    public final TvPipImpl mImpl = new TvPipImpl();
    public final ShellExecutor mMainExecutor;
    public int mPinnedTaskId = -1;
    public int mPipForceCloseDelay;
    public final PipMediaController mPipMediaController;
    public final TvPipNotificationController mPipNotificationController;
    public final PipTaskOrganizer mPipTaskOrganizer;
    public int mPreviousGravity = 85;
    public int mResizeAnimationDuration;
    public int mState = 0;
    public final TvPipBoundsAlgorithm mTvPipBoundsAlgorithm;
    public final TvPipBoundsController mTvPipBoundsController;
    public final TvPipBoundsState mTvPipBoundsState;
    public final TvPipMenuController mTvPipMenuController;

    public static Pip create(Context context, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsController tvPipBoundsController, PipAppOpsListener pipAppOpsListener, PipTaskOrganizer pipTaskOrganizer, PipTransitionController pipTransitionController, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, DisplayController displayController, WindowManagerShellWrapper windowManagerShellWrapper, ShellExecutor shellExecutor) {
        TvPipController tvPipController = r0;
        TvPipController tvPipController2 = new TvPipController(context, tvPipBoundsState, tvPipBoundsAlgorithm, tvPipBoundsController, pipAppOpsListener, pipTaskOrganizer, pipTransitionController, tvPipMenuController, pipMediaController, tvPipNotificationController, taskStackListenerImpl, pipParamsChangedForwarder, displayController, windowManagerShellWrapper, shellExecutor);
        return tvPipController.mImpl;
    }

    public TvPipController(Context context, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsController tvPipBoundsController, PipAppOpsListener pipAppOpsListener, PipTaskOrganizer pipTaskOrganizer, PipTransitionController pipTransitionController, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, DisplayController displayController, WindowManagerShellWrapper windowManagerShellWrapper, ShellExecutor shellExecutor) {
        TvPipMenuController tvPipMenuController2 = tvPipMenuController;
        TvPipNotificationController tvPipNotificationController2 = tvPipNotificationController;
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mTvPipBoundsState = tvPipBoundsState;
        tvPipBoundsState.setDisplayId(context.getDisplayId());
        tvPipBoundsState.setDisplayLayout(new DisplayLayout(context, context.getDisplay()));
        this.mTvPipBoundsAlgorithm = tvPipBoundsAlgorithm;
        this.mTvPipBoundsController = tvPipBoundsController;
        tvPipBoundsController.setListener(this);
        this.mPipMediaController = pipMediaController;
        this.mPipNotificationController = tvPipNotificationController2;
        tvPipNotificationController2.setDelegate(this);
        this.mTvPipMenuController = tvPipMenuController2;
        tvPipMenuController2.setDelegate(this);
        this.mAppOpsListener = pipAppOpsListener;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        PipTransitionController pipTransitionController2 = pipTransitionController;
        pipTransitionController.registerPipTransitionCallback(this);
        loadConfigurations();
        registerPipParamsChangedListener(pipParamsChangedForwarder);
        registerTaskStackListenerCallback(taskStackListenerImpl);
        registerWmShellPinnedStackListener(windowManagerShellWrapper);
        displayController.addDisplayWindowListener(this);
    }

    public final void onConfigurationChanged(Configuration configuration) {
        if (isPipShown()) {
            closePip();
        }
        loadConfigurations();
        this.mPipNotificationController.onConfigurationChanged(this.mContext);
        this.mTvPipBoundsAlgorithm.onConfigurationChanged(this.mContext);
    }

    public final boolean isPipShown() {
        return this.mState != 0;
    }

    public void showPictureInPictureMenu() {
        if (this.mState != 0) {
            setState(2);
            this.mTvPipMenuController.showMenu();
            updatePinnedStackBounds();
        }
    }

    public void onMenuClosed() {
        setState(1);
        updatePinnedStackBounds();
    }

    public void onInMoveModeChanged() {
        updatePinnedStackBounds();
    }

    public void movePipToFullscreen() {
        this.mPipTaskOrganizer.exitPip(this.mResizeAnimationDuration, false);
        onPipDisappeared();
    }

    public void togglePipExpansion() {
        boolean z = !this.mTvPipBoundsState.isTvPipExpanded();
        int updateGravityOnExpandToggled = this.mTvPipBoundsAlgorithm.updateGravityOnExpandToggled(this.mPreviousGravity, z);
        if (updateGravityOnExpandToggled != 0) {
            this.mPreviousGravity = updateGravityOnExpandToggled;
        }
        this.mTvPipBoundsState.setTvPipManuallyCollapsed(!z);
        this.mTvPipBoundsState.setTvPipExpanded(z);
        this.mPipNotificationController.updateExpansionState();
        updatePinnedStackBounds();
    }

    public void enterPipMovementMenu() {
        setState(2);
        this.mTvPipMenuController.showMovementMenuOnly();
    }

    public void movePip(int i) {
        if (this.mTvPipBoundsAlgorithm.updateGravity(i)) {
            this.mTvPipMenuController.updateGravity(this.mTvPipBoundsState.getTvPipGravity());
            this.mPreviousGravity = 0;
            updatePinnedStackBounds();
        }
    }

    public int getPipGravity() {
        return this.mTvPipBoundsState.getTvPipGravity();
    }

    public void onKeepClearAreasChanged(int i, Set<Rect> set, Set<Rect> set2) {
        if (this.mTvPipBoundsState.getDisplayId() == i) {
            this.mTvPipBoundsState.setKeepClearAreas(set, set2);
            updatePinnedStackBounds(this.mResizeAnimationDuration, !Objects.equals(set2, this.mTvPipBoundsState.getUnrestrictedKeepClearAreas()));
        }
    }

    public final void updatePinnedStackBounds() {
        updatePinnedStackBounds(this.mResizeAnimationDuration, true);
    }

    public final void updatePinnedStackBounds(int i, boolean z) {
        if (this.mState != 0) {
            boolean isInMoveMode = this.mTvPipMenuController.isInMoveMode();
            this.mTvPipBoundsController.recalculatePipBounds(isInMoveMode, this.mState == 2 || isInMoveMode, i, z);
        }
    }

    public void onPipTargetBoundsChange(Rect rect, int i) {
        this.mPipTaskOrganizer.scheduleAnimateResizePip(rect, i, new TvPipController$$ExternalSyntheticLambda1(this));
        this.mTvPipMenuController.onPipTransitionStarted(rect);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPipTargetBoundsChange$0(Rect rect) {
        this.mTvPipMenuController.updateExpansionState();
    }

    public void closePip() {
        RemoteAction remoteAction = this.mCloseAction;
        if (remoteAction != null) {
            try {
                remoteAction.getActionIntent().send();
            } catch (PendingIntent.CanceledException e) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    String valueOf = String.valueOf(e);
                    ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1447706303, 0, (String) null, "TvPipController", valueOf);
                }
            }
            this.mMainExecutor.executeDelayed(new TvPipController$$ExternalSyntheticLambda0(this), (long) this.mPipForceCloseDelay);
            return;
        }
        closeCurrentPiP(this.mPinnedTaskId);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$closePip$1() {
        closeCurrentPiP(this.mPinnedTaskId);
    }

    public final void closeCurrentPiP(int i) {
        int i2 = this.mPinnedTaskId;
        if (i2 == i) {
            removeTask(i2);
            onPipDisappeared();
        } else if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -1170726109, 0, (String) null, "TvPipController");
        }
    }

    public void closeEduText() {
        updatePinnedStackBounds(this.mEduTextWindowExitAnimationDurationMs, false);
    }

    public final void registerSessionListenerForCurrentUser() {
        this.mPipMediaController.registerSessionListenerForCurrentUser();
    }

    public final void checkIfPinnedTaskAppeared() {
        TaskInfo pinnedTaskInfo = getPinnedTaskInfo();
        if (pinnedTaskInfo != null && pinnedTaskInfo.topActivity != null) {
            this.mPinnedTaskId = pinnedTaskInfo.taskId;
            this.mPipMediaController.onActivityPinned();
            this.mPipNotificationController.show(pinnedTaskInfo.topActivity.getPackageName());
        }
    }

    public final void checkIfPinnedTaskIsGone() {
        if (isPipShown() && getPinnedTaskInfo() == null) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 2088927117, 0, (String) null, "TvPipController");
            }
            onPipDisappeared();
        }
    }

    public final void onPipDisappeared() {
        this.mPipNotificationController.dismiss();
        this.mTvPipMenuController.closeMenu();
        this.mTvPipBoundsState.resetTvPipState();
        this.mTvPipBoundsController.onPipDismissed();
        setState(0);
        this.mPinnedTaskId = -1;
    }

    public void onPipTransitionStarted(int i, Rect rect) {
        this.mTvPipMenuController.notifyPipAnimating(true);
    }

    public void onPipTransitionCanceled(int i) {
        this.mTvPipMenuController.notifyPipAnimating(false);
    }

    public void onPipTransitionFinished(int i) {
        if (PipAnimationController.isInPipDirection(i) && this.mState == 0) {
            setState(1);
        }
        this.mTvPipMenuController.notifyPipAnimating(false);
    }

    public final void setState(int i) {
        this.mState = i;
    }

    public final void loadConfigurations() {
        Resources resources = this.mContext.getResources();
        this.mResizeAnimationDuration = resources.getInteger(R.integer.config_pipResizeAnimationDuration);
        this.mPipForceCloseDelay = resources.getInteger(R.integer.config_pipForceCloseDelay);
        this.mEduTextWindowExitAnimationDurationMs = resources.getInteger(R.integer.pip_edu_text_window_exit_animation_duration_ms);
    }

    public final void registerTaskStackListenerCallback(TaskStackListenerImpl taskStackListenerImpl) {
        taskStackListenerImpl.addListener(new TaskStackListenerCallback() {
            public void onActivityPinned(String str, int i, int i2, int i3) {
                TvPipController.this.checkIfPinnedTaskAppeared();
                TvPipController.this.mAppOpsListener.onActivityPinned(str);
            }

            public void onActivityUnpinned() {
                TvPipController.this.mAppOpsListener.onActivityUnpinned();
            }

            public void onTaskStackChanged() {
                TvPipController.this.checkIfPinnedTaskIsGone();
            }

            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
                if (runningTaskInfo.getWindowingMode() == 2) {
                    TvPipController.this.movePipToFullscreen();
                }
            }
        });
    }

    public final void registerPipParamsChangedListener(PipParamsChangedForwarder pipParamsChangedForwarder) {
        pipParamsChangedForwarder.addListener(new PipParamsChangedForwarder.PipParamsChangedCallback() {
            public void onActionsChanged(List<RemoteAction> list, RemoteAction remoteAction) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 462871743, 0, (String) null, "TvPipController");
                }
                TvPipController.this.mTvPipMenuController.setAppActions(list, remoteAction);
                TvPipController.this.mCloseAction = remoteAction;
            }

            public void onAspectRatioChanged(float f) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1408555887, 8, (String) null, "TvPipController", Double.valueOf((double) f));
                }
                TvPipController.this.mTvPipBoundsState.setAspectRatio(f);
                if (!TvPipController.this.mTvPipBoundsState.isTvPipExpanded()) {
                    TvPipController.this.updatePinnedStackBounds();
                }
            }

            public void onExpandedAspectRatioChanged(float f) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1353368200, 8, (String) null, "TvPipController", Double.valueOf((double) f));
                }
                TvPipController.this.mTvPipBoundsState.setDesiredTvExpandedAspectRatio(f, false);
                TvPipController.this.mTvPipMenuController.updateExpansionState();
                if (TvPipController.this.mTvPipBoundsState.isTvPipExpanded() && f != 0.0f) {
                    TvPipController.this.mTvPipBoundsAlgorithm.updateExpandedPipSize();
                    TvPipController.this.updatePinnedStackBounds();
                }
                if (TvPipController.this.mTvPipBoundsState.isTvPipExpanded() && f == 0.0f) {
                    int updateGravityOnExpandToggled = TvPipController.this.mTvPipBoundsAlgorithm.updateGravityOnExpandToggled(TvPipController.this.mPreviousGravity, false);
                    if (updateGravityOnExpandToggled != 0) {
                        TvPipController.this.mPreviousGravity = updateGravityOnExpandToggled;
                    }
                    TvPipController.this.mTvPipBoundsState.setTvPipExpanded(false);
                    TvPipController.this.updatePinnedStackBounds();
                }
                if (!TvPipController.this.mTvPipBoundsState.isTvPipExpanded() && f != 0.0f && !TvPipController.this.mTvPipBoundsState.isTvPipManuallyCollapsed()) {
                    TvPipController.this.mTvPipBoundsAlgorithm.updateExpandedPipSize();
                    int updateGravityOnExpandToggled2 = TvPipController.this.mTvPipBoundsAlgorithm.updateGravityOnExpandToggled(TvPipController.this.mPreviousGravity, true);
                    if (updateGravityOnExpandToggled2 != 0) {
                        TvPipController.this.mPreviousGravity = updateGravityOnExpandToggled2;
                    }
                    TvPipController.this.mTvPipBoundsState.setTvPipExpanded(true);
                    TvPipController.this.updatePinnedStackBounds();
                }
            }
        });
    }

    public final void registerWmShellPinnedStackListener(WindowManagerShellWrapper windowManagerShellWrapper) {
        try {
            windowManagerShellWrapper.addPinnedStackListener(new PinnedStackListenerForwarder.PinnedTaskListener() {
                public void onImeVisibilityChanged(boolean z, int i) {
                    if (z != TvPipController.this.mTvPipBoundsState.isImeShowing() || (z && i != TvPipController.this.mTvPipBoundsState.getImeHeight())) {
                        TvPipController.this.mTvPipBoundsState.setImeVisibility(z, i);
                        if (TvPipController.this.mState != 0) {
                            TvPipController.this.updatePinnedStackBounds();
                        }
                    }
                }
            });
        } catch (RemoteException e) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                String valueOf = String.valueOf(e);
                ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1581014830, 0, (String) null, "TvPipController", valueOf);
            }
        }
    }

    public static TaskInfo getPinnedTaskInfo() {
        try {
            return ActivityTaskManager.getService().getRootTaskInfo(2, 0);
        } catch (RemoteException e) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                String valueOf = String.valueOf(e);
                ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -1679295386, 0, (String) null, "TvPipController", valueOf);
            }
            return null;
        }
    }

    public static void removeTask(int i) {
        try {
            ActivityTaskManager.getService().removeTask(i);
        } catch (Exception e) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                String valueOf = String.valueOf(e);
                ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 802286616, 0, (String) null, "TvPipController", valueOf);
            }
        }
    }

    public class TvPipImpl implements Pip {
        public TvPipImpl() {
        }

        public void onConfigurationChanged(Configuration configuration) {
            TvPipController.this.mMainExecutor.execute(new TvPipController$TvPipImpl$$ExternalSyntheticLambda1(this, configuration));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigurationChanged$0(Configuration configuration) {
            TvPipController.this.onConfigurationChanged(configuration);
        }

        public void registerSessionListenerForCurrentUser() {
            TvPipController.this.mMainExecutor.execute(new TvPipController$TvPipImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerSessionListenerForCurrentUser$1() {
            TvPipController.this.registerSessionListenerForCurrentUser();
        }
    }
}
