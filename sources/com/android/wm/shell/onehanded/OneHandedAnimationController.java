package com.android.wm.shell.onehanded;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.animation.BaseInterpolator;
import android.window.WindowContainerToken;
import com.android.wm.shell.onehanded.OneHandedSurfaceTransactionHelper;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OneHandedAnimationController {
    public final HashMap<WindowContainerToken, OneHandedTransitionAnimator> mAnimatorMap = new HashMap<>();
    public final OneHandedInterpolator mInterpolator;
    public final OneHandedSurfaceTransactionHelper mSurfaceTransactionHelper;

    public OneHandedAnimationController(Context context) {
        this.mSurfaceTransactionHelper = new OneHandedSurfaceTransactionHelper(context);
        this.mInterpolator = new OneHandedInterpolator();
    }

    public OneHandedTransitionAnimator getAnimator(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2, Rect rect) {
        OneHandedTransitionAnimator oneHandedTransitionAnimator = this.mAnimatorMap.get(windowContainerToken);
        if (oneHandedTransitionAnimator == null) {
            this.mAnimatorMap.put(windowContainerToken, setupOneHandedTransitionAnimator(OneHandedTransitionAnimator.ofYOffset(windowContainerToken, surfaceControl, f, f2, rect)));
        } else if (oneHandedTransitionAnimator.isRunning()) {
            oneHandedTransitionAnimator.updateEndValue(f2);
        } else {
            oneHandedTransitionAnimator.cancel();
            this.mAnimatorMap.put(windowContainerToken, setupOneHandedTransitionAnimator(OneHandedTransitionAnimator.ofYOffset(windowContainerToken, surfaceControl, f, f2, rect)));
        }
        return this.mAnimatorMap.get(windowContainerToken);
    }

    public HashMap<WindowContainerToken, OneHandedTransitionAnimator> getAnimatorMap() {
        return this.mAnimatorMap;
    }

    public boolean isAnimatorsConsumed() {
        return this.mAnimatorMap.isEmpty();
    }

    public void removeAnimator(WindowContainerToken windowContainerToken) {
        OneHandedTransitionAnimator remove = this.mAnimatorMap.remove(windowContainerToken);
        if (remove != null && remove.isRunning()) {
            remove.cancel();
        }
    }

    public OneHandedTransitionAnimator setupOneHandedTransitionAnimator(OneHandedTransitionAnimator oneHandedTransitionAnimator) {
        oneHandedTransitionAnimator.setSurfaceTransactionHelper(this.mSurfaceTransactionHelper);
        oneHandedTransitionAnimator.setInterpolator(this.mInterpolator);
        oneHandedTransitionAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        return oneHandedTransitionAnimator;
    }

    public static abstract class OneHandedTransitionAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        public float mCurrentValue;
        public float mEndValue;
        public final SurfaceControl mLeash;
        public final List<OneHandedAnimationCallback> mOneHandedAnimationCallbacks;
        public float mStartValue;
        public OneHandedSurfaceTransactionHelper.SurfaceControlTransactionFactory mSurfaceControlTransactionFactory;
        public OneHandedSurfaceTransactionHelper mSurfaceTransactionHelper;
        public final WindowContainerToken mToken;
        public int mTransitionDirection;

        public abstract void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f);

        public void onAnimationRepeat(Animator animator) {
        }

        public void onEndTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        }

        public OneHandedTransitionAnimator(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2) {
            this.mOneHandedAnimationCallbacks = new ArrayList();
            this.mLeash = surfaceControl;
            this.mToken = windowContainerToken;
            this.mStartValue = f;
            this.mEndValue = f2;
            addListener(this);
            addUpdateListener(this);
            this.mSurfaceControlTransactionFactory = new BackgroundWindowManager$$ExternalSyntheticLambda0();
            this.mTransitionDirection = 0;
        }

        public void onAnimationStart(Animator animator) {
            this.mCurrentValue = this.mStartValue;
            this.mOneHandedAnimationCallbacks.forEach(new OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationStart$0(OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onOneHandedAnimationStart(this);
        }

        public void onAnimationEnd(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            onEndTransaction(this.mLeash, newSurfaceControlTransaction);
            this.mOneHandedAnimationCallbacks.forEach(new OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda1(this, newSurfaceControlTransaction));
            this.mOneHandedAnimationCallbacks.clear();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$1(SurfaceControl.Transaction transaction, OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onOneHandedAnimationEnd(transaction, this);
        }

        public void onAnimationCancel(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            this.mOneHandedAnimationCallbacks.forEach(new OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda2(this));
            this.mOneHandedAnimationCallbacks.clear();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationCancel$2(OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onOneHandedAnimationCancel(this);
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            this.mOneHandedAnimationCallbacks.forEach(new OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda3(this, newSurfaceControlTransaction));
            applySurfaceControlTransaction(this.mLeash, newSurfaceControlTransaction, valueAnimator.getAnimatedFraction());
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationUpdate$3(SurfaceControl.Transaction transaction, OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onAnimationUpdate(transaction, 0.0f, this.mCurrentValue);
        }

        public OneHandedSurfaceTransactionHelper getSurfaceTransactionHelper() {
            return this.mSurfaceTransactionHelper;
        }

        public void setSurfaceTransactionHelper(OneHandedSurfaceTransactionHelper oneHandedSurfaceTransactionHelper) {
            this.mSurfaceTransactionHelper = oneHandedSurfaceTransactionHelper;
        }

        public OneHandedTransitionAnimator addOneHandedAnimationCallback(OneHandedAnimationCallback oneHandedAnimationCallback) {
            if (oneHandedAnimationCallback != null) {
                this.mOneHandedAnimationCallbacks.add(oneHandedAnimationCallback);
            }
            return this;
        }

        public WindowContainerToken getToken() {
            return this.mToken;
        }

        public float getDestinationOffset() {
            return this.mEndValue - this.mStartValue;
        }

        public int getTransitionDirection() {
            return this.mTransitionDirection;
        }

        public OneHandedTransitionAnimator setTransitionDirection(int i) {
            this.mTransitionDirection = i;
            return this;
        }

        public float getStartValue() {
            return this.mStartValue;
        }

        public float getEndValue() {
            return this.mEndValue;
        }

        public void setCurrentValue(float f) {
            this.mCurrentValue = f;
        }

        public void updateEndValue(float f) {
            this.mEndValue = f;
        }

        public SurfaceControl.Transaction newSurfaceControlTransaction() {
            return this.mSurfaceControlTransactionFactory.getTransaction();
        }

        public static OneHandedTransitionAnimator ofYOffset(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2, Rect rect) {
            return new OneHandedTransitionAnimator(windowContainerToken, surfaceControl, f, f2, rect) {
                public final Rect mTmpRect;
                public final /* synthetic */ Rect val$displayBounds;

                public final float getCastedFractionValue(float f, float f2, float f3) {
                    return (f * (1.0f - f3)) + (f2 * f3) + 0.5f;
                }

                {
                    this.val$displayBounds = r11;
                    this.mTmpRect = new Rect(r11);
                }

                public void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f) {
                    float castedFractionValue = getCastedFractionValue(getStartValue(), getEndValue(), f);
                    Rect rect = this.mTmpRect;
                    int i = rect.left;
                    int round = rect.top + Math.round(castedFractionValue);
                    Rect rect2 = this.mTmpRect;
                    rect.set(i, round, rect2.right, rect2.bottom + Math.round(castedFractionValue));
                    setCurrentValue(castedFractionValue);
                    getSurfaceTransactionHelper().crop(transaction, surfaceControl, this.mTmpRect).round(transaction, surfaceControl).translate(transaction, surfaceControl, castedFractionValue);
                    transaction.apply();
                }
            };
        }
    }

    public class OneHandedInterpolator extends BaseInterpolator {
        public OneHandedInterpolator() {
        }

        public float getInterpolation(float f) {
            return (float) ((Math.pow(2.0d, (double) (-10.0f * f)) * Math.sin((((double) ((f - 4.0f) / 4.0f)) * 6.283185307179586d) / 4.0d)) + 1.0d);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedAnimationControllerstates: ");
        printWriter.print("  mAnimatorMap=");
        printWriter.println(this.mAnimatorMap);
        OneHandedSurfaceTransactionHelper oneHandedSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
        if (oneHandedSurfaceTransactionHelper != null) {
            oneHandedSurfaceTransactionHelper.dump(printWriter);
        }
    }
}
