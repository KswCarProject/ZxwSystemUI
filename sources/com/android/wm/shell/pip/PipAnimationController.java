package com.android.wm.shell.pip;

import android.animation.AnimationHandler;
import android.animation.Animator;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.app.TaskInfo;
import android.content.Context;
import android.graphics.Rect;
import android.util.RotationUtils;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.window.TaskSnapshot;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.pip.PipContentOverlay;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.transition.Transitions;
import java.util.Objects;

public class PipAnimationController {
    public PipTransitionAnimator mCurrentAnimator;
    public final ThreadLocal<AnimationHandler> mSfAnimationHandlerThreadLocal = ThreadLocal.withInitial(new PipAnimationController$$ExternalSyntheticLambda0());
    public final PipSurfaceTransactionHelper mSurfaceTransactionHelper;

    public static class PipAnimationCallback {
        public void onPipAnimationCancel(TaskInfo taskInfo, PipTransitionAnimator pipTransitionAnimator) {
            throw null;
        }

        public void onPipAnimationEnd(TaskInfo taskInfo, SurfaceControl.Transaction transaction, PipTransitionAnimator pipTransitionAnimator) {
            throw null;
        }

        public void onPipAnimationStart(TaskInfo taskInfo, PipTransitionAnimator pipTransitionAnimator) {
            throw null;
        }
    }

