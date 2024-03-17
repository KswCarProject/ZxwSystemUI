package com.android.wm.shell.pip;

import android.app.ActivityManager;
import android.app.PictureInPictureParams;
import android.app.TaskInfo;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.RotationUtils;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.R;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.transition.CounterRotatorHelper;
import com.android.wm.shell.transition.Transitions;
import java.util.Optional;

public class PipTransition extends PipTransitionController {
    public static final String TAG = "PipTransition";
    public final Context mContext;
    public WindowContainerToken mCurrentPipTaskToken;
    public int mEndFixedRotation;
    public final int mEnterExitAnimationDuration;
    public final Rect mExitDestinationBounds = new Rect();
    public IBinder mExitTransition;
    public Transitions.TransitionFinishCallback mFinishCallback;
    public SurfaceControl.Transaction mFinishTransaction;
    public boolean mHasFadeOut;
    public boolean mInFixedRotation;
    public int mOneShotAnimationType = 0;
    public final PipTransitionState mPipTransitionState;
    public WindowContainerToken mRequestedEnterTask;
    public IBinder mRequestedEnterTransition;
    public final Optional<SplitScreenController> mSplitScreenOptional;
    public final PipSurfaceTransactionHelper mSurfaceTransactionHelper;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PipTransition(Context context, PipBoundsState pipBoundsState, PipTransitionState pipTransitionState, PipMenuController pipMenuController, PipBoundsAlgorithm pipBoundsAlgorithm, PipAnimationController pipAnimationController, Transitions transitions, ShellTaskOrganizer shellTaskOrganizer, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, Optional<SplitScreenController> optional) {
        super(pipBoundsState, pipMenuController, pipBoundsAlgorithm, pipAnimationController, transitions, shellTaskOrganizer);
        this.mContext = context;
        this.mPipTransitionState = pipTransitionState;
        this.mEnterExitAnimationDuration = context.getResources().getInteger(R.integer.config_pipResizeAnimationDuration);
        this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
        this.mSplitScreenOptional = optional;
    }

    public void setIsFullAnimation(boolean z) {
        setOneShotAnimationType(z ^ true ? 1 : 0);
    }

    public final void setOneShotAnimationType(int i) {
        this.mOneShotAnimationType = i;
    }

