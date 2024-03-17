package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Looper;
import android.util.ArrayMap;
import android.view.Choreographer;
import androidx.dynamicanimation.animation.FrameCallbackScheduler;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.FloatProperties;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import com.android.wm.shell.pip.PipAppOpsListener;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class PipMotionHelper implements PipAppOpsListener.Callback, FloatingContentCoordinator.FloatingContent {
    public final PhysicsAnimator.SpringConfig mAnimateToDismissSpringConfig = new PhysicsAnimator.SpringConfig(1500.0f, 1.0f);
    public final PhysicsAnimator.SpringConfig mCatchUpSpringConfig = new PhysicsAnimator.SpringConfig(5000.0f, 1.0f);
    public final PhysicsAnimator.SpringConfig mConflictResolutionSpringConfig = new PhysicsAnimator.SpringConfig(200.0f, 1.0f);
    public final Context mContext;
    public boolean mDismissalPending = false;
    public PhysicsAnimator.FlingConfig mFlingConfigX;
    public PhysicsAnimator.FlingConfig mFlingConfigY;
    public final Rect mFloatingAllowedArea = new Rect();
    public FloatingContentCoordinator mFloatingContentCoordinator;
    public MagnetizedObject<Rect> mMagnetizedPip;
    public PhonePipMenuController mMenuController;
    public PipBoundsState mPipBoundsState;
    public final PipTaskOrganizer mPipTaskOrganizer;
    public final PipTransitionController.PipTransitionCallback mPipTransitionCallback;
    public Runnable mPostPipTransitionCallback;
    public final PhysicsAnimator.UpdateListener<Rect> mResizePipUpdateListener;
    public ThreadLocal<FrameCallbackScheduler> mSfSchedulerThreadLocal = ThreadLocal.withInitial(new PipMotionHelper$$ExternalSyntheticLambda0(this));
    public PipSnapAlgorithm mSnapAlgorithm;
    public final PhysicsAnimator.SpringConfig mSpringConfig = new PhysicsAnimator.SpringConfig(700.0f, 1.0f);
    public boolean mSpringingToTouch = false;
    public PhysicsAnimator.FlingConfig mStashConfigX;
    public PhysicsAnimator<Rect> mTemporaryBoundsPhysicsAnimator;
    public final Consumer<Rect> mUpdateBoundsCallback = new PipMotionHelper$$ExternalSyntheticLambda1(this);

    /* access modifiers changed from: private */
    public /* synthetic */ FrameCallbackScheduler lambda$new$0() {
        final Looper myLooper = Looper.myLooper();
        return new FrameCallbackScheduler() {
            public void postFrameCallback(Runnable runnable) {
                Choreographer.getSfInstance().postFrameCallback(new PipMotionHelper$1$$ExternalSyntheticLambda0(runnable));
            }

            public boolean isCurrentThread() {
                return Looper.myLooper() == myLooper;
            }
        };
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Rect rect) {
        if (!this.mPipBoundsState.getBounds().equals(rect)) {
            this.mMenuController.updateMenuLayout(rect);
            this.mPipBoundsState.setBounds(rect);
        }
    }

    public PipMotionHelper(Context context, PipBoundsState pipBoundsState, PipTaskOrganizer pipTaskOrganizer, PhonePipMenuController phonePipMenuController, PipSnapAlgorithm pipSnapAlgorithm, PipTransitionController pipTransitionController, FloatingContentCoordinator floatingContentCoordinator) {
        AnonymousClass2 r0 = new PipTransitionController.PipTransitionCallback() {
            public void onPipTransitionCanceled(int i) {
            }

            public void onPipTransitionStarted(int i, Rect rect) {
            }

            public void onPipTransitionFinished(int i) {
                if (PipMotionHelper.this.mPostPipTransitionCallback != null) {
                    PipMotionHelper.this.mPostPipTransitionCallback.run();
                    PipMotionHelper.this.mPostPipTransitionCallback = null;
                }
            }
        };
        this.mPipTransitionCallback = r0;
        this.mContext = context;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        this.mPipBoundsState = pipBoundsState;
        this.mMenuController = phonePipMenuController;
        this.mSnapAlgorithm = pipSnapAlgorithm;
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        pipTransitionController.registerPipTransitionCallback(r0);
        this.mResizePipUpdateListener = new PipMotionHelper$$ExternalSyntheticLambda2(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Rect rect, ArrayMap arrayMap) {
        if (this.mPipBoundsState.getMotionBoundsState().isInMotion()) {
            this.mPipTaskOrganizer.scheduleUserResizePip(getBounds(), this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion(), (Consumer<Rect>) null);
        }
    }

    public void init() {
        PhysicsAnimator<Rect> instance = PhysicsAnimator.getInstance(this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion());
        this.mTemporaryBoundsPhysicsAnimator = instance;
        instance.setCustomScheduler(this.mSfSchedulerThreadLocal.get());
    }

    public Rect getFloatingBoundsOnScreen() {
        return !this.mPipBoundsState.getMotionBoundsState().getAnimatingToBounds().isEmpty() ? this.mPipBoundsState.getMotionBoundsState().getAnimatingToBounds() : getBounds();
    }

    public Rect getAllowedFloatingBoundsRegion() {
        return this.mFloatingAllowedArea;
    }

    public void moveToBounds(Rect rect) {
        animateToBounds(rect, this.mConflictResolutionSpringConfig);
    }

    public void synchronizePinnedStackBounds() {
        cancelPhysicsAnimation();
        this.mPipBoundsState.getMotionBoundsState().onAllAnimationsEnded();
        if (this.mPipTaskOrganizer.isInPip()) {
            this.mFloatingContentCoordinator.onContentMoved(this);
        }
    }

    public void movePip(Rect rect) {
        movePip(rect, false);
    }

    public void movePip(Rect rect, boolean z) {
        if (!z) {
            this.mFloatingContentCoordinator.onContentMoved(this);
        }
        if (!this.mSpringingToTouch) {
            cancelPhysicsAnimation();
            if (!z) {
                resizePipUnchecked(rect);
                this.mPipBoundsState.setBounds(rect);
                return;
            }
            this.mPipBoundsState.getMotionBoundsState().setBoundsInMotion(rect);
            this.mPipTaskOrganizer.scheduleUserResizePip(getBounds(), rect, new PipMotionHelper$$ExternalSyntheticLambda3(this));
            return;
        }
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_WIDTH, (float) getBounds().width(), this.mCatchUpSpringConfig).spring(FloatProperties.RECT_HEIGHT, (float) getBounds().height(), this.mCatchUpSpringConfig).spring(FloatProperties.RECT_X, (float) rect.left, this.mCatchUpSpringConfig).spring(FloatProperties.RECT_Y, (float) rect.top, this.mCatchUpSpringConfig);
        startBoundsAnimator((float) rect.left, (float) rect.top);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$movePip$3(Rect rect) {
        this.mMenuController.updateMenuLayout(rect);
    }

    public void animateIntoDismissTarget(MagnetizedObject.MagneticTarget magneticTarget, float f, float f2, boolean z, Function0<Unit> function0) {
        PointF centerOnScreen = magneticTarget.getCenterOnScreen();
        float dimensionPixelSize = ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dismiss_circle_size)) * 0.85f;
        float width = dimensionPixelSize / (((float) getBounds().width()) / ((float) getBounds().height()));
        float f3 = centerOnScreen.x - (dimensionPixelSize / 2.0f);
        float f4 = centerOnScreen.y - (width / 2.0f);
        if (!this.mPipBoundsState.getMotionBoundsState().isInMotion()) {
            this.mPipBoundsState.getMotionBoundsState().setBoundsInMotion(getBounds());
        }
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_X, f3, f, this.mAnimateToDismissSpringConfig).spring(FloatProperties.RECT_Y, f4, f2, this.mAnimateToDismissSpringConfig).spring(FloatProperties.RECT_WIDTH, dimensionPixelSize, this.mAnimateToDismissSpringConfig).spring(FloatProperties.RECT_HEIGHT, width, this.mAnimateToDismissSpringConfig).withEndActions((Function0<Unit>[]) new Function0[]{function0});
        startBoundsAnimator(f3, f4);
    }

    public void setSpringingToTouch(boolean z) {
        this.mSpringingToTouch = z;
    }

    public void expandLeavePip(boolean z) {
        expandLeavePip(z, false);
    }

    public void expandIntoSplit() {
        expandLeavePip(false, true);
    }

    public final void expandLeavePip(boolean z, boolean z2) {
        cancelPhysicsAnimation();
        int i = 0;
        this.mMenuController.hideMenu(0, false);
        PipTaskOrganizer pipTaskOrganizer = this.mPipTaskOrganizer;
        if (!z) {
            i = 300;
        }
        pipTaskOrganizer.exitPip(i, z2);
    }

    public void dismissPip() {
        cancelPhysicsAnimation();
        this.mMenuController.hideMenu(2, false);
        this.mPipTaskOrganizer.removePip();
    }

    public void onMovementBoundsChanged() {
        rebuildFlingConfigs();
        this.mFloatingAllowedArea.set(this.mPipBoundsState.getMovementBounds());
        this.mFloatingAllowedArea.right += getBounds().width();
        this.mFloatingAllowedArea.bottom += getBounds().height();
    }

    public final Rect getBounds() {
        return this.mPipBoundsState.getBounds();
    }

    public void flingToSnapTarget(float f, float f2, Runnable runnable) {
        movetoTarget(f, f2, runnable, false);
    }

    public void stashToEdge(float f, float f2, Runnable runnable) {
        if (this.mPipBoundsState.getStashedState() == 0) {
            f2 = 0.0f;
        }
        movetoTarget(f, f2, runnable, true);
    }

    public final void movetoTarget(float f, float f2, Runnable runnable, boolean z) {
        int i;
        int i2;
        this.mSpringingToTouch = false;
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_WIDTH, (float) getBounds().width(), this.mSpringConfig).spring(FloatProperties.RECT_HEIGHT, (float) getBounds().height(), this.mSpringConfig).flingThenSpring(FloatProperties.RECT_X, f, z ? this.mStashConfigX : this.mFlingConfigX, this.mSpringConfig, true).flingThenSpring(FloatProperties.RECT_Y, f2, this.mFlingConfigY, this.mSpringConfig);
        Rect stableInsets = this.mPipBoundsState.getDisplayLayout().stableInsets();
        if (z) {
            i = (this.mPipBoundsState.getStashOffset() - this.mPipBoundsState.getBounds().width()) + stableInsets.left;
        } else {
            i = this.mPipBoundsState.getMovementBounds().left;
        }
        float f3 = (float) i;
        if (z) {
            i2 = (this.mPipBoundsState.getDisplayBounds().right - this.mPipBoundsState.getStashOffset()) - stableInsets.right;
        } else {
            i2 = this.mPipBoundsState.getMovementBounds().right;
        }
        float f4 = (float) i2;
        if (f >= 0.0f) {
            f3 = f4;
        }
        startBoundsAnimator(f3, PhysicsAnimator.estimateFlingEndValue((float) this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion().top, f2, this.mFlingConfigY), runnable);
    }

    public void animateToBounds(Rect rect, PhysicsAnimator.SpringConfig springConfig) {
        if (!this.mTemporaryBoundsPhysicsAnimator.isRunning()) {
            this.mPipBoundsState.getMotionBoundsState().setBoundsInMotion(getBounds());
        }
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_X, (float) rect.left, springConfig).spring(FloatProperties.RECT_Y, (float) rect.top, springConfig);
        startBoundsAnimator((float) rect.left, (float) rect.top);
    }

    public void animateDismiss() {
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_Y, (float) (this.mPipBoundsState.getMovementBounds().bottom + (getBounds().height() * 2)), 0.0f, this.mSpringConfig).withEndActions(new PipMotionHelper$$ExternalSyntheticLambda5(this));
        startBoundsAnimator((float) getBounds().left, (float) (getBounds().bottom + getBounds().height()));
        this.mDismissalPending = false;
    }

    public float animateToExpandedState(Rect rect, Rect rect2, Rect rect3, Runnable runnable) {
        float snapFraction = this.mSnapAlgorithm.getSnapFraction(new Rect(getBounds()), rect2);
        this.mSnapAlgorithm.applySnapFraction(rect, rect3, snapFraction);
        this.mPostPipTransitionCallback = runnable;
        resizeAndAnimatePipUnchecked(rect, 250);
        return snapFraction;
    }

    public void animateToUnexpandedState(Rect rect, float f, Rect rect2, Rect rect3, boolean z) {
        if (f < 0.0f) {
            f = this.mSnapAlgorithm.getSnapFraction(new Rect(getBounds()), rect3, this.mPipBoundsState.getStashedState());
        }
        this.mSnapAlgorithm.applySnapFraction(rect, rect2, f, this.mPipBoundsState.getStashedState(), this.mPipBoundsState.getStashOffset(), this.mPipBoundsState.getDisplayBounds(), this.mPipBoundsState.getDisplayLayout().stableInsets());
        if (z) {
            movePip(rect);
        } else {
            resizeAndAnimatePipUnchecked(rect, 250);
        }
    }

    public void animateToStashedClosestEdge() {
        int i;
        Rect rect = new Rect();
        Rect stableInsets = this.mPipBoundsState.getDisplayLayout().stableInsets();
        int i2 = this.mPipBoundsState.getBounds().left == this.mPipBoundsState.getMovementBounds().left ? 1 : 2;
        if (i2 == 1) {
            i = (this.mPipBoundsState.getStashOffset() - this.mPipBoundsState.getBounds().width()) + stableInsets.left;
        } else {
            i = (this.mPipBoundsState.getDisplayBounds().right - this.mPipBoundsState.getStashOffset()) - stableInsets.right;
        }
        float f = (float) i;
        rect.set((int) f, this.mPipBoundsState.getBounds().top, (int) (f + ((float) this.mPipBoundsState.getBounds().width())), this.mPipBoundsState.getBounds().bottom);
        resizeAndAnimatePipUnchecked(rect, 250);
        this.mPipBoundsState.setStashed(i2);
    }

    public void animateToUnStashedBounds(Rect rect) {
        resizeAndAnimatePipUnchecked(rect, 250);
    }

    public void animateToOffset(Rect rect, int i) {
        cancelPhysicsAnimation();
        this.mPipTaskOrganizer.scheduleOffsetPip(rect, i, 300, this.mUpdateBoundsCallback);
    }

    public final void cancelPhysicsAnimation() {
        this.mTemporaryBoundsPhysicsAnimator.cancel();
        this.mPipBoundsState.getMotionBoundsState().onPhysicsAnimationEnded();
        this.mSpringingToTouch = false;
    }

    public final void rebuildFlingConfigs() {
        this.mFlingConfigX = new PhysicsAnimator.FlingConfig(1.9f, (float) this.mPipBoundsState.getMovementBounds().left, (float) this.mPipBoundsState.getMovementBounds().right);
        this.mFlingConfigY = new PhysicsAnimator.FlingConfig(1.9f, (float) this.mPipBoundsState.getMovementBounds().top, (float) this.mPipBoundsState.getMovementBounds().bottom);
        Rect stableInsets = this.mPipBoundsState.getDisplayLayout().stableInsets();
        this.mStashConfigX = new PhysicsAnimator.FlingConfig(1.9f, (float) ((this.mPipBoundsState.getStashOffset() - this.mPipBoundsState.getBounds().width()) + stableInsets.left), (float) ((this.mPipBoundsState.getDisplayBounds().right - this.mPipBoundsState.getStashOffset()) - stableInsets.right));
    }

    public final void startBoundsAnimator(float f, float f2) {
        startBoundsAnimator(f, f2, (Runnable) null);
    }

    public final void startBoundsAnimator(float f, float f2, Runnable runnable) {
        if (!this.mSpringingToTouch) {
            cancelPhysicsAnimation();
        }
        int i = (int) f;
        int i2 = (int) f2;
        setAnimatingToBounds(new Rect(i, i2, getBounds().width() + i, getBounds().height() + i2));
        if (!this.mTemporaryBoundsPhysicsAnimator.isRunning()) {
            if (runnable != null) {
                this.mTemporaryBoundsPhysicsAnimator.addUpdateListener(this.mResizePipUpdateListener).withEndActions(new PipMotionHelper$$ExternalSyntheticLambda4(this), runnable);
            } else {
                this.mTemporaryBoundsPhysicsAnimator.addUpdateListener(this.mResizePipUpdateListener).withEndActions(new PipMotionHelper$$ExternalSyntheticLambda4(this));
            }
        }
        this.mTemporaryBoundsPhysicsAnimator.start();
    }

    public void notifyDismissalPending() {
        this.mDismissalPending = true;
    }

    public final void onBoundsPhysicsAnimationEnd() {
        if (!this.mDismissalPending && !this.mSpringingToTouch && !this.mMagnetizedPip.getObjectStuckToTarget()) {
            PipBoundsState pipBoundsState = this.mPipBoundsState;
            pipBoundsState.setBounds(pipBoundsState.getMotionBoundsState().getBoundsInMotion());
            this.mPipBoundsState.getMotionBoundsState().onAllAnimationsEnded();
            if (!this.mDismissalPending) {
                this.mPipTaskOrganizer.scheduleFinishResizePip(getBounds());
            }
        }
        this.mPipBoundsState.getMotionBoundsState().onPhysicsAnimationEnded();
        this.mSpringingToTouch = false;
        this.mDismissalPending = false;
    }

    public final void setAnimatingToBounds(Rect rect) {
        this.mPipBoundsState.getMotionBoundsState().setAnimatingToBounds(rect);
        this.mFloatingContentCoordinator.onContentMoved(this);
    }

    public final void resizePipUnchecked(Rect rect) {
        if (!rect.equals(getBounds())) {
            this.mPipTaskOrganizer.scheduleResizePip(rect, this.mUpdateBoundsCallback);
        }
    }

    public final void resizeAndAnimatePipUnchecked(Rect rect, int i) {
        this.mPipTaskOrganizer.scheduleAnimateResizePip(rect, i, 8, (Consumer<Rect>) null);
        setAnimatingToBounds(rect);
    }

    public MagnetizedObject<Rect> getMagnetizedPip() {
        if (this.mMagnetizedPip == null) {
            this.mMagnetizedPip = new MagnetizedObject<Rect>(this.mContext, this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion(), FloatProperties.RECT_X, FloatProperties.RECT_Y) {
                public float getWidth(Rect rect) {
                    return (float) rect.width();
                }

                public float getHeight(Rect rect) {
                    return (float) rect.height();
                }

                public void getLocationOnScreen(Rect rect, int[] iArr) {
                    iArr[0] = rect.left;
                    iArr[1] = rect.top;
                }
            };
        }
        return this.mMagnetizedPip;
    }
}