    public static class PipTransactionHandler {
        public boolean handlePipTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
            throw null;
        }
    }

    public static boolean isInPipDirection(int i) {
        return i == 2;
    }

    public static boolean isOutPipDirection(int i) {
        return i == 3 || i == 4;
    }

    public static boolean isRemovePipDirection(int i) {
        return i == 5;
    }

    public static /* synthetic */ AnimationHandler lambda$new$0() {
        AnimationHandler animationHandler = new AnimationHandler();
        animationHandler.setProvider(new SfVsyncFrameCallbackProvider());
        return animationHandler;
    }

    public PipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
    }

    @VisibleForTesting
    public PipTransitionAnimator getAnimator(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, float f, float f2) {
        PipTransitionAnimator pipTransitionAnimator = this.mCurrentAnimator;
        if (pipTransitionAnimator == null) {
            this.mCurrentAnimator = setupPipTransitionAnimator(PipTransitionAnimator.ofAlpha(taskInfo, surfaceControl, rect, f, f2));
        } else if (pipTransitionAnimator.getAnimationType() != 1 || !Objects.equals(rect, this.mCurrentAnimator.getDestinationBounds()) || !this.mCurrentAnimator.isRunning()) {
            this.mCurrentAnimator.cancel();
            this.mCurrentAnimator = setupPipTransitionAnimator(PipTransitionAnimator.ofAlpha(taskInfo, surfaceControl, rect, f, f2));
        } else {
            this.mCurrentAnimator.updateEndValue(Float.valueOf(f2));
        }
        return this.mCurrentAnimator;
    }

    @VisibleForTesting
    public PipTransitionAnimator getAnimator(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, Rect rect4, int i, float f, int i2) {
        Rect rect5 = rect3;
        PipTransitionAnimator pipTransitionAnimator = this.mCurrentAnimator;
        if (pipTransitionAnimator == null) {
            this.mCurrentAnimator = setupPipTransitionAnimator(PipTransitionAnimator.ofBounds(taskInfo, surfaceControl, rect2, rect2, rect3, rect4, i, 0.0f, i2));
        } else if (pipTransitionAnimator.getAnimationType() == 1 && this.mCurrentAnimator.isRunning()) {
            this.mCurrentAnimator.setDestinationBounds(rect3);
        } else if (this.mCurrentAnimator.getAnimationType() != 0 || !this.mCurrentAnimator.isRunning()) {
            this.mCurrentAnimator.cancel();
            this.mCurrentAnimator = setupPipTransitionAnimator(PipTransitionAnimator.ofBounds(taskInfo, surfaceControl, rect, rect2, rect3, rect4, i, f, i2));
        } else {
            this.mCurrentAnimator.setDestinationBounds(rect3);
            this.mCurrentAnimator.updateEndValue(new Rect(rect3));
        }
        return this.mCurrentAnimator;
    }

    public PipTransitionAnimator getCurrentAnimator() {
        return this.mCurrentAnimator;
    }

    public final PipTransitionAnimator setupPipTransitionAnimator(PipTransitionAnimator pipTransitionAnimator) {
        pipTransitionAnimator.setSurfaceTransactionHelper(this.mSurfaceTransactionHelper);
        pipTransitionAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        pipTransitionAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        pipTransitionAnimator.setAnimationHandler(this.mSfAnimationHandlerThreadLocal.get());
        return pipTransitionAnimator;
    }

    public static void quietCancel(ValueAnimator valueAnimator) {
        valueAnimator.removeAllUpdateListeners();
        valueAnimator.removeAllListeners();
        valueAnimator.cancel();
    }

    public static abstract class PipTransitionAnimator<T> extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        public final int mAnimationType;
        public T mBaseValue;
        public PipContentOverlay mContentOverlay;
        public T mCurrentValue;
        public final Rect mDestinationBounds;
        public T mEndValue;
        public final SurfaceControl mLeash;
        public PipAnimationCallback mPipAnimationCallback;
        public PipTransactionHandler mPipTransactionHandler;
        public T mStartValue;
        public PipSurfaceTransactionHelper.SurfaceControlTransactionFactory mSurfaceControlTransactionFactory;
        public PipSurfaceTransactionHelper mSurfaceTransactionHelper;
        public final TaskInfo mTaskInfo;
        public int mTransitionDirection;

        public abstract void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f);

        public void onAnimationRepeat(Animator animator) {
        }

        public void onEndTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i) {
        }

        public void onStartTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        }

        public PipTransitionAnimator(TaskInfo taskInfo, SurfaceControl surfaceControl, int i, Rect rect, T t, T t2, T t3) {
            Rect rect2 = new Rect();
            this.mDestinationBounds = rect2;
            this.mTaskInfo = taskInfo;
            this.mLeash = surfaceControl;
            this.mAnimationType = i;
            rect2.set(rect);
            this.mBaseValue = t;
            this.mStartValue = t2;
            this.mEndValue = t3;
            addListener(this);
            addUpdateListener(this);
            this.mSurfaceControlTransactionFactory = new PipAnimationController$PipTransitionAnimator$$ExternalSyntheticLambda0();
            this.mTransitionDirection = 0;
        }

        public void onAnimationStart(Animator animator) {
            this.mCurrentValue = this.mStartValue;
            onStartTransaction(this.mLeash, newSurfaceControlTransaction());
            PipAnimationCallback pipAnimationCallback = this.mPipAnimationCallback;
            if (pipAnimationCallback != null) {
                pipAnimationCallback.onPipAnimationStart(this.mTaskInfo, this);
            }
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            applySurfaceControlTransaction(this.mLeash, newSurfaceControlTransaction(), valueAnimator.getAnimatedFraction());
        }

        public void onAnimationEnd(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            onEndTransaction(this.mLeash, newSurfaceControlTransaction, this.mTransitionDirection);
            PipAnimationCallback pipAnimationCallback = this.mPipAnimationCallback;
            if (pipAnimationCallback != null) {
                pipAnimationCallback.onPipAnimationEnd(this.mTaskInfo, newSurfaceControlTransaction, this);
            }
            this.mTransitionDirection = 0;
        }

        public void onAnimationCancel(Animator animator) {
            PipAnimationCallback pipAnimationCallback = this.mPipAnimationCallback;
            if (pipAnimationCallback != null) {
                pipAnimationCallback.onPipAnimationCancel(this.mTaskInfo, this);
            }
            this.mTransitionDirection = 0;
        }

        @VisibleForTesting
        public int getAnimationType() {
            return this.mAnimationType;
        }

        @VisibleForTesting
        public PipTransitionAnimator<T> setPipAnimationCallback(PipAnimationCallback pipAnimationCallback) {
            this.mPipAnimationCallback = pipAnimationCallback;
            return this;
        }

        public PipTransitionAnimator<T> setPipTransactionHandler(PipTransactionHandler pipTransactionHandler) {
            this.mPipTransactionHandler = pipTransactionHandler;
            return this;
        }

        public boolean handlePipTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
            PipTransactionHandler pipTransactionHandler = this.mPipTransactionHandler;
            if (pipTransactionHandler != null) {
                return pipTransactionHandler.handlePipTransaction(surfaceControl, transaction, rect);
            }
            return false;
        }

        public SurfaceControl getContentOverlayLeash() {
            PipContentOverlay pipContentOverlay = this.mContentOverlay;
            if (pipContentOverlay == null) {
                return null;
            }
            return pipContentOverlay.mLeash;
        }

        public void setColorContentOverlay(Context context) {
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            PipContentOverlay pipContentOverlay = this.mContentOverlay;
            if (pipContentOverlay != null) {
                pipContentOverlay.detach(newSurfaceControlTransaction);
            }
            PipContentOverlay.PipColorOverlay pipColorOverlay = new PipContentOverlay.PipColorOverlay(context);
            this.mContentOverlay = pipColorOverlay;
            pipColorOverlay.attach(newSurfaceControlTransaction, this.mLeash);
        }

        public void setSnapshotContentOverlay(TaskSnapshot taskSnapshot, Rect rect) {
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            PipContentOverlay pipContentOverlay = this.mContentOverlay;
            if (pipContentOverlay != null) {
                pipContentOverlay.detach(newSurfaceControlTransaction);
            }
            PipContentOverlay.PipSnapshotOverlay pipSnapshotOverlay = new PipContentOverlay.PipSnapshotOverlay(taskSnapshot, rect);
            this.mContentOverlay = pipSnapshotOverlay;
            pipSnapshotOverlay.attach(newSurfaceControlTransaction, this.mLeash);
        }

        public void clearContentOverlay() {
            this.mContentOverlay = null;
        }

        @VisibleForTesting
        public int getTransitionDirection() {
            return this.mTransitionDirection;
        }

        @VisibleForTesting
        public PipTransitionAnimator<T> setTransitionDirection(int i) {
            if (i != 1) {
                this.mTransitionDirection = i;
            }
            return this;
        }

        public T getStartValue() {
            return this.mStartValue;
        }

        public T getBaseValue() {
            return this.mBaseValue;
        }

        @VisibleForTesting
        public T getEndValue() {
            return this.mEndValue;
        }

        public Rect getDestinationBounds() {
            return this.mDestinationBounds;
        }

        public void setDestinationBounds(Rect rect) {
            this.mDestinationBounds.set(rect);
            if (this.mAnimationType == 1) {
                onStartTransaction(this.mLeash, newSurfaceControlTransaction());
            }
        }

        public void setCurrentValue(T t) {
            this.mCurrentValue = t;
        }

        public boolean shouldApplyCornerRadius() {
            return !PipAnimationController.isOutPipDirection(this.mTransitionDirection);
        }

        public boolean shouldApplyShadowRadius() {
            return !PipAnimationController.isOutPipDirection(this.mTransitionDirection) && !PipAnimationController.isRemovePipDirection(this.mTransitionDirection);
        }

        public boolean inScaleTransition() {
            if (this.mAnimationType != 0) {
                return false;
            }
            int transitionDirection = getTransitionDirection();
            if (PipAnimationController.isInPipDirection(transitionDirection) || PipAnimationController.isOutPipDirection(transitionDirection)) {
                return false;
            }
            return true;
        }

        public void updateEndValue(T t) {
            this.mEndValue = t;
        }

        public SurfaceControl.Transaction newSurfaceControlTransaction() {
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            transaction.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
            return transaction;
        }

        @VisibleForTesting
        public void setSurfaceControlTransactionFactory(PipSurfaceTransactionHelper.SurfaceControlTransactionFactory surfaceControlTransactionFactory) {
            this.mSurfaceControlTransactionFactory = surfaceControlTransactionFactory;
        }

        public PipSurfaceTransactionHelper getSurfaceTransactionHelper() {
            return this.mSurfaceTransactionHelper;
        }

        public void setSurfaceTransactionHelper(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
            this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
        }

        public static PipTransitionAnimator<Float> ofAlpha(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, float f, float f2) {
            return new PipTransitionAnimator<Float>(taskInfo, surfaceControl, 1, rect, Float.valueOf(f), Float.valueOf(f), Float.valueOf(f2)) {
                public void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f) {
                    float floatValue = (((Float) getStartValue()).floatValue() * (1.0f - f)) + (((Float) getEndValue()).floatValue() * f);
                    setCurrentValue(Float.valueOf(floatValue));
                    getSurfaceTransactionHelper().alpha(transaction, surfaceControl, floatValue).round(transaction, surfaceControl, shouldApplyCornerRadius()).shadow(transaction, surfaceControl, shouldApplyShadowRadius());
                    transaction.apply();
                }

                public void onStartTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
                    if (getTransitionDirection() != 5) {
                        getSurfaceTransactionHelper().resetScale(transaction, surfaceControl, getDestinationBounds()).crop(transaction, surfaceControl, getDestinationBounds()).round(transaction, surfaceControl, shouldApplyCornerRadius()).shadow(transaction, surfaceControl, shouldApplyShadowRadius());
                        transaction.show(surfaceControl);
                        transaction.apply();
                    }
                }

                public void updateEndValue(Float f) {
                    super.updateEndValue(f);
                    this.mStartValue = this.mCurrentValue;
                }
            };
        }

        public static PipTransitionAnimator<Rect> ofBounds(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, Rect rect4, int i, float f, int i2) {
            Rect rect5;
            Rect rect6;
            Rect rect7;
            final Rect rect8;
            Rect rect9 = rect;
            Rect rect10 = rect3;
            Rect rect11 = rect4;
            int i3 = i2;
            final boolean isOutPipDirection = PipAnimationController.isOutPipDirection(i);
            boolean isInPipDirection = PipAnimationController.isInPipDirection(i);
            if (isOutPipDirection) {
                rect5 = new Rect(rect10);
            } else {
                rect5 = new Rect(rect9);
            }
            final Rect rect12 = rect5;
            if (i3 == 1 || i3 == 3) {
                Rect rect13 = new Rect(rect10);
                Rect rect14 = new Rect(rect10);
                RotationUtils.rotateBounds(rect14, rect12, i3);
                rect6 = rect13;
                rect8 = rect14;
                rect7 = isOutPipDirection ? rect14 : rect12;
            } else {
                rect8 = null;
                rect6 = null;
                rect7 = rect12;
            }
            final Rect rect15 = rect11 == null ? null : new Rect(rect11.left - rect7.left, rect11.top - rect7.top, rect7.right - rect11.right, rect7.bottom - rect11.bottom);
            final Rect rect16 = r1;
            Rect rect17 = new Rect(0, 0, 0, 0);
            Rect rect18 = r2;
            Rect rect19 = new Rect(rect9);
            Rect rect20 = r0;
            Rect rect21 = new Rect(rect2);
            Rect rect22 = r0;
            Rect rect23 = new Rect(rect10);
            Rect rect24 = rect7;
            final float f2 = f;
            final Rect rect25 = rect4;
            final boolean z = isInPipDirection;
            final Rect rect26 = rect24;
            final Rect rect27 = rect6;
            final Rect rect28 = rect3;
            final int i4 = i2;
            final int i5 = i;
            return new PipTransitionAnimator<Rect>(taskInfo, surfaceControl, 0, rect3, rect18, rect20, rect22) {
                public final RectEvaluator mInsetsEvaluator = new RectEvaluator(new Rect());
                public final RectEvaluator mRectEvaluator = new RectEvaluator(new Rect());

                public void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f) {
                    SurfaceControl surfaceControl2 = surfaceControl;
                    SurfaceControl.Transaction transaction2 = transaction;
                    float f2 = f;
                    Rect rect = (Rect) getBaseValue();
                    Rect rect2 = (Rect) getStartValue();
                    Rect rect3 = (Rect) getEndValue();
                    PipContentOverlay pipContentOverlay = this.mContentOverlay;
                    if (pipContentOverlay != null) {
                        pipContentOverlay.onAnimationUpdate(transaction, f2);
                    }
                    if (rect8 != null) {
                        applyRotation(transaction, surfaceControl, f, rect2, rect3);
                        return;
                    }
                    Rect evaluate = this.mRectEvaluator.evaluate(f2, rect2, rect3);
                    float f3 = (1.0f - f2) * f2;
                    setCurrentValue(evaluate);
                    if (!inScaleTransition() && rect25 != null) {
                        Rect computeInsets = computeInsets(f2);
                        getSurfaceTransactionHelper().scaleAndCrop(transaction, surfaceControl, rect25, rect12, evaluate, computeInsets, z);
                        if (shouldApplyCornerRadius()) {
                            Rect rect4 = new Rect(rect26);
                            rect4.inset(computeInsets);
                            getSurfaceTransactionHelper().round(transaction, surfaceControl, rect4, evaluate).shadow(transaction, surfaceControl, shouldApplyShadowRadius());
                        }
                    } else if (isOutPipDirection) {
                        getSurfaceTransactionHelper().crop(transaction, surfaceControl, rect3).scale(transaction, surfaceControl, rect3, evaluate);
                    } else {
                        getSurfaceTransactionHelper().crop(transaction, surfaceControl, rect).scale(transaction, surfaceControl, rect, evaluate, f3).round(transaction, surfaceControl, rect, evaluate).shadow(transaction, surfaceControl, shouldApplyShadowRadius());
                    }
                    if (!handlePipTransaction(surfaceControl, transaction, evaluate)) {
                        transaction.apply();
                    }
                }

                public final void applyRotation(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, Rect rect, Rect rect2) {
                    float f2;
                    float f3;
                    float f4;
                    float f5;
                    float f6;
                    int i;
                    int i2;
                    SurfaceControl.Transaction transaction2 = transaction;
                    SurfaceControl surfaceControl2 = surfaceControl;
                    float f7 = f;
                    Rect rect3 = rect;
                    Rect rect4 = rect2;
                    if (!rect4.equals(rect27)) {
                        rect8.set(rect28);
                        RotationUtils.rotateBounds(rect8, rect12, i4);
                        rect27.set(rect4);
                    }
                    Rect evaluate = this.mRectEvaluator.evaluate(f7, rect3, rect8);
                    setCurrentValue(evaluate);
                    Rect computeInsets = computeInsets(f7);
                    if (!Transitions.SHELL_TRANSITIONS_ROTATION) {
                        if (i4 == 1) {
                            f6 = f7 * 90.0f;
                            int i3 = rect4.right;
                            int i4 = rect3.left;
                            f5 = (((float) (i3 - i4)) * f7) + ((float) i4);
                            i = rect4.top;
                            i2 = rect3.top;
                        } else {
                            f6 = f7 * -90.0f;
                            int i5 = rect4.left;
                            int i6 = rect3.left;
                            f5 = (((float) (i5 - i6)) * f7) + ((float) i6);
                            i = rect4.bottom;
                            i2 = rect3.top;
                        }
                        f2 = (f7 * ((float) (i - i2))) + ((float) i2);
                        f4 = f6;
                        f3 = f5;
                    } else if (i4 == 1) {
                        float f8 = 1.0f - f7;
                        float f9 = 90.0f * f8;
                        int i7 = rect4.left;
                        int i8 = rect3.left;
                        int i9 = rect4.top;
                        int i10 = rect3.top;
                        f2 = (f7 * ((float) (i9 - i10))) + ((float) i10);
                        f3 = (((float) (i7 - i8)) * f7) + ((float) i8) + (((float) rect.width()) * f8);
                        f4 = f9;
                    } else {
                        float f10 = 1.0f - f7;
                        f4 = -90.0f * f10;
                        int i11 = rect4.left;
                        int i12 = rect3.left;
                        float f11 = (((float) (i11 - i12)) * f7) + ((float) i12);
                        int i13 = rect4.top;
                        int i14 = rect3.top;
                        f2 = (f7 * ((float) (i13 - i14))) + ((float) i14) + (((float) rect.height()) * f10);
                        f3 = f11;
                    }
                    Rect rect5 = new Rect(rect26);
                    rect5.inset(computeInsets);
                    getSurfaceTransactionHelper().rotateAndScaleWithCrop(transaction, surfaceControl, rect26, evaluate, computeInsets, f4, f3, f2, isOutPipDirection, i4 == 3);
                    if (shouldApplyCornerRadius()) {
                        getSurfaceTransactionHelper().round(transaction2, surfaceControl2, rect5, evaluate).shadow(transaction2, surfaceControl2, shouldApplyShadowRadius());
                    }
                    transaction.apply();
                }

                public final Rect computeInsets(float f) {
                    Rect rect = rect15;
                    if (rect == null) {
                        return rect16;
                    }
                    boolean z = isOutPipDirection;
                    Rect rect2 = z ? rect : rect16;
                    if (z) {
                        rect = rect16;
                    }
                    return this.mInsetsEvaluator.evaluate(f, rect2, rect);
                }

                public void onStartTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
                    getSurfaceTransactionHelper().alpha(transaction, surfaceControl, 1.0f).round(transaction, surfaceControl, shouldApplyCornerRadius()).shadow(transaction, surfaceControl, shouldApplyShadowRadius());
                    if (PipAnimationController.isInPipDirection(i5)) {
                        transaction.setWindowCrop(surfaceControl, (Rect) getStartValue());
                    }
                    transaction.show(surfaceControl);
                    transaction.apply();
                }

                public void onEndTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i) {
                    Rect destinationBounds = getDestinationBounds();
                    getSurfaceTransactionHelper().resetScale(transaction, surfaceControl, destinationBounds);
                    if (PipAnimationController.isOutPipDirection(i)) {
                        transaction.setMatrix(surfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
                        transaction.setPosition(surfaceControl, 0.0f, 0.0f);
                        transaction.setWindowCrop(surfaceControl, 0, 0);
                    } else {
                        getSurfaceTransactionHelper().crop(transaction, surfaceControl, destinationBounds);
                    }
                    PipContentOverlay pipContentOverlay = this.mContentOverlay;
                    if (pipContentOverlay != null) {
                        pipContentOverlay.onAnimationEnd(transaction, destinationBounds);
                    }
                }

                public void updateEndValue(Rect rect) {
                    T t;
                    super.updateEndValue(rect);
                    T t2 = this.mStartValue;
                    if (t2 != null && (t = this.mCurrentValue) != null) {
                        ((Rect) t2).set((Rect) t);
                    }
                }
            };
        }
    }
}
