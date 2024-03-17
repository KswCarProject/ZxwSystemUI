package com.android.wm.shell.bubbles.animation;

import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BubblePositioner;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import com.google.android.collect.Sets;
import java.io.PrintWriter;
import java.util.Set;

public class ExpandedAnimationController extends PhysicsAnimationLayout.PhysicsAnimationController {
    public Runnable mAfterCollapse;
    public Runnable mAfterExpand;
    public final PhysicsAnimator.SpringConfig mAnimateOutSpringConfig = new PhysicsAnimator.SpringConfig(1000.0f, 1.0f);
    public boolean mAnimatingCollapse = false;
    public boolean mAnimatingExpand = false;
    public boolean mBubbleDraggedOutEnough = false;
    public float mBubbleSizePx;
    public BubbleStackView mBubbleStackView;
    public PointF mCollapsePoint;
    public Runnable mLeadBubbleEndAction;
    public MagnetizedObject<View> mMagnetizedBubbleDraggingOut;
    public Runnable mOnBubbleAnimatedOutAction;
    public BubblePositioner mPositioner;
    public boolean mPreparingToCollapse = false;
    public boolean mSpringToTouchOnNextMotionEvent = false;
    public boolean mSpringingBubbleToTouch = false;
    public float mStackOffsetPx;

    public int getNextAnimationInChain(DynamicAnimation.ViewProperty viewProperty, int i) {
        return -1;
    }

    public float getOffsetForChainedPropertyAnimation(DynamicAnimation.ViewProperty viewProperty, int i) {
        return 0.0f;
    }

    public ExpandedAnimationController(BubblePositioner bubblePositioner, Runnable runnable, BubbleStackView bubbleStackView) {
        this.mPositioner = bubblePositioner;
        updateResources();
        this.mOnBubbleAnimatedOutAction = runnable;
        this.mCollapsePoint = this.mPositioner.getDefaultStartPosition();
        this.mBubbleStackView = bubbleStackView;
    }

    public void expandFromStack(Runnable runnable, Runnable runnable2) {
        this.mPreparingToCollapse = false;
        this.mAnimatingCollapse = false;
        this.mAnimatingExpand = true;
        this.mAfterExpand = runnable;
        this.mLeadBubbleEndAction = runnable2;
        startOrUpdatePathAnimation(true);
    }

    public void expandFromStack(Runnable runnable) {
        expandFromStack(runnable, (Runnable) null);
    }

    public void notifyPreparingToCollapse() {
        this.mPreparingToCollapse = true;
    }

    public void collapseBackToStack(PointF pointF, Runnable runnable) {
        this.mAnimatingExpand = false;
        this.mPreparingToCollapse = false;
        this.mAnimatingCollapse = true;
        this.mAfterCollapse = runnable;
        this.mCollapsePoint = pointF;
        startOrUpdatePathAnimation(false);
    }

    public void updateResources() {
        PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
        if (physicsAnimationLayout != null) {
            this.mStackOffsetPx = (float) physicsAnimationLayout.getContext().getResources().getDimensionPixelSize(R.dimen.bubble_stack_offset);
            this.mBubbleSizePx = (float) this.mPositioner.getBubbleSize();
        }
    }

