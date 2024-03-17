package com.android.wm.shell.splitscreen;

import android.animation.RectEvaluator;
import android.animation.TypeEvaluator;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.SurfaceControl;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldBackgroundController;
import java.util.concurrent.Executor;

public class StageTaskUnfoldController implements ShellUnfoldProgressProvider.UnfoldListener, DisplayInsetsController.OnInsetsChangedListener {
    public static final TypeEvaluator<Rect> RECT_EVALUATOR = new RectEvaluator(new Rect());
    public final SparseArray<AnimationContext> mAnimationContextByTaskId = new SparseArray<>();
    public final UnfoldBackgroundController mBackgroundController;
    public boolean mBothStagesVisible;
    public final DisplayInsetsController mDisplayInsetsController;
    public final Executor mExecutor;
    public final int mExpandedTaskBarHeight;
    public final Rect mStageBounds = new Rect();
    public InsetsSource mTaskbarInsetsSource;
    public final TransactionPool mTransactionPool;
    public final ShellUnfoldProgressProvider mUnfoldProgressProvider;
    public final float mWindowCornerRadiusPx;

    public StageTaskUnfoldController(Context context, TransactionPool transactionPool, ShellUnfoldProgressProvider shellUnfoldProgressProvider, DisplayInsetsController displayInsetsController, UnfoldBackgroundController unfoldBackgroundController, Executor executor) {
        this.mUnfoldProgressProvider = shellUnfoldProgressProvider;
        this.mTransactionPool = transactionPool;
        this.mExecutor = executor;
        this.mBackgroundController = unfoldBackgroundController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mWindowCornerRadiusPx = ScreenDecorationsUtils.getWindowCornerRadius(context);
        this.mExpandedTaskBarHeight = context.getResources().getDimensionPixelSize(17105564);
    }

    public void init() {
        this.mUnfoldProgressProvider.addListener(this.mExecutor, this);
        this.mDisplayInsetsController.addInsetsChangedListener(0, this);
    }

    public void insetsChanged(InsetsState insetsState) {
        this.mTaskbarInsetsSource = insetsState.getSource(21);
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            this.mAnimationContextByTaskId.valueAt(size).update();
        }
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (runningTaskInfo.hasParentTask()) {
            this.mAnimationContextByTaskId.put(runningTaskInfo.taskId, new AnimationContext(surfaceControl));
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (runningTaskInfo.hasParentTask()) {
            AnimationContext animationContext = this.mAnimationContextByTaskId.get(runningTaskInfo.taskId);
            if (animationContext != null) {
                SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
                resetSurface(acquire, animationContext);
                acquire.apply();
                this.mTransactionPool.release(acquire);
            }
            this.mAnimationContextByTaskId.remove(runningTaskInfo.taskId);
        }
    }

    public void onStateChangeProgress(float f) {
        if (this.mAnimationContextByTaskId.size() != 0 && this.mBothStagesVisible) {
            SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
            this.mBackgroundController.ensureBackground(acquire);
            for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
                AnimationContext valueAt = this.mAnimationContextByTaskId.valueAt(size);
                valueAt.mCurrentCropRect.set(RECT_EVALUATOR.evaluate(f, valueAt.mStartCropRect, valueAt.mEndCropRect));
                acquire.setWindowCrop(valueAt.mLeash, valueAt.mCurrentCropRect).setCornerRadius(valueAt.mLeash, this.mWindowCornerRadiusPx);
            }
            acquire.apply();
            this.mTransactionPool.release(acquire);
        }
    }

    public void onStateChangeFinished() {
        resetTransformations();
    }

    public void onSplitVisibilityChanged(boolean z) {
        this.mBothStagesVisible = z;
        if (!z) {
            resetTransformations();
        }
    }

    public void onLayoutChanged(Rect rect, int i, boolean z) {
        this.mStageBounds.set(rect);
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            this.mAnimationContextByTaskId.valueAt(size).update(i, z);
        }
    }

    public final void resetTransformations() {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            resetSurface(acquire, this.mAnimationContextByTaskId.valueAt(size));
        }
        this.mBackgroundController.removeBackground(acquire);
        acquire.apply();
        this.mTransactionPool.release(acquire);
    }

    public final void resetSurface(SurfaceControl.Transaction transaction, AnimationContext animationContext) {
        transaction.setWindowCrop(animationContext.mLeash, (Rect) null).setCornerRadius(animationContext.mLeash, 0.0f);
    }

    public class AnimationContext {
        public final Rect mCurrentCropRect;
        public final Rect mEndCropRect;
        public boolean mIsLandscape;
        public final SurfaceControl mLeash;
        public int mSplitPosition;
        public final Rect mStartCropRect;

        public AnimationContext(SurfaceControl surfaceControl) {
            this.mStartCropRect = new Rect();
            this.mEndCropRect = new Rect();
            this.mCurrentCropRect = new Rect();
            this.mSplitPosition = -1;
            this.mIsLandscape = false;
            this.mLeash = surfaceControl;
            update();
        }

        public final void update(int i, boolean z) {
            this.mSplitPosition = i;
            this.mIsLandscape = z;
            update();
        }

        public final void update() {
            Insets insets;
            this.mStartCropRect.set(StageTaskUnfoldController.this.mStageBounds);
            boolean isTaskbarExpanded = isTaskbarExpanded();
            if (isTaskbarExpanded) {
                this.mStartCropRect.inset(StageTaskUnfoldController.this.mTaskbarInsetsSource.calculateVisibleInsets(this.mStartCropRect));
            }
            this.mStartCropRect.offsetTo(0, 0);
            this.mEndCropRect.set(this.mStartCropRect);
            int max = (int) (((float) Math.max(this.mEndCropRect.width(), this.mEndCropRect.height())) * 0.05f);
            if (this.mIsLandscape) {
                insets = getLandscapeMargins(max, isTaskbarExpanded);
            } else {
                insets = getPortraitMargins(max, isTaskbarExpanded);
            }
            this.mStartCropRect.inset(insets);
        }

        public final Insets getLandscapeMargins(int i, boolean z) {
            int i2;
            int i3 = 0;
            int i4 = z ? 0 : i;
            if (this.mSplitPosition == 0) {
                i2 = 0;
                i3 = i;
            } else {
                i2 = i;
            }
            return Insets.of(i3, i, i2, i4);
        }

        public final Insets getPortraitMargins(int i, boolean z) {
            int i2;
            int i3 = 0;
            if (this.mSplitPosition == 0) {
                i2 = 0;
                i3 = i;
            } else {
                i2 = z ? 0 : i;
            }
            return Insets.of(i, i3, i, i2);
        }

        public final boolean isTaskbarExpanded() {
            return StageTaskUnfoldController.this.mTaskbarInsetsSource != null && StageTaskUnfoldController.this.mTaskbarInsetsSource.getFrame().height() >= StageTaskUnfoldController.this.mExpandedTaskBarHeight;
        }
    }
}
