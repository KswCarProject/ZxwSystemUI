package com.android.wm.shell.pip;

import android.app.PictureInPictureParams;
import android.app.TaskInfo;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceControl;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.List;

public abstract class PipTransitionController implements Transitions.TransitionHandler {
    public final Handler mMainHandler;
    public final PipAnimationController.PipAnimationCallback mPipAnimationCallback = new PipAnimationController.PipAnimationCallback() {
        public void onPipAnimationStart(TaskInfo taskInfo, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            PipTransitionController.this.sendOnPipTransitionStarted(pipTransitionAnimator.getTransitionDirection());
        }

        public void onPipAnimationEnd(TaskInfo taskInfo, SurfaceControl.Transaction transaction, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            int transitionDirection = pipTransitionAnimator.getTransitionDirection();
            PipTransitionController.this.mPipBoundsState.setBounds(pipTransitionAnimator.getDestinationBounds());
            if (transitionDirection != 5) {
                if (PipAnimationController.isInPipDirection(transitionDirection) && pipTransitionAnimator.getContentOverlayLeash() != null) {
                    PipTransitionController.this.mPipOrganizer.fadeOutAndRemoveOverlay(pipTransitionAnimator.getContentOverlayLeash(), new PipTaskOrganizer$$ExternalSyntheticLambda4(pipTransitionAnimator), true);
                }
                PipTransitionController.this.onFinishResize(taskInfo, pipTransitionAnimator.getDestinationBounds(), transitionDirection, transaction);
                PipTransitionController.this.sendOnPipTransitionFinished(transitionDirection);
            }
        }

        public void onPipAnimationCancel(TaskInfo taskInfo, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            if (PipAnimationController.isInPipDirection(pipTransitionAnimator.getTransitionDirection()) && pipTransitionAnimator.getContentOverlayLeash() != null) {
                PipTransitionController.this.mPipOrganizer.fadeOutAndRemoveOverlay(pipTransitionAnimator.getContentOverlayLeash(), new PipTaskOrganizer$$ExternalSyntheticLambda4(pipTransitionAnimator), true);
            }
            PipTransitionController.this.sendOnPipTransitionCancelled(pipTransitionAnimator.getTransitionDirection());
        }
    };
    public final PipAnimationController mPipAnimationController;
    public final PipBoundsAlgorithm mPipBoundsAlgorithm;
    public final PipBoundsState mPipBoundsState;
    public final PipMenuController mPipMenuController;
    public PipTaskOrganizer mPipOrganizer;
    public final List<PipTransitionCallback> mPipTransitionCallbacks = new ArrayList();
    public final ShellTaskOrganizer mShellTaskOrganizer;
    public final Transitions mTransitions;

    public interface PipTransitionCallback {
        void onPipTransitionCanceled(int i);

        void onPipTransitionFinished(int i);

        void onPipTransitionStarted(int i, Rect rect);
    }

    public void forceFinishTransition() {
    }

    public int getOutPipWindowingMode() {
        return 0;
    }

    public boolean handleRotateDisplay(int i, int i2, WindowContainerTransaction windowContainerTransaction) {
        return false;
    }

    public void onFinishResize(TaskInfo taskInfo, Rect rect, int i, SurfaceControl.Transaction transaction) {
    }

    public void onFixedRotationStarted() {
    }

    public void setIsFullAnimation(boolean z) {
    }

    public void startExitTransition(int i, WindowContainerTransaction windowContainerTransaction, Rect rect) {
    }

    public PipTransitionController(PipBoundsState pipBoundsState, PipMenuController pipMenuController, PipBoundsAlgorithm pipBoundsAlgorithm, PipAnimationController pipAnimationController, Transitions transitions, ShellTaskOrganizer shellTaskOrganizer) {
        this.mPipBoundsState = pipBoundsState;
        this.mPipMenuController = pipMenuController;
        this.mShellTaskOrganizer = shellTaskOrganizer;
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipAnimationController = pipAnimationController;
        this.mTransitions = transitions;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            transitions.addHandler(this);
        }
    }

    public void setPipOrganizer(PipTaskOrganizer pipTaskOrganizer) {
        this.mPipOrganizer = pipTaskOrganizer;
    }

    public void registerPipTransitionCallback(PipTransitionCallback pipTransitionCallback) {
        this.mPipTransitionCallbacks.add(pipTransitionCallback);
    }

    public void sendOnPipTransitionStarted(int i) {
        Rect bounds = this.mPipBoundsState.getBounds();
        for (int size = this.mPipTransitionCallbacks.size() - 1; size >= 0; size--) {
            this.mPipTransitionCallbacks.get(size).onPipTransitionStarted(i, bounds);
        }
    }

    public void sendOnPipTransitionFinished(int i) {
        for (int size = this.mPipTransitionCallbacks.size() - 1; size >= 0; size--) {
            this.mPipTransitionCallbacks.get(size).onPipTransitionFinished(i);
        }
    }

    public void sendOnPipTransitionCancelled(int i) {
        for (int size = this.mPipTransitionCallbacks.size() - 1; size >= 0; size--) {
            this.mPipTransitionCallbacks.get(size).onPipTransitionCanceled(i);
        }
    }

    public void setBoundsStateForEntry(ComponentName componentName, PictureInPictureParams pictureInPictureParams, ActivityInfo activityInfo) {
        this.mPipBoundsState.setBoundsStateForEntry(componentName, activityInfo, pictureInPictureParams, this.mPipBoundsAlgorithm);
    }
}