    public void startExitTransition(int i, WindowContainerTransaction windowContainerTransaction, Rect rect) {
        if (rect != null) {
            this.mExitDestinationBounds.set(rect);
        }
        this.mExitTransition = this.mTransitions.startTransition(i, windowContainerTransaction, this);
    }

    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        IBinder iBinder2 = iBinder;
        TransitionInfo transitionInfo2 = transitionInfo;
        SurfaceControl.Transaction transaction3 = transaction;
        SurfaceControl.Transaction transaction4 = transaction2;
        TransitionInfo.Change findCurrentPipTaskChange = findCurrentPipTaskChange(transitionInfo);
        TransitionInfo.Change findFixedRotationChange = findFixedRotationChange(transitionInfo);
        boolean z = findFixedRotationChange != null;
        this.mInFixedRotation = z;
        this.mEndFixedRotation = z ? findFixedRotationChange.getEndFixedRotation() : -1;
        int type = transitionInfo.getType();
        if (iBinder.equals(this.mExitTransition)) {
            this.mExitDestinationBounds.setEmpty();
            this.mExitTransition = null;
            this.mHasFadeOut = false;
            if (this.mFinishCallback == null) {
                if (findCurrentPipTaskChange != null) {
                    runningTaskInfo = findCurrentPipTaskChange.getTaskInfo();
                } else {
                    runningTaskInfo = this.mPipOrganizer.getTaskInfo();
                }
                ActivityManager.RunningTaskInfo runningTaskInfo2 = runningTaskInfo;
                if (runningTaskInfo2 != null) {
                    switch (type) {
                        case 13:
                            startExitAnimation(transitionInfo, transaction, transaction2, transitionFinishCallback, runningTaskInfo2, findCurrentPipTaskChange);
                            break;
                        case 14:
                            startExitToSplitAnimation(transitionInfo, transaction, transaction2, transitionFinishCallback, runningTaskInfo2);
                            break;
                        case 15:
                            removePipImmediately(transitionInfo, transaction, transaction2, transitionFinishCallback, runningTaskInfo2);
                            break;
                        default:
                            throw new IllegalStateException("mExitTransition with unexpected transit type=" + WindowManager.transitTypeToString(type));
                    }
                    this.mCurrentPipTaskToken = null;
                    return true;
                }
                throw new RuntimeException("Cannot find the pip task for exit-pip transition.");
            }
            callFinishCallback((WindowContainerTransaction) null);
            this.mFinishTransaction = null;
            throw new RuntimeException("Previous callback not called, aborting exit PIP.");
        }
        if (iBinder2 == this.mRequestedEnterTransition) {
            this.mRequestedEnterTransition = null;
            this.mRequestedEnterTask = null;
        }
        if (!(findCurrentPipTaskChange == null || findCurrentPipTaskChange.getTaskInfo().getWindowingMode() == 2)) {
            resetPrevPip(findCurrentPipTaskChange, transaction);
        }
        if (isEnteringPip(transitionInfo, this.mCurrentPipTaskToken)) {
            return startEnterAnimation(transitionInfo, transaction, transaction2, transitionFinishCallback);
        }
        if (findCurrentPipTaskChange != null) {
            updatePipForUnhandledTransition(findCurrentPipTaskChange, transaction, transaction2);
        }
        if (this.mPipTransitionState.isInPip() && !this.mInFixedRotation && this.mHasFadeOut) {
            fadeExistingPip(true);
        }
        return false;
    }

    public final boolean isAnimatingLocally() {
        return this.mFinishTransaction != null;
    }

    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        if (transitionRequestInfo.getType() != 10) {
            return null;
        }
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        if (this.mOneShotAnimationType == 1) {
            this.mRequestedEnterTransition = iBinder;
            this.mRequestedEnterTask = transitionRequestInfo.getTriggerTask().token;
            windowContainerTransaction.setActivityWindowingMode(transitionRequestInfo.getTriggerTask().token, 0);
            windowContainerTransaction.setBounds(transitionRequestInfo.getTriggerTask().token, this.mPipBoundsAlgorithm.getEntryDestinationBounds());
        }
        return windowContainerTransaction;
    }

    public boolean handleRotateDisplay(int i, int i2, WindowContainerTransaction windowContainerTransaction) {
        if (this.mRequestedEnterTransition == null || this.mOneShotAnimationType != 1 || RotationUtils.deltaRotation(i, i2) == 0) {
            return false;
        }
        this.mPipBoundsState.getDisplayLayout().rotateTo(this.mContext.getResources(), i2);
        windowContainerTransaction.setBounds(this.mRequestedEnterTask, this.mPipBoundsAlgorithm.getEntryDestinationBounds());
        return true;
    }

    public void onTransitionMerged(IBinder iBinder) {
        boolean z;
        if (iBinder == this.mExitTransition) {
            if (this.mPipAnimationController.getCurrentAnimator() != null) {
                this.mPipAnimationController.getCurrentAnimator().cancel();
                z = true;
            } else {
                z = false;
            }
            this.mExitTransition = null;
            if (z) {
                ActivityManager.RunningTaskInfo taskInfo = this.mPipOrganizer.getTaskInfo();
                if (taskInfo != null) {
                    startExpandAnimation(taskInfo, this.mPipOrganizer.getSurfaceControl(), new Rect(this.mExitDestinationBounds), 0);
                }
                this.mExitDestinationBounds.setEmpty();
                this.mCurrentPipTaskToken = null;
            }
        }
    }

    public void onFinishResize(TaskInfo taskInfo, Rect rect, int i, SurfaceControl.Transaction transaction) {
        boolean isInPipDirection = PipAnimationController.isInPipDirection(i);
        if (isInPipDirection) {
            this.mPipTransitionState.setTransitionState(4);
        }
        if ((this.mExitTransition == null || isAnimatingLocally()) && this.mFinishCallback != null) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            prepareFinishResizeTransaction(taskInfo, rect, i, windowContainerTransaction);
            if (transaction != null) {
                windowContainerTransaction.setBoundsChangeTransaction(taskInfo.token, transaction);
            }
            SurfaceControl surfaceControl = this.mPipOrganizer.getSurfaceControl();
            int displayRotation = taskInfo.getConfiguration().windowConfiguration.getDisplayRotation();
            if (isInPipDirection && this.mInFixedRotation && this.mEndFixedRotation != displayRotation && surfaceControl != null && surfaceControl.isValid()) {
                Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
                Rect rect2 = new Rect(rect);
                RotationUtils.rotateBounds(rect2, displayBounds, this.mEndFixedRotation, displayRotation);
                this.mSurfaceTransactionHelper.crop(this.mFinishTransaction, surfaceControl, rect2);
            }
            this.mFinishTransaction = null;
            callFinishCallback(windowContainerTransaction);
        }
        finishResizeForMenu(rect);
    }

    public final void callFinishCallback(WindowContainerTransaction windowContainerTransaction) {
        Transitions.TransitionFinishCallback transitionFinishCallback = this.mFinishCallback;
        this.mFinishCallback = null;
        transitionFinishCallback.onTransitionFinished(windowContainerTransaction, (WindowContainerTransactionCallback) null);
    }

    public void forceFinishTransition() {
        Transitions.TransitionFinishCallback transitionFinishCallback = this.mFinishCallback;
        if (transitionFinishCallback != null) {
            transitionFinishCallback.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
            this.mFinishCallback = null;
            this.mFinishTransaction = null;
        }
    }

    public void onFixedRotationStarted() {
        if (this.mPipTransitionState.getTransitionState() == 4 && !this.mHasFadeOut) {
            fadeExistingPip(false);
        }
    }

    public final TransitionInfo.Change findCurrentPipTaskChange(TransitionInfo transitionInfo) {
        if (this.mCurrentPipTaskToken == null) {
            return null;
        }
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            if (this.mCurrentPipTaskToken.equals(change.getContainer())) {
                return change;
            }
        }
        return null;
    }

    public final TransitionInfo.Change findFixedRotationChange(TransitionInfo transitionInfo) {
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            if (change.getEndFixedRotation() != -1) {
                return change;
            }
        }
        return null;
    }

    public final void startExitAnimation(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback, TaskInfo taskInfo, TransitionInfo.Change change) {
        TransitionInfo.Change change2;
        int i;
        int i2;
        int i3;
        SurfaceControl.Transaction transaction3 = transaction;
        TaskInfo taskInfo2 = taskInfo;
        if (change == null) {
            int size = transitionInfo.getChanges().size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                change2 = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
                if (change2.getTaskInfo() != null || change2.getMode() != 6) {
                    TransitionInfo transitionInfo2 = transitionInfo;
                } else if (TransitionInfo.isIndependent(change2, transitionInfo)) {
                    break;
                }
                size--;
            }
        }
        TransitionInfo transitionInfo3 = transitionInfo;
        change2 = change;
        TransitionInfo.Change change3 = null;
        int i4 = 0;
        if (change2 == null) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1746143857, 0, (String) null, String.valueOf(TAG));
            }
            removePipImmediately(transitionInfo, transaction, transaction2, transitionFinishCallback, taskInfo);
            return;
        }
        this.mFinishCallback = new PipTransition$$ExternalSyntheticLambda0(this, taskInfo2, transitionFinishCallback);
        this.mFinishTransaction = transaction2;
        if (Transitions.SHELL_TRANSITIONS_ROTATION) {
            int size2 = transitionInfo.getChanges().size() - 1;
            while (true) {
                if (size2 < 0) {
                    break;
                }
                TransitionInfo.Change change4 = (TransitionInfo.Change) transitionInfo.getChanges().get(size2);
                if (change4.getMode() == 6 && (change4.getFlags() & 32) != 0 && change4.getStartRotation() != change4.getEndRotation()) {
                    change3 = change4;
                    break;
                }
                size2--;
            }
            if (change3 != null) {
                startExpandAndRotationAnimation(transitionInfo, transaction, transaction2, change3, taskInfo, change2);
                return;
            }
        }
        Rect rect = new Rect(change2.getEndAbsBounds());
        Point endRelOffset = change2.getEndRelOffset();
        rect.offset(-endRelOffset.x, -endRelOffset.y);
        transaction3.setWindowCrop(change2.getLeash(), rect);
        this.mSurfaceTransactionHelper.scale(transaction3, change2.getLeash(), rect, this.mPipBoundsState.getBounds());
        transaction.apply();
        if (this.mInFixedRotation) {
            int deltaRotation = RotationUtils.deltaRotation(change2.getStartRotation(), this.mEndFixedRotation);
            Rect rect2 = new Rect(rect);
            RotationUtils.rotateBounds(rect2, rect, deltaRotation);
            if (deltaRotation == 1) {
                i3 = 90;
                i2 = rect.right;
                i = rect.top;
            } else {
                i3 = -90;
                i2 = rect.left;
                i = rect.bottom;
            }
            this.mSurfaceTransactionHelper.rotateAndScaleWithCrop(transaction2, change2.getLeash(), rect2, rect2, new Rect(), (float) i3, (float) i2, (float) i, true, deltaRotation == 3);
            i4 = deltaRotation;
        }
        startExpandAnimation(taskInfo2, change2.getLeash(), rect, i4);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExitAnimation$0(TaskInfo taskInfo, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        this.mPipOrganizer.onExitPipFinished(taskInfo);
        transitionFinishCallback.onTransitionFinished(windowContainerTransaction, windowContainerTransactionCallback);
    }

    public final void startExpandAndRotationAnimation(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, TransitionInfo.Change change, TaskInfo taskInfo, TransitionInfo.Change change2) {
        int i;
        int i2;
        int i3;
        int deltaRotation = RotationUtils.deltaRotation(change.getStartRotation(), change.getEndRotation());
        CounterRotatorHelper counterRotatorHelper = new CounterRotatorHelper();
        counterRotatorHelper.handleClosingChanges(transitionInfo, transaction, change);
        Rect rect = new Rect(change2.getStartAbsBounds());
        RotationUtils.rotateBounds(rect, change.getStartAbsBounds(), deltaRotation);
        Rect rect2 = new Rect(change2.getEndAbsBounds());
        Point endRelOffset = change2.getEndRelOffset();
        rect.offset(-endRelOffset.x, -endRelOffset.y);
        rect2.offset(-endRelOffset.x, -endRelOffset.y);
        int deltaRotation2 = RotationUtils.deltaRotation(deltaRotation, 0);
        if (deltaRotation2 == 1) {
            i3 = 90;
            i2 = rect.right;
            i = rect.top;
        } else {
            i3 = -90;
            i2 = rect.left;
            i = rect.bottom;
        }
        float f = (float) i;
        Rect rect3 = rect;
        this.mSurfaceTransactionHelper.rotateAndScaleWithCrop(transaction, change2.getLeash(), rect2, rect3, new Rect(), (float) i3, (float) i2, f, true, deltaRotation2 == 3);
        transaction.apply();
        counterRotatorHelper.cleanUp(transaction2);
        this.mPipAnimationController.getAnimator(taskInfo, change2.getLeash(), rect, rect3, rect2, (Rect) null, 3, 0.0f, deltaRotation2).setTransitionDirection(3).setPipAnimationCallback(this.mPipAnimationCallback).setDuration((long) this.mEnterExitAnimationDuration).start();
    }

    public final void startExpandAnimation(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, int i) {
        this.mPipAnimationController.getAnimator(taskInfo, surfaceControl, this.mPipBoundsState.getBounds(), this.mPipBoundsState.getBounds(), rect, (Rect) null, 3, 0.0f, i).setTransitionDirection(3).setPipAnimationCallback(this.mPipAnimationCallback).setDuration((long) this.mEnterExitAnimationDuration).start();
    }

    public final void removePipImmediately(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback, TaskInfo taskInfo) {
        transaction.apply();
        transaction2.setWindowCrop(((TransitionInfo.Change) transitionInfo.getChanges().get(0)).getLeash(), this.mPipBoundsState.getDisplayBounds());
        this.mPipOrganizer.onExitPipFinished(taskInfo);
        transitionFinishCallback.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
    }

    public static boolean isEnteringPip(TransitionInfo transitionInfo, WindowContainerToken windowContainerToken) {
        int size = transitionInfo.getChanges().size() - 1;
        while (size >= 0) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            if (change.getTaskInfo() == null || change.getTaskInfo().getWindowingMode() != 2 || change.getContainer().equals(windowContainerToken)) {
                size--;
            } else if (transitionInfo.getType() == 10 || transitionInfo.getType() == 1 || transitionInfo.getType() == 6) {
                return true;
            } else {
                throw new IllegalStateException("Entering PIP with unexpected transition type=" + WindowManager.transitTypeToString(transitionInfo.getType()));
            }
        }
        return false;
    }

    public final boolean startEnterAnimation(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        TransitionInfo.Change change = null;
        TransitionInfo.Change change2 = null;
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change3 = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            if (change3.getTaskInfo() != null && change3.getTaskInfo().getWindowingMode() == 2) {
                change = change3;
            } else if ((change3.getFlags() & 2) != 0) {
                change2 = change3;
            }
        }
        if (change == null) {
            return false;
        }
        this.mCurrentPipTaskToken = change.getContainer();
        this.mHasFadeOut = false;
        if (this.mFinishCallback == null) {
            if (change2 != null) {
                transaction.show(change2.getLeash());
                transaction.setAlpha(change2.getLeash(), 1.0f);
            }
            for (int size2 = transitionInfo.getChanges().size() - 1; size2 >= 0; size2--) {
                TransitionInfo.Change change4 = (TransitionInfo.Change) transitionInfo.getChanges().get(size2);
                if (!(change4 == change || change4 == change2 || !Transitions.isOpeningType(change4.getMode()))) {
                    SurfaceControl leash = change4.getLeash();
                    transaction.show(leash).setAlpha(leash, 1.0f);
                }
            }
            this.mPipTransitionState.setTransitionState(3);
            this.mFinishCallback = transitionFinishCallback;
            this.mFinishTransaction = transaction2;
            return startEnterAnimation(change.getTaskInfo(), change.getLeash(), transaction, transaction2, change.getStartRotation(), this.mInFixedRotation ? this.mEndFixedRotation : change.getEndRotation());
        }
        callFinishCallback((WindowContainerTransaction) null);
        this.mFinishTransaction = null;
        throw new RuntimeException("Previous callback not called, aborting entering PIP.");
    }

    public final boolean startEnterAnimation(TaskInfo taskInfo, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, int i, int i2) {
        int i3;
        int i4;
        PipAnimationController.PipTransitionAnimator pipTransitionAnimator;
        TaskInfo taskInfo2 = taskInfo;
        SurfaceControl surfaceControl2 = surfaceControl;
        SurfaceControl.Transaction transaction3 = transaction;
        SurfaceControl.Transaction transaction4 = transaction2;
        setBoundsStateForEntry(taskInfo2.topActivity, taskInfo2.pictureInPictureParams, taskInfo2.topActivityInfo);
        Rect entryDestinationBounds = this.mPipBoundsAlgorithm.getEntryDestinationBounds();
        Rect bounds = taskInfo2.configuration.windowConfiguration.getBounds();
        int deltaRotation = RotationUtils.deltaRotation(i, i2);
        Rect validSourceHintRect = PipBoundsAlgorithm.getValidSourceHintRect(taskInfo2.pictureInPictureParams, bounds);
        if (deltaRotation != 0 && this.mInFixedRotation) {
            computeEnterPipRotatedBounds(deltaRotation, i, i2, taskInfo, entryDestinationBounds, validSourceHintRect);
        }
        this.mSurfaceTransactionHelper.crop(transaction4, surfaceControl2, entryDestinationBounds).round(transaction4, surfaceControl2, true);
        this.mPipMenuController.attach(surfaceControl2);
        PictureInPictureParams pictureInPictureParams = taskInfo2.pictureInPictureParams;
        if (pictureInPictureParams == null || !pictureInPictureParams.isAutoEnterEnabled() || !this.mPipTransitionState.getInSwipePipToHomeTransition()) {
            if (deltaRotation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate((float) deltaRotation);
                transaction3.setMatrix(surfaceControl2, matrix, new float[9]);
            }
            int i5 = this.mOneShotAnimationType;
            if (i5 == 0) {
                i3 = deltaRotation;
                SurfaceControl.Transaction transaction5 = transaction3;
                pipTransitionAnimator = this.mPipAnimationController.getAnimator(taskInfo, surfaceControl, bounds, bounds, entryDestinationBounds, validSourceHintRect, 2, 0.0f, i3);
                if (validSourceHintRect == null) {
                    pipTransitionAnimator.setColorContentOverlay(this.mContext);
                }
                i4 = 2;
            } else {
                i3 = deltaRotation;
                SurfaceControl.Transaction transaction6 = transaction3;
                if (i5 == 1) {
                    transaction6.setAlpha(surfaceControl2, 0.0f);
                    i4 = 2;
                    pipTransitionAnimator = this.mPipAnimationController.getAnimator(taskInfo, surfaceControl, entryDestinationBounds, 0.0f, 1.0f);
                    this.mOneShotAnimationType = 0;
                } else {
                    throw new RuntimeException("Unrecognized animation type: " + this.mOneShotAnimationType);
                }
            }
            transaction.apply();
            pipTransitionAnimator.setTransitionDirection(i4).setPipAnimationCallback(this.mPipAnimationCallback).setDuration((long) this.mEnterExitAnimationDuration);
            if (i3 != 0 && this.mInFixedRotation) {
                pipTransitionAnimator.setDestinationBounds(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
            }
            pipTransitionAnimator.start();
            return true;
        }
        this.mOneShotAnimationType = 0;
        SurfaceControl surfaceControl3 = this.mPipOrganizer.mSwipePipToHomeOverlay;
        transaction3.setMatrix(surfaceControl2, Matrix.IDENTITY_MATRIX, new float[9]).setPosition(surfaceControl2, (float) entryDestinationBounds.left, (float) entryDestinationBounds.top).setWindowCrop(surfaceControl2, entryDestinationBounds.width(), entryDestinationBounds.height());
        if (surfaceControl3 != null) {
            transaction3.reparent(surfaceControl3, surfaceControl2).setLayer(surfaceControl3, Integer.MAX_VALUE);
            this.mPipOrganizer.mSwipePipToHomeOverlay = null;
        }
        transaction.apply();
        if (deltaRotation != 0 && this.mInFixedRotation) {
            entryDestinationBounds.set(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
        }
        this.mPipBoundsState.setBounds(entryDestinationBounds);
        onFinishResize(taskInfo2, entryDestinationBounds, 2, (SurfaceControl.Transaction) null);
        sendOnPipTransitionFinished(2);
        if (surfaceControl3 != null) {
            this.mPipOrganizer.fadeOutAndRemoveOverlay(surfaceControl3, (Runnable) null, false);
        }
        this.mPipTransitionState.setInSwipePipToHomeTransition(false);
        return true;
    }

    public final void computeEnterPipRotatedBounds(int i, int i2, int i3, TaskInfo taskInfo, Rect rect, Rect rect2) {
        Rect rect3;
        this.mPipBoundsState.getDisplayLayout().rotateTo(this.mContext.getResources(), i3);
        Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
        rect.set(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
        RotationUtils.rotateBounds(rect, displayBounds, i3, i2);
        if (rect2 != null && (rect3 = taskInfo.displayCutoutInsets) != null && i == 3) {
            rect2.offset(rect3.left, rect3.top);
        }
    }

    public final void startExitToSplitAnimation(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback, TaskInfo taskInfo) {
        int size = transitionInfo.getChanges().size();
        if (size >= 4) {
            for (int i = size - 1; i >= 0; i--) {
                TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
                int mode = change.getMode();
                if ((mode != 6 || change.getParent() == null) && Transitions.isOpeningType(mode) && change.getParent() == null) {
                    SurfaceControl leash = change.getLeash();
                    Rect endAbsBounds = change.getEndAbsBounds();
                    transaction.show(leash).setAlpha(leash, 1.0f).setPosition(leash, (float) endAbsBounds.left, (float) endAbsBounds.top).setWindowCrop(leash, endAbsBounds.width(), endAbsBounds.height());
                }
            }
            this.mSplitScreenOptional.get().finishEnterSplitScreen(transaction);
            transaction.apply();
            this.mPipOrganizer.onExitPipFinished(taskInfo);
            transitionFinishCallback.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
            return;
        }
        throw new RuntimeException("Got an exit-pip-to-split transition with unexpected change-list");
    }

    public final void resetPrevPip(TransitionInfo.Change change, SurfaceControl.Transaction transaction) {
        SurfaceControl leash = change.getLeash();
        Rect endAbsBounds = change.getEndAbsBounds();
        Point endRelOffset = change.getEndRelOffset();
        endAbsBounds.offset(-endRelOffset.x, -endRelOffset.y);
        transaction.setWindowCrop(leash, (Rect) null);
        transaction.setMatrix(leash, 1.0f, 0.0f, 0.0f, 1.0f);
        transaction.setCornerRadius(leash, 0.0f);
        transaction.setPosition(leash, (float) endAbsBounds.left, (float) endAbsBounds.top);
        if (this.mHasFadeOut && change.getTaskInfo().isVisible()) {
            if (this.mPipAnimationController.getCurrentAnimator() != null) {
                this.mPipAnimationController.getCurrentAnimator().cancel();
            }
            transaction.setAlpha(leash, 1.0f);
        }
        this.mHasFadeOut = false;
        this.mCurrentPipTaskToken = null;
        this.mPipOrganizer.onExitPipFinished(change.getTaskInfo());
    }

    public final void updatePipForUnhandledTransition(TransitionInfo.Change change, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        SurfaceControl leash = change.getLeash();
        Rect bounds = this.mPipBoundsState.getBounds();
        boolean isInPip = this.mPipTransitionState.isInPip();
        this.mSurfaceTransactionHelper.crop(transaction, leash, bounds).round(transaction, leash, isInPip);
        this.mSurfaceTransactionHelper.crop(transaction2, leash, bounds).round(transaction2, leash, isInPip);
    }

    public final void fadeExistingPip(boolean z) {
        SurfaceControl surfaceControl = this.mPipOrganizer.getSurfaceControl();
        ActivityManager.RunningTaskInfo taskInfo = this.mPipOrganizer.getTaskInfo();
        if (surfaceControl != null && surfaceControl.isValid() && taskInfo != null) {
            this.mPipAnimationController.getAnimator(taskInfo, surfaceControl, this.mPipBoundsState.getBounds(), z ? 0.0f : 1.0f, z ? 1.0f : 0.0f).setTransitionDirection(1).setPipAnimationCallback(this.mPipAnimationCallback).setDuration((long) this.mEnterExitAnimationDuration).start();
            this.mHasFadeOut = !z;
        } else if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1714592462, 0, (String) null, String.valueOf(TAG), String.valueOf(surfaceControl));
        }
    }

    public final void finishResizeForMenu(Rect rect) {
        this.mPipMenuController.movePipMenu((SurfaceControl) null, (SurfaceControl.Transaction) null, rect);
        this.mPipMenuController.updateMenuBounds(rect);
    }

    public final void prepareFinishResizeTransaction(TaskInfo taskInfo, Rect rect, int i, WindowContainerTransaction windowContainerTransaction) {
        if (PipAnimationController.isInPipDirection(i)) {
            windowContainerTransaction.setActivityWindowingMode(taskInfo.token, 0);
            windowContainerTransaction.scheduleFinishEnterPip(taskInfo.token, rect);
        } else if (PipAnimationController.isOutPipDirection(i)) {
            if (i == 3) {
                rect = null;
            }
            windowContainerTransaction.setWindowingMode(taskInfo.token, getOutPipWindowingMode());
            windowContainerTransaction.setActivityWindowingMode(taskInfo.token, 0);
        } else {
            rect = null;
        }
        windowContainerTransaction.setBounds(taskInfo.token, rect);
    }
}
