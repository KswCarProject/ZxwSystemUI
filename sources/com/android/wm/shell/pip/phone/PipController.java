package com.android.wm.shell.pip.phone;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.Size;
import android.view.SurfaceControl;
import android.view.WindowManagerGlobal;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.wm.shell.R;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SingleInstanceRemoteListener;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.onehanded.OneHandedTransitionCallback;
import com.android.wm.shell.pip.IPip;
import com.android.wm.shell.pip.IPipAnimationListener;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipAppOpsListener;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipUtils;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class PipController implements PipTransitionController.PipTransitionCallback, RemoteCallable<PipController> {
    public PipAppOpsListener mAppOpsListener;
    public Context mContext;
    public DisplayController mDisplayController;
    @VisibleForTesting
    public final DisplayController.OnDisplaysChangedListener mDisplaysChangedListener = new DisplayController.OnDisplaysChangedListener() {
        public void onFixedRotationStarted(int i, int i2) {
            PipController.this.mIsInFixedRotation = true;
        }

        public void onFixedRotationFinished(int i) {
            PipController.this.mIsInFixedRotation = false;
        }

        public void onDisplayAdded(int i) {
            if (i == PipController.this.mPipBoundsState.getDisplayId()) {
                PipController pipController = PipController.this;
                pipController.onDisplayChanged(pipController.mDisplayController.getDisplayLayout(i), false);
            }
        }

        public void onDisplayConfigurationChanged(int i, Configuration configuration) {
            if (i == PipController.this.mPipBoundsState.getDisplayId()) {
                PipController pipController = PipController.this;
                pipController.onDisplayChanged(pipController.mDisplayController.getDisplayLayout(i), true);
            }
        }

        public void onKeepClearAreasChanged(int i, Set<Rect> set, Set<Rect> set2) {
            if (PipController.this.mPipBoundsState.getDisplayId() == i) {
                PipController.this.mPipBoundsState.setKeepClearAreas(set, set2);
            }
        }
    };
    public final int mEnterAnimationDuration;
    public final PipImpl mImpl;
    public boolean mIsInFixedRotation;
    public boolean mIsKeyguardShowingOrAnimating;
    public ShellExecutor mMainExecutor;
    public PipMediaController mMediaController;
    public PhonePipMenuController mMenuController;
    public Optional<OneHandedController> mOneHandedController;
    public PipAnimationListener mPinnedStackAnimationRecentsCallback;
    public PinnedStackListenerForwarder.PinnedTaskListener mPinnedTaskListener = new PipControllerPinnedTaskListener();
    public PipBoundsAlgorithm mPipBoundsAlgorithm;
    public PipBoundsState mPipBoundsState;
    public PipInputConsumer mPipInputConsumer;
    public PipParamsChangedForwarder mPipParamsChangedForwarder;
    public PipTaskOrganizer mPipTaskOrganizer;
    public PipTransitionController mPipTransitionController;
    public final DisplayChangeController.OnDisplayChangingListener mRotationController = new PipController$$ExternalSyntheticLambda0(this);
    public TaskStackListenerImpl mTaskStackListener;
    public final Rect mTmpInsetBounds = new Rect();
    public PipTouchHandler mTouchHandler;
    public WindowManagerShellWrapper mWindowManagerShellWrapper;

    public interface PipAnimationListener {
        void onExpandPip();

        void onPipAnimationStarted();

        void onPipResourceDimensionsChanged(int i, int i2);
    }

    public final String getTransitionTag(int i) {
        switch (i) {
            case 2:
                return "TRANSITION_TO_PIP";
            case 3:
                return "TRANSITION_LEAVE_PIP";
            case 4:
                return "TRANSITION_LEAVE_PIP_TO_SPLIT_SCREEN";
            case 5:
                return "TRANSITION_REMOVE_STACK";
            case 6:
                return "TRANSITION_SNAP_AFTER_RESIZE";
            case 7:
                return "TRANSITION_USER_RESIZE";
            case 8:
                return "TRANSITION_EXPAND_OR_UNEXPAND";
            default:
                return "TRANSITION_LEAVE_UNKNOWN";
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        if (!this.mPipTransitionController.handleRotateDisplay(i2, i3, windowContainerTransaction)) {
            if (this.mPipBoundsState.getDisplayLayout().rotation() == i3) {
                updateMovementBounds((Rect) null, false, false, false, windowContainerTransaction);
            } else if (!this.mPipTaskOrganizer.isInPip() || this.mPipTaskOrganizer.isEntryScheduled()) {
                onDisplayRotationChangedNotInPip(this.mContext, i3);
                updateMovementBounds(this.mPipBoundsState.getNormalBounds(), true, false, false, windowContainerTransaction);
                this.mPipTaskOrganizer.onDisplayRotationSkipped();
            } else {
                Rect currentOrAnimatingBounds = this.mPipTaskOrganizer.getCurrentOrAnimatingBounds();
                Rect rect = new Rect();
                if (onDisplayRotationChanged(this.mContext, rect, currentOrAnimatingBounds, this.mTmpInsetBounds, i, i2, i3, windowContainerTransaction)) {
                    this.mTouchHandler.adjustBoundsForRotation(rect, this.mPipBoundsState.getBounds(), this.mTmpInsetBounds);
                    if (!this.mIsInFixedRotation) {
                        this.mPipBoundsState.setShelfVisibility(false, 0, false);
                        this.mPipBoundsState.setImeVisibility(false, 0);
                        this.mTouchHandler.onShelfVisibilityChanged(false, 0);
                        this.mTouchHandler.onImeVisibilityChanged(false, 0);
                    }
                    updateMovementBounds(rect, true, false, false, windowContainerTransaction);
                }
            }
        }
    }

    public class PipControllerPinnedTaskListener extends PinnedStackListenerForwarder.PinnedTaskListener {
        public PipControllerPinnedTaskListener() {
        }

        public void onImeVisibilityChanged(boolean z, int i) {
            PipController.this.mPipBoundsState.setImeVisibility(z, i);
            PipController.this.mTouchHandler.onImeVisibilityChanged(z, i);
        }

        public void onMovementBoundsChanged(boolean z) {
            PipController.this.updateMovementBounds((Rect) null, false, z, false, (WindowContainerTransaction) null);
        }

        public void onActivityHidden(ComponentName componentName) {
            if (componentName.equals(PipController.this.mPipBoundsState.getLastPipComponentName())) {
                PipController.this.mPipBoundsState.setLastPipComponentName((ComponentName) null);
            }
        }
    }

    public static Pip create(Context context, DisplayController displayController, PipAppOpsListener pipAppOpsListener, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMediaController pipMediaController, PhonePipMenuController phonePipMenuController, PipTaskOrganizer pipTaskOrganizer, PipTouchHandler pipTouchHandler, PipTransitionController pipTransitionController, WindowManagerShellWrapper windowManagerShellWrapper, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<OneHandedController> optional, ShellExecutor shellExecutor) {
        if (context.getPackageManager().hasSystemFeature("android.software.picture_in_picture")) {
            return new PipController(context, displayController, pipAppOpsListener, pipBoundsAlgorithm, pipBoundsState, pipMediaController, phonePipMenuController, pipTaskOrganizer, pipTouchHandler, pipTransitionController, windowManagerShellWrapper, taskStackListenerImpl, pipParamsChangedForwarder, optional, shellExecutor).mImpl;
        }
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 377854703, 0, (String) null, "PipController");
        }
        return null;
    }

    public PipController(Context context, DisplayController displayController, PipAppOpsListener pipAppOpsListener, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMediaController pipMediaController, PhonePipMenuController phonePipMenuController, PipTaskOrganizer pipTaskOrganizer, PipTouchHandler pipTouchHandler, PipTransitionController pipTransitionController, WindowManagerShellWrapper windowManagerShellWrapper, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<OneHandedController> optional, ShellExecutor shellExecutor) {
        if (UserManager.get(context).getProcessUserId() == 0) {
            this.mContext = context;
            this.mImpl = new PipImpl();
            this.mWindowManagerShellWrapper = windowManagerShellWrapper;
            this.mDisplayController = displayController;
            this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
            this.mPipBoundsState = pipBoundsState;
            this.mPipTaskOrganizer = pipTaskOrganizer;
            this.mMainExecutor = shellExecutor;
            this.mMediaController = pipMediaController;
            this.mMenuController = phonePipMenuController;
            this.mTouchHandler = pipTouchHandler;
            this.mAppOpsListener = pipAppOpsListener;
            this.mOneHandedController = optional;
            this.mPipTransitionController = pipTransitionController;
            this.mTaskStackListener = taskStackListenerImpl;
            this.mEnterAnimationDuration = this.mContext.getResources().getInteger(R.integer.config_pipEnterAnimationDuration);
            this.mPipParamsChangedForwarder = pipParamsChangedForwarder;
            this.mMainExecutor.execute(new PipController$$ExternalSyntheticLambda1(this));
            return;
        }
        throw new IllegalStateException("Non-primary Pip component not currently supported.");
    }

    public void init() {
        this.mPipInputConsumer = new PipInputConsumer(WindowManagerGlobal.getWindowManagerService(), "pip_input_consumer", this.mMainExecutor);
        this.mPipTransitionController.registerPipTransitionCallback(this);
        this.mPipTaskOrganizer.registerOnDisplayIdChangeCallback(new PipController$$ExternalSyntheticLambda2(this));
        this.mPipBoundsState.setOnMinimalSizeChangeCallback(new PipController$$ExternalSyntheticLambda3(this));
        this.mPipBoundsState.setOnShelfVisibilityChangeCallback(new PipController$$ExternalSyntheticLambda4(this));
        PipTouchHandler pipTouchHandler = this.mTouchHandler;
        if (pipTouchHandler != null) {
            PipInputConsumer pipInputConsumer = this.mPipInputConsumer;
            Objects.requireNonNull(pipTouchHandler);
            pipInputConsumer.setInputListener(new PipController$$ExternalSyntheticLambda5(pipTouchHandler));
            PipInputConsumer pipInputConsumer2 = this.mPipInputConsumer;
            PipTouchHandler pipTouchHandler2 = this.mTouchHandler;
            Objects.requireNonNull(pipTouchHandler2);
            pipInputConsumer2.setRegistrationListener(new PipController$$ExternalSyntheticLambda6(pipTouchHandler2));
        }
        this.mDisplayController.addDisplayChangingController(this.mRotationController);
        this.mDisplayController.addDisplayWindowListener(this.mDisplaysChangedListener);
        this.mPipBoundsState.setDisplayId(this.mContext.getDisplayId());
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Context context = this.mContext;
        pipBoundsState.setDisplayLayout(new DisplayLayout(context, context.getDisplay()));
        try {
            this.mWindowManagerShellWrapper.addPinnedStackListener(this.mPinnedTaskListener);
        } catch (RemoteException e) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                String valueOf = String.valueOf(e);
                ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 2043633764, 0, (String) null, "PipController", valueOf);
            }
        }
        try {
            if (ActivityTaskManager.getService().getRootTaskInfo(2, 0) != null) {
                this.mPipInputConsumer.registerInputConsumer();
            }
        } catch (RemoteException | UnsupportedOperationException e2) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                String valueOf2 = String.valueOf(e2);
                ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 2043633764, 0, (String) null, "PipController", valueOf2);
            }
            e2.printStackTrace();
        }
        this.mTaskStackListener.addListener(new TaskStackListenerCallback() {
            public void onActivityPinned(String str, int i, int i2, int i3) {
                PipController.this.mTouchHandler.onActivityPinned();
                PipController.this.mMediaController.onActivityPinned();
                PipController.this.mAppOpsListener.onActivityPinned(str);
                PipController.this.mPipInputConsumer.registerInputConsumer();
            }

            public void onActivityUnpinned() {
                PipController.this.mTouchHandler.onActivityUnpinned((ComponentName) PipUtils.getTopPipActivity(PipController.this.mContext).first);
                PipController.this.mAppOpsListener.onActivityUnpinned();
                PipController.this.mPipInputConsumer.unregisterInputConsumer();
            }

            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
                if (runningTaskInfo.getWindowingMode() == 2) {
                    PipController.this.mTouchHandler.getMotionHelper().expandLeavePip(z2);
                }
            }
        });
        this.mPipParamsChangedForwarder.addListener(new PipParamsChangedForwarder.PipParamsChangedCallback() {
            public void onAspectRatioChanged(float f) {
                PipController.this.mPipBoundsState.setAspectRatio(f);
                Rect adjustedDestinationBounds = PipController.this.mPipBoundsAlgorithm.getAdjustedDestinationBounds(PipController.this.mPipBoundsState.getBounds(), PipController.this.mPipBoundsState.getAspectRatio());
                Objects.requireNonNull(adjustedDestinationBounds, "Missing destination bounds");
                PipController pipController = PipController.this;
                pipController.mPipTaskOrganizer.scheduleAnimateResizePip(adjustedDestinationBounds, pipController.mEnterAnimationDuration, (Consumer<Rect>) null);
                PipController.this.mTouchHandler.onAspectRatioChanged();
                PipController.this.updateMovementBounds((Rect) null, false, false, false, (WindowContainerTransaction) null);
            }

            public void onActionsChanged(List<RemoteAction> list, RemoteAction remoteAction) {
                PipController.this.mMenuController.setAppActions(list, remoteAction);
            }
        });
        this.mOneHandedController.ifPresent(new PipController$$ExternalSyntheticLambda7(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(int i) {
        this.mPipBoundsState.setDisplayId(i);
        onDisplayChanged(this.mDisplayController.getDisplayLayout(i), false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$2() {
        updateMovementBounds((Rect) null, false, false, false, (WindowContainerTransaction) null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$3(Boolean bool, Integer num, Boolean bool2) {
        this.mTouchHandler.onShelfVisibilityChanged(bool.booleanValue(), num.intValue());
        if (bool2.booleanValue()) {
            updateMovementBounds(this.mPipBoundsState.getBounds(), false, false, true, (WindowContainerTransaction) null);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$4(OneHandedController oneHandedController) {
        oneHandedController.asOneHanded().registerTransitionCallback(new OneHandedTransitionCallback() {
            public void onStartFinished(Rect rect) {
                PipController.this.mTouchHandler.setOhmOffset(rect.top);
            }

            public void onStopFinished(Rect rect) {
                PipController.this.mTouchHandler.setOhmOffset(rect.top);
            }
        });
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public final void onConfigurationChanged(Configuration configuration) {
        this.mPipBoundsAlgorithm.onConfigurationChanged(this.mContext);
        this.mTouchHandler.onConfigurationChanged();
        this.mPipBoundsState.onConfigurationChanged();
    }

    public final void onDensityOrFontScaleChanged() {
        this.mPipTaskOrganizer.onDensityOrFontScaleChanged(this.mContext);
        onPipResourceDimensionsChanged();
    }

    public final void onOverlayChanged() {
        this.mTouchHandler.onOverlayChanged();
        Context context = this.mContext;
        onDisplayChanged(new DisplayLayout(context, context.getDisplay()), false);
    }

    public final void onDisplayChanged(DisplayLayout displayLayout, boolean z) {
        if (!this.mPipBoundsState.getDisplayLayout().isSameGeometry(displayLayout)) {
            PipController$$ExternalSyntheticLambda8 pipController$$ExternalSyntheticLambda8 = new PipController$$ExternalSyntheticLambda8(this, displayLayout);
            if (!this.mPipTaskOrganizer.isInPip() || !z) {
                pipController$$ExternalSyntheticLambda8.run();
                return;
            }
            this.mMenuController.attachPipMenuView();
            PipSnapAlgorithm snapAlgorithm = this.mPipBoundsAlgorithm.getSnapAlgorithm();
            Rect rect = new Rect(this.mPipBoundsState.getBounds());
            float snapFraction = snapAlgorithm.getSnapFraction(rect, this.mPipBoundsAlgorithm.getMovementBounds(rect), this.mPipBoundsState.getStashedState());
            pipController$$ExternalSyntheticLambda8.run();
            snapAlgorithm.applySnapFraction(rect, this.mPipBoundsAlgorithm.getMovementBounds(rect, false), snapFraction, this.mPipBoundsState.getStashedState(), this.mPipBoundsState.getStashOffset(), this.mPipBoundsState.getDisplayBounds(), this.mPipBoundsState.getDisplayLayout().stableInsets());
            this.mTouchHandler.getMotionHelper().movePip(rect);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayChanged$5(DisplayLayout displayLayout) {
        boolean z = Transitions.ENABLE_SHELL_TRANSITIONS && this.mPipBoundsState.getDisplayLayout().rotation() != displayLayout.rotation();
        this.mPipBoundsState.setDisplayLayout(displayLayout);
        WindowContainerTransaction windowContainerTransaction = z ? new WindowContainerTransaction() : null;
        updateMovementBounds((Rect) null, z, false, false, windowContainerTransaction);
        if (windowContainerTransaction != null) {
            this.mPipTaskOrganizer.applyFinishBoundsResize(windowContainerTransaction, 1, false);
        }
    }

    public final void registerSessionListenerForCurrentUser() {
        this.mMediaController.registerSessionListenerForCurrentUser();
    }

    public final void onSystemUiStateChanged(boolean z, int i) {
        this.mTouchHandler.onSystemUiStateChanged(z);
    }

    public void hidePipMenu(Runnable runnable, Runnable runnable2) {
        this.mMenuController.hideMenu(runnable, runnable2);
    }

    public void showPictureInPictureMenu() {
        this.mTouchHandler.showPictureInPictureMenu();
    }

    public final void onKeyguardVisibilityChanged(boolean z, boolean z2) {
        if (this.mPipTaskOrganizer.isInPip()) {
            if (z) {
                this.mIsKeyguardShowingOrAnimating = true;
                hidePipMenu((Runnable) null, (Runnable) null);
                this.mPipTaskOrganizer.setPipVisibility(false);
            } else if (!z2) {
                this.mIsKeyguardShowingOrAnimating = false;
                this.mPipTaskOrganizer.setPipVisibility(true);
            }
        }
    }

    public final void onKeyguardDismissAnimationFinished() {
        if (this.mPipTaskOrganizer.isInPip()) {
            this.mIsKeyguardShowingOrAnimating = false;
            this.mPipTaskOrganizer.setPipVisibility(true);
        }
    }

    public final void setShelfHeight(boolean z, int i) {
        if (!this.mIsKeyguardShowingOrAnimating) {
            setShelfHeightLocked(z, i);
        }
    }

    public final void setShelfHeightLocked(boolean z, int i) {
        if (!z) {
            i = 0;
        }
        this.mPipBoundsState.setShelfVisibility(z, i);
    }

    public final void setPinnedStackAnimationType(int i) {
        this.mPipTaskOrganizer.setOneShotAnimationType(i);
        this.mPipTransitionController.setIsFullAnimation(i == 0);
    }

    public final void setPinnedStackAnimationListener(PipAnimationListener pipAnimationListener) {
        this.mPinnedStackAnimationRecentsCallback = pipAnimationListener;
        onPipResourceDimensionsChanged();
    }

    public final void onPipResourceDimensionsChanged() {
        PipAnimationListener pipAnimationListener = this.mPinnedStackAnimationRecentsCallback;
        if (pipAnimationListener != null) {
            pipAnimationListener.onPipResourceDimensionsChanged(this.mContext.getResources().getDimensionPixelSize(R.dimen.pip_corner_radius), this.mContext.getResources().getDimensionPixelSize(R.dimen.pip_shadow_radius));
        }
    }

    public final Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) {
        setShelfHeightLocked(i2 > 0, i2);
        onDisplayRotationChangedNotInPip(this.mContext, i);
        Rect startSwipePipToHome = this.mPipTaskOrganizer.startSwipePipToHome(componentName, activityInfo, pictureInPictureParams);
        this.mPipBoundsState.setNormalBounds(startSwipePipToHome);
        return startSwipePipToHome;
    }

    public final void stopSwipePipToHome(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
        this.mPipTaskOrganizer.stopSwipePipToHome(i, componentName, rect, surfaceControl);
    }

    public void onPipTransitionStarted(int i, Rect rect) {
        InteractionJankMonitor.getInstance().begin(InteractionJankMonitor.Configuration.Builder.withSurface(35, this.mContext, this.mPipTaskOrganizer.getSurfaceControl()).setTag(getTransitionTag(i)).setTimeout(2000));
        if (PipAnimationController.isOutPipDirection(i)) {
            saveReentryState(rect);
        }
        this.mTouchHandler.setTouchEnabled(false);
        PipAnimationListener pipAnimationListener = this.mPinnedStackAnimationRecentsCallback;
        if (pipAnimationListener != null) {
            pipAnimationListener.onPipAnimationStarted();
            if (i == 3) {
                this.mPinnedStackAnimationRecentsCallback.onExpandPip();
            }
        }
    }

    public void saveReentryState(Rect rect) {
        float snapFraction = this.mPipBoundsAlgorithm.getSnapFraction(rect);
        if (this.mPipBoundsState.hasUserResizedPip()) {
            Rect userResizeBounds = this.mTouchHandler.getUserResizeBounds();
            this.mPipBoundsState.saveReentryState(new Size(userResizeBounds.width(), userResizeBounds.height()), snapFraction);
            return;
        }
        this.mPipBoundsState.saveReentryState((Size) null, snapFraction);
    }

    public void onPipTransitionFinished(int i) {
        onPipTransitionFinishedOrCanceled(i);
    }

    public void onPipTransitionCanceled(int i) {
        onPipTransitionFinishedOrCanceled(i);
    }

    public final void onPipTransitionFinishedOrCanceled(int i) {
        InteractionJankMonitor.getInstance().end(35);
        this.mTouchHandler.setTouchEnabled(true);
        this.mTouchHandler.onPinnedStackAnimationEnded(i);
    }

    public final void updateMovementBounds(Rect rect, boolean z, boolean z2, boolean z3, WindowContainerTransaction windowContainerTransaction) {
        Rect rect2 = new Rect(rect);
        int rotation = this.mPipBoundsState.getDisplayLayout().rotation();
        this.mPipBoundsAlgorithm.getInsetBounds(this.mTmpInsetBounds);
        this.mPipBoundsState.setNormalBounds(this.mPipBoundsAlgorithm.getNormalBounds());
        if (rect2.isEmpty()) {
            rect2.set(this.mPipBoundsAlgorithm.getDefaultBounds());
        }
        this.mPipTaskOrganizer.onMovementBoundsChanged(rect2, z, z2, z3, windowContainerTransaction);
        this.mPipTaskOrganizer.finishResizeForMenu(rect2);
        this.mTouchHandler.onMovementBoundsChanged(this.mTmpInsetBounds, this.mPipBoundsState.getNormalBounds(), rect2, z2, z3, rotation);
    }

    public final void onDisplayRotationChangedNotInPip(Context context, int i) {
        this.mPipBoundsState.getDisplayLayout().rotateTo(context.getResources(), i);
    }

    public final boolean onDisplayRotationChanged(Context context, Rect rect, Rect rect2, Rect rect3, int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        Rect rect4 = rect;
        int i4 = i3;
        if (i == this.mPipBoundsState.getDisplayId() && i2 != i4) {
            try {
                ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(2, 0);
                if (rootTaskInfo == null) {
                    return false;
                }
                PipSnapAlgorithm snapAlgorithm = this.mPipBoundsAlgorithm.getSnapAlgorithm();
                Rect rect5 = new Rect(rect2);
                float snapFraction = snapAlgorithm.getSnapFraction(rect5, this.mPipBoundsAlgorithm.getMovementBounds(rect5), this.mPipBoundsState.getStashedState());
                this.mPipBoundsState.getDisplayLayout().rotateTo(context.getResources(), i4);
                snapAlgorithm.applySnapFraction(rect5, this.mPipBoundsAlgorithm.getMovementBounds(rect5, false), snapFraction, this.mPipBoundsState.getStashedState(), this.mPipBoundsState.getStashOffset(), this.mPipBoundsState.getDisplayBounds(), this.mPipBoundsState.getDisplayLayout().stableInsets());
                this.mPipBoundsAlgorithm.getInsetBounds(rect3);
                rect4.set(rect5);
                windowContainerTransaction.setBounds(rootTaskInfo.token, rect4);
                return true;
            } catch (RemoteException e) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    String valueOf = String.valueOf(e);
                    ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -1027114398, 0, (String) null, "PipController", valueOf);
                }
            }
        }
        return false;
    }

    public final void dump(PrintWriter printWriter) {
        printWriter.println("PipController");
        this.mMenuController.dump(printWriter, "  ");
        this.mTouchHandler.dump(printWriter, "  ");
        this.mPipBoundsAlgorithm.dump(printWriter, "  ");
        this.mPipTaskOrganizer.dump(printWriter, "  ");
        this.mPipBoundsState.dump(printWriter, "  ");
        this.mPipInputConsumer.dump(printWriter, "  ");
    }

    public class PipImpl implements Pip {
        public IPipImpl mIPip;

        public PipImpl() {
        }

        public IPip createExternalInterface() {
            IPipImpl iPipImpl = this.mIPip;
            if (iPipImpl != null) {
                iPipImpl.invalidate();
            }
            IPipImpl iPipImpl2 = new IPipImpl(PipController.this);
            this.mIPip = iPipImpl2;
            return iPipImpl2;
        }

        public void onConfigurationChanged(Configuration configuration) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda7(this, configuration));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigurationChanged$1(Configuration configuration) {
            PipController.this.onConfigurationChanged(configuration);
        }

        public void onDensityOrFontScaleChanged() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda8(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onDensityOrFontScaleChanged$2() {
            PipController.this.onDensityOrFontScaleChanged();
        }

        public void onOverlayChanged() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onOverlayChanged$3() {
            PipController.this.onOverlayChanged();
        }

        public void onSystemUiStateChanged(boolean z, int i) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda1(this, z, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSystemUiStateChanged$4(boolean z, int i) {
            PipController.this.onSystemUiStateChanged(z, i);
        }

        public void registerSessionListenerForCurrentUser() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda5(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerSessionListenerForCurrentUser$5() {
            PipController.this.registerSessionListenerForCurrentUser();
        }

        public void setPinnedStackAnimationType(int i) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda10(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setPinnedStackAnimationType$7(int i) {
            PipController.this.setPinnedStackAnimationType(i);
        }

        public void addPipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda4(this, consumer));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$addPipExclusionBoundsChangeListener$8(Consumer consumer) {
            PipController.this.mPipBoundsState.addPipExclusionBoundsChangeCallback(consumer);
        }

        public void removePipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda6(this, consumer));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$removePipExclusionBoundsChangeListener$9(Consumer consumer) {
            PipController.this.mPipBoundsState.removePipExclusionBoundsChangeCallback(consumer);
        }

        public void showPictureInPictureMenu() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda9(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$showPictureInPictureMenu$10() {
            PipController.this.showPictureInPictureMenu();
        }

        public void onKeyguardVisibilityChanged(boolean z, boolean z2) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda3(this, z, z2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onKeyguardVisibilityChanged$11(boolean z, boolean z2) {
            PipController.this.onKeyguardVisibilityChanged(z, z2);
        }

        public void onKeyguardDismissAnimationFinished() {
            PipController pipController = PipController.this;
            pipController.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda2(pipController));
        }

        public void dump(PrintWriter printWriter) {
            try {
                PipController.this.mMainExecutor.executeBlocking(new PipController$PipImpl$$ExternalSyntheticLambda11(this, printWriter));
            } catch (InterruptedException unused) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -1234573202, 0, (String) null, "PipController");
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$dump$13(PrintWriter printWriter) {
            PipController.this.dump(printWriter);
        }
    }

    public static class IPipImpl extends IPip.Stub {
        public PipController mController;
        public final SingleInstanceRemoteListener<PipController, IPipAnimationListener> mListener;
        public final PipAnimationListener mPipAnimationListener = new PipAnimationListener() {
            public void onPipAnimationStarted() {
                IPipImpl.this.mListener.call(new PipController$IPipImpl$1$$ExternalSyntheticLambda0());
            }

            public void onPipResourceDimensionsChanged(int i, int i2) {
                IPipImpl.this.mListener.call(new PipController$IPipImpl$1$$ExternalSyntheticLambda2(i, i2));
            }

            public void onExpandPip() {
                IPipImpl.this.mListener.call(new PipController$IPipImpl$1$$ExternalSyntheticLambda1());
            }
        };

        public IPipImpl(PipController pipController) {
            this.mController = pipController;
            this.mListener = new SingleInstanceRemoteListener<>(this.mController, new PipController$IPipImpl$$ExternalSyntheticLambda4(this), new PipController$IPipImpl$$ExternalSyntheticLambda5());
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(PipController pipController) {
            pipController.setPinnedStackAnimationListener(this.mPipAnimationListener);
        }

        public void invalidate() {
            this.mController = null;
        }

        public Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) {
            Rect[] rectArr = new Rect[1];
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startSwipePipToHome", new PipController$IPipImpl$$ExternalSyntheticLambda1(rectArr, componentName, activityInfo, pictureInPictureParams, i, i2), true);
            return rectArr[0];
        }

        public static /* synthetic */ void lambda$startSwipePipToHome$2(Rect[] rectArr, ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2, PipController pipController) {
            rectArr[0] = pipController.startSwipePipToHome(componentName, activityInfo, pictureInPictureParams, i, i2);
        }

        public void stopSwipePipToHome(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "stopSwipePipToHome", new PipController$IPipImpl$$ExternalSyntheticLambda0(i, componentName, rect, surfaceControl));
        }

        public void setShelfHeight(boolean z, int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setShelfHeight", new PipController$IPipImpl$$ExternalSyntheticLambda3(z, i));
        }

        public void setPinnedStackAnimationListener(IPipAnimationListener iPipAnimationListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setPinnedStackAnimationListener", new PipController$IPipImpl$$ExternalSyntheticLambda2(this, iPipAnimationListener));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setPinnedStackAnimationListener$5(IPipAnimationListener iPipAnimationListener, PipController pipController) {
            if (iPipAnimationListener != null) {
                this.mListener.register(iPipAnimationListener);
            } else {
                this.mListener.unregister();
            }
        }
    }
}