    public final void startOrUpdatePathAnimation(boolean z) {
        Runnable runnable;
        if (z) {
            runnable = new ExpandedAnimationController$$ExternalSyntheticLambda2(this);
        } else {
            runnable = new ExpandedAnimationController$$ExternalSyntheticLambda3(this);
        }
        animationsForChildrenFromIndex(0, new ExpandedAnimationController$$ExternalSyntheticLambda4(this, z)).startAll(runnable);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startOrUpdatePathAnimation$0() {
        this.mAnimatingExpand = false;
        Runnable runnable = this.mAfterExpand;
        if (runnable != null) {
            runnable.run();
        }
        this.mAfterExpand = null;
        updateBubblePositions();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startOrUpdatePathAnimation$1() {
        this.mAnimatingCollapse = false;
        Runnable runnable = this.mAfterCollapse;
        if (runnable != null) {
            runnable.run();
        }
        this.mAfterCollapse = null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startOrUpdatePathAnimation$3(boolean z, int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
        int i2;
        View childAt = this.mLayout.getChildAt(i);
        Path path = new Path();
        path.moveTo(childAt.getTranslationX(), childAt.getTranslationY());
        PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(i, this.mBubbleStackView.getState());
        if (z) {
            path.lineTo(childAt.getTranslationX(), expandedBubbleXY.y);
            path.lineTo(expandedBubbleXY.x, expandedBubbleXY.y);
        } else {
            float f = this.mCollapsePoint.x;
            path.lineTo(f, expandedBubbleXY.y);
            path.lineTo(f, this.mCollapsePoint.y + (((float) Math.min(i, 1)) * this.mStackOffsetPx));
        }
        boolean z2 = (z && !this.mLayout.isFirstChildXLeftOfCenter(childAt.getTranslationX())) || (!z && this.mLayout.isFirstChildXLeftOfCenter(this.mCollapsePoint.x));
        if (z2) {
            i2 = i * 10;
        } else {
            i2 = (this.mLayout.getChildCount() - i) * 10;
        }
        boolean z3 = (z2 && i == 0) || (!z2 && i == this.mLayout.getChildCount() - 1);
        Interpolator interpolator = Interpolators.LINEAR;
        Runnable[] runnableArr = new Runnable[2];
        runnableArr[0] = z3 ? this.mLeadBubbleEndAction : null;
        runnableArr[1] = new ExpandedAnimationController$$ExternalSyntheticLambda5(this);
        physicsPropertyAnimator.followAnimatedTargetAlongPath(path, 175, interpolator, runnableArr).withStartDelay((long) i2).withStiffness(1000.0f);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startOrUpdatePathAnimation$2() {
        this.mLeadBubbleEndAction = null;
    }

    public void onUnstuckFromTarget() {
        this.mSpringToTouchOnNextMotionEvent = true;
    }

    public void prepareForBubbleDrag(View view, MagnetizedObject.MagneticTarget magneticTarget, MagnetizedObject.MagnetListener magnetListener) {
        this.mLayout.cancelAnimationsOnView(view);
        view.setTranslationZ(32767.0f);
        final View view2 = view;
        AnonymousClass1 r1 = new MagnetizedObject<View>(this.mLayout.getContext(), view, DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y) {
            public float getWidth(View view) {
                return ExpandedAnimationController.this.mBubbleSizePx;
            }

            public float getHeight(View view) {
                return ExpandedAnimationController.this.mBubbleSizePx;
            }

            public void getLocationOnScreen(View view, int[] iArr) {
                iArr[0] = (int) view2.getTranslationX();
                iArr[1] = (int) view2.getTranslationY();
            }
        };
        this.mMagnetizedBubbleDraggingOut = r1;
        r1.addTarget(magneticTarget);
        this.mMagnetizedBubbleDraggingOut.setMagnetListener(magnetListener);
        this.mMagnetizedBubbleDraggingOut.setHapticsEnabled(true);
        this.mMagnetizedBubbleDraggingOut.setFlingToTargetMinVelocity(6000.0f);
    }

    public final void springBubbleTo(View view, float f, float f2) {
        animationForChild(view).translationX(f, new Runnable[0]).translationY(f2, new Runnable[0]).withStiffness(10000.0f).start(new Runnable[0]);
    }

    public void dragBubbleOut(View view, float f, float f2) {
        MagnetizedObject<View> magnetizedObject = this.mMagnetizedBubbleDraggingOut;
        if (magnetizedObject != null) {
            boolean z = true;
            if (this.mSpringToTouchOnNextMotionEvent) {
                springBubbleTo(magnetizedObject.getUnderlyingObject(), f, f2);
                this.mSpringToTouchOnNextMotionEvent = false;
                this.mSpringingBubbleToTouch = true;
            } else if (this.mSpringingBubbleToTouch) {
                if (this.mLayout.arePropertiesAnimatingOnView(view, DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y)) {
                    springBubbleTo(this.mMagnetizedBubbleDraggingOut.getUnderlyingObject(), f, f2);
                } else {
                    this.mSpringingBubbleToTouch = false;
                }
            }
            if (!this.mSpringingBubbleToTouch && !this.mMagnetizedBubbleDraggingOut.getObjectStuckToTarget()) {
                view.setTranslationX(f);
                view.setTranslationY(f2);
            }
            float expandedViewYTopAligned = this.mPositioner.getExpandedViewYTopAligned();
            float f3 = this.mBubbleSizePx;
            if (f2 <= expandedViewYTopAligned + f3 && f2 >= expandedViewYTopAligned - f3) {
                z = false;
            }
            if (z != this.mBubbleDraggedOutEnough) {
                updateBubblePositions();
                this.mBubbleDraggedOutEnough = z;
            }
        }
    }

    public void dismissDraggedOutBubble(View view, float f, Runnable runnable) {
        if (view != null) {
            animationForChild(view).withStiffness(10000.0f).scaleX(0.0f, new Runnable[0]).scaleY(0.0f, new Runnable[0]).translationY(view.getTranslationY() + f, new Runnable[0]).alpha(0.0f, runnable).start(new Runnable[0]);
            updateBubblePositions();
        }
    }

    public View getDraggedOutBubble() {
        MagnetizedObject<View> magnetizedObject = this.mMagnetizedBubbleDraggingOut;
        if (magnetizedObject == null) {
            return null;
        }
        return magnetizedObject.getUnderlyingObject();
    }

    public MagnetizedObject<View> getMagnetizedBubbleDraggingOut() {
        return this.mMagnetizedBubbleDraggingOut;
    }

    public void snapBubbleBack(View view, float f, float f2) {
        PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
        if (physicsAnimationLayout != null) {
            int indexOfChild = physicsAnimationLayout.indexOfChild(view);
            PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(indexOfChild, this.mBubbleStackView.getState());
            animationForChildAtIndex(indexOfChild).position(expandedBubbleXY.x, expandedBubbleXY.y, new Runnable[0]).withPositionStartVelocities(f, f2).start(new ExpandedAnimationController$$ExternalSyntheticLambda0(view));
            this.mMagnetizedBubbleDraggingOut = null;
            updateBubblePositions();
        }
    }

    public void onGestureFinished() {
        this.mBubbleDraggedOutEnough = false;
        this.mMagnetizedBubbleDraggingOut = null;
        updateBubblePositions();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("ExpandedAnimationController state:");
        printWriter.print("  isActive:          ");
        printWriter.println(isActiveController());
        printWriter.print("  animatingExpand:   ");
        printWriter.println(this.mAnimatingExpand);
        printWriter.print("  animatingCollapse: ");
        printWriter.println(this.mAnimatingCollapse);
        printWriter.print("  springingBubble:   ");
        printWriter.println(this.mSpringingBubbleToTouch);
    }

    public void onActiveControllerForLayout(PhysicsAnimationLayout physicsAnimationLayout) {
        updateResources();
        this.mLayout.setVisibility(0);
        animationsForChildrenFromIndex(0, new ExpandedAnimationController$$ExternalSyntheticLambda1()).startAll(new Runnable[0]);
    }

    public Set<DynamicAnimation.ViewProperty> getAnimatedProperties() {
        return Sets.newHashSet(new DynamicAnimation.ViewProperty[]{DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y, DynamicAnimation.SCALE_X, DynamicAnimation.SCALE_Y, DynamicAnimation.ALPHA});
    }

    public SpringForce getSpringForce(DynamicAnimation.ViewProperty viewProperty, View view) {
        return new SpringForce().setDampingRatio(0.65f).setStiffness(200.0f);
    }

    public void onChildAdded(View view, int i) {
        float f;
        if (this.mAnimatingExpand) {
            startOrUpdatePathAnimation(true);
        } else if (this.mAnimatingCollapse) {
            startOrUpdatePathAnimation(false);
        } else {
            boolean isStackOnLeft = this.mPositioner.isStackOnLeft(this.mCollapsePoint);
            PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(i, this.mBubbleStackView.getState());
            if (this.mPositioner.showBubblesVertically()) {
                view.setTranslationY(expandedBubbleXY.y);
            } else {
                view.setTranslationX(expandedBubbleXY.x);
            }
            if (!this.mPreparingToCollapse) {
                if (this.mPositioner.showBubblesVertically()) {
                    if (isStackOnLeft) {
                        f = expandedBubbleXY.x - (this.mBubbleSizePx * 4.0f);
                    } else {
                        f = expandedBubbleXY.x + (this.mBubbleSizePx * 4.0f);
                    }
                    animationForChild(view).translationX(f, expandedBubbleXY.y, new Runnable[0]).start(new Runnable[0]);
                } else {
                    animationForChild(view).translationY(expandedBubbleXY.y - (this.mBubbleSizePx * 4.0f), expandedBubbleXY.y, new Runnable[0]).start(new Runnable[0]);
                }
                updateBubblePositions();
            }
        }
    }

    public void onChildRemoved(View view, int i, Runnable runnable) {
        if (view.equals(getDraggedOutBubble())) {
            this.mMagnetizedBubbleDraggingOut = null;
            runnable.run();
            this.mOnBubbleAnimatedOutAction.run();
        } else {
            PhysicsAnimator.getInstance(view).spring(DynamicAnimation.ALPHA, 0.0f).spring(DynamicAnimation.SCALE_X, 0.0f, this.mAnimateOutSpringConfig).spring(DynamicAnimation.SCALE_Y, 0.0f, this.mAnimateOutSpringConfig).withEndActions(runnable, this.mOnBubbleAnimatedOutAction).start();
        }
        updateBubblePositions();
    }

    public void onChildReordered(View view, int i, int i2) {
        if (!this.mPreparingToCollapse) {
            if (this.mAnimatingCollapse) {
                startOrUpdatePathAnimation(false);
            } else {
                updateBubblePositions();
            }
        }
    }

    public final void updateBubblePositions() {
        if (!this.mAnimatingExpand && !this.mAnimatingCollapse) {
            int i = 0;
            while (i < this.mLayout.getChildCount()) {
                View childAt = this.mLayout.getChildAt(i);
                if (!childAt.equals(getDraggedOutBubble())) {
                    PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(i, this.mBubbleStackView.getState());
                    animationForChild(childAt).translationX(expandedBubbleXY.x, new Runnable[0]).translationY(expandedBubbleXY.y, new Runnable[0]).start(new Runnable[0]);
                    i++;
                } else {
                    return;
                }
            }
        }
    }
}
