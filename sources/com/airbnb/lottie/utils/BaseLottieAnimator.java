package com.airbnb.lottie.utils;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class BaseLottieAnimator extends ValueAnimator {
    public final Set<Animator.AnimatorListener> listeners = new CopyOnWriteArraySet();
    public final Set<ValueAnimator.AnimatorUpdateListener> updateListeners = new CopyOnWriteArraySet();

    public long getStartDelay() {
        throw new UnsupportedOperationException("LottieAnimator does not support getStartDelay.");
    }

    public void setStartDelay(long j) {
        throw new UnsupportedOperationException("LottieAnimator does not support setStartDelay.");
    }

    public ValueAnimator setDuration(long j) {
        throw new UnsupportedOperationException("LottieAnimator does not support setDuration.");
    }

    public void setInterpolator(TimeInterpolator timeInterpolator) {
        throw new UnsupportedOperationException("LottieAnimator does not support setInterpolator.");
    }

    public void addUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        this.updateListeners.add(animatorUpdateListener);
    }

    public void removeUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        this.updateListeners.remove(animatorUpdateListener);
    }

    public void removeAllUpdateListeners() {
        this.updateListeners.clear();
    }

    public void addListener(Animator.AnimatorListener animatorListener) {
        this.listeners.add(animatorListener);
    }

    public void removeListener(Animator.AnimatorListener animatorListener) {
        this.listeners.remove(animatorListener);
    }

    public void removeAllListeners() {
        this.listeners.clear();
    }

    public void notifyStart(boolean z) {
        for (Animator.AnimatorListener onAnimationStart : this.listeners) {
            onAnimationStart.onAnimationStart(this, z);
        }
    }

    public void notifyRepeat() {
        for (Animator.AnimatorListener onAnimationRepeat : this.listeners) {
            onAnimationRepeat.onAnimationRepeat(this);
        }
    }

    public void notifyEnd(boolean z) {
        for (Animator.AnimatorListener onAnimationEnd : this.listeners) {
            onAnimationEnd.onAnimationEnd(this, z);
        }
    }

    public void notifyCancel() {
        for (Animator.AnimatorListener onAnimationCancel : this.listeners) {
            onAnimationCancel.onAnimationCancel(this);
        }
    }

    public void notifyUpdate() {
        for (ValueAnimator.AnimatorUpdateListener onAnimationUpdate : this.updateListeners) {
            onAnimationUpdate.onAnimationUpdate(this);
        }
    }
}
