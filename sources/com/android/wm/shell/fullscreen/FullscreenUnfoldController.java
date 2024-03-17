package com.android.wm.shell.fullscreen;

import android.animation.RectEvaluator;
import android.animation.TypeEvaluator;
import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.MathUtils;
import android.util.SparseArray;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.SurfaceControl;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldBackgroundController;
import java.util.concurrent.Executor;

public final class FullscreenUnfoldController implements ShellUnfoldProgressProvider.UnfoldListener, DisplayInsetsController.OnInsetsChangedListener {
    public static final float[] FLOAT_9 = new float[9];
    public static final TypeEvaluator<Rect> RECT_EVALUATOR = new RectEvaluator(new Rect());
    public final SparseArray<AnimationContext> mAnimationContextByTaskId = new SparseArray<>();
    public final UnfoldBackgroundController mBackgroundController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final Executor mExecutor;
    public final float mExpandedTaskBarHeight;
    public final ShellUnfoldProgressProvider mProgressProvider;
    public InsetsSource mTaskbarInsetsSource;
    public final SurfaceControl.Transaction mTransaction = new SurfaceControl.Transaction();
    public final float mWindowCornerRadiusPx;

    public FullscreenUnfoldController(Context context, Executor executor, UnfoldBackgroundController unfoldBackgroundController, ShellUnfoldProgressProvider shellUnfoldProgressProvider, DisplayInsetsController displayInsetsController) {
        this.mExecutor = executor;
        this.mProgressProvider = shellUnfoldProgressProvider;
        this.mDisplayInsetsController = displayInsetsController;
        this.mWindowCornerRadiusPx = ScreenDecorationsUtils.getWindowCornerRadius(context);
        this.mExpandedTaskBarHeight = (float) context.getResources().getDimensionPixelSize(17105564);
        this.mBackgroundController = unfoldBackgroundController;
    }

    public void init() {
        this.mProgressProvider.addListener(this.mExecutor, this);
        this.mDisplayInsetsController.addInsetsChangedListener(0, this);
    }

    public void onStateChangeProgress(float f) {
        if (this.mAnimationContextByTaskId.size() != 0) {
            this.mBackgroundController.ensureBackground(this.mTransaction);
            for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
                AnimationContext valueAt = this.mAnimationContextByTaskId.valueAt(size);
                valueAt.mCurrentCropRect.set(RECT_EVALUATOR.evaluate(f, valueAt.mStartCropRect, valueAt.mEndCropRect));
                float lerp = MathUtils.lerp(0.94f, 1.0f, f);
                valueAt.mMatrix.setScale(lerp, lerp, valueAt.mCurrentCropRect.exactCenterX(), valueAt.mCurrentCropRect.exactCenterY());
                this.mTransaction.setWindowCrop(valueAt.mLeash, valueAt.mCurrentCropRect).setMatrix(valueAt.mLeash, valueAt.mMatrix, FLOAT_9).setCornerRadius(valueAt.mLeash, this.mWindowCornerRadiusPx);
            }
            this.mTransaction.apply();
        }
    }

    public void onStateChangeFinished() {
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            resetSurface(this.mAnimationContextByTaskId.valueAt(size));
        }
        this.mBackgroundController.removeBackground(this.mTransaction);
        this.mTransaction.apply();
    }

    public void insetsChanged(InsetsState insetsState) {
        this.mTaskbarInsetsSource = insetsState.getSource(21);
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            AnimationContext valueAt = this.mAnimationContextByTaskId.valueAt(size);
            valueAt.update(this.mTaskbarInsetsSource, valueAt.mTaskInfo);
        }
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        this.mAnimationContextByTaskId.put(runningTaskInfo.taskId, new AnimationContext(surfaceControl, this.mTaskbarInsetsSource, runningTaskInfo));
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        AnimationContext animationContext = this.mAnimationContextByTaskId.get(runningTaskInfo.taskId);
        if (animationContext != null) {
            animationContext.update(this.mTaskbarInsetsSource, runningTaskInfo);
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        AnimationContext animationContext = this.mAnimationContextByTaskId.get(runningTaskInfo.taskId);
        if (animationContext != null) {
            if (runningTaskInfo.getWindowingMode() != 2) {
                resetSurface(animationContext);
            }
            this.mAnimationContextByTaskId.remove(runningTaskInfo.taskId);
        }
        if (this.mAnimationContextByTaskId.size() == 0) {
            this.mBackgroundController.removeBackground(this.mTransaction);
        }
        this.mTransaction.apply();
    }

    public final void resetSurface(AnimationContext animationContext) {
        SurfaceControl.Transaction matrix = this.mTransaction.setWindowCrop(animationContext.mLeash, (Rect) null).setCornerRadius(animationContext.mLeash, 0.0f).setMatrix(animationContext.mLeash, 1.0f, 0.0f, 0.0f, 1.0f);
        SurfaceControl surfaceControl = animationContext.mLeash;
        Point point = animationContext.mTaskInfo.positionInParent;
        matrix.setPosition(surfaceControl, (float) point.x, (float) point.y);
    }

    public class AnimationContext {
        public final Rect mCurrentCropRect;
        public final Rect mEndCropRect;
        public final SurfaceControl mLeash;
        public final Matrix mMatrix;
        public final Rect mStartCropRect;
        public TaskInfo mTaskInfo;

        public AnimationContext(SurfaceControl surfaceControl, InsetsSource insetsSource, TaskInfo taskInfo) {
            this.mStartCropRect = new Rect();
            this.mEndCropRect = new Rect();
            this.mCurrentCropRect = new Rect();
            this.mMatrix = new Matrix();
            this.mLeash = surfaceControl;
            update(insetsSource, taskInfo);
        }

        public final void update(InsetsSource insetsSource, TaskInfo taskInfo) {
            this.mTaskInfo = taskInfo;
            this.mStartCropRect.set(taskInfo.getConfiguration().windowConfiguration.getBounds());
            if (insetsSource != null && ((float) insetsSource.getFrame().height()) >= FullscreenUnfoldController.this.mExpandedTaskBarHeight) {
                Rect rect = this.mStartCropRect;
                rect.inset(insetsSource.calculateVisibleInsets(rect));
            }
            this.mEndCropRect.set(this.mStartCropRect);
            int width = (int) (((float) this.mEndCropRect.width()) * 0.08f);
            Rect rect2 = this.mStartCropRect;
            Rect rect3 = this.mEndCropRect;
            rect2.left = rect3.left + width;
            rect2.right = rect3.right - width;
            int height = (int) (((float) rect3.height()) * 0.03f);
            Rect rect4 = this.mStartCropRect;
            Rect rect5 = this.mEndCropRect;
            rect4.top = rect5.top + height;
            rect4.bottom = rect5.bottom - height;
        }
    }
}
