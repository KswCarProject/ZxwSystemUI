package com.android.systemui.statusbar.phone;

import android.view.MotionEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.util.concurrency.DelayableExecutor;

public class NotificationTapHelper {
    public final ActivationListener mActivationListener;
    public final DoubleTapListener mDoubleTapListener;
    public final DelayableExecutor mExecutor;
    public final FalsingManager mFalsingManager;
    public final SlideBackListener mSlideBackListener;
    public Runnable mTimeoutCancel;
    public boolean mTrackTouch;

    @FunctionalInterface
    public interface ActivationListener {
        void onActiveChanged(boolean z);
    }

    @FunctionalInterface
    public interface DoubleTapListener {
        boolean onDoubleTap();
    }

    @FunctionalInterface
    public interface SlideBackListener {
        boolean onSlideBack();
    }

    public NotificationTapHelper(FalsingManager falsingManager, DelayableExecutor delayableExecutor, ActivationListener activationListener, DoubleTapListener doubleTapListener, SlideBackListener slideBackListener) {
        this.mFalsingManager = falsingManager;
        this.mExecutor = delayableExecutor;
        this.mActivationListener = activationListener;
        this.mDoubleTapListener = doubleTapListener;
        this.mSlideBackListener = slideBackListener;
    }

    @VisibleForTesting
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return onTouchEvent(motionEvent, Integer.MAX_VALUE);
    }

    public boolean onTouchEvent(MotionEvent motionEvent, int i) {
        int actionMasked = motionEvent.getActionMasked();
        boolean z = true;
        if (actionMasked == 0) {
            if (motionEvent.getY() > ((float) i)) {
                z = false;
            }
            this.mTrackTouch = z;
        } else if (actionMasked == 1) {
            this.mTrackTouch = false;
            if (!this.mFalsingManager.isFalseTap(0)) {
                makeInactive();
                return this.mDoubleTapListener.onDoubleTap();
            } else if (this.mFalsingManager.isSimpleTap()) {
                SlideBackListener slideBackListener = this.mSlideBackListener;
                if (slideBackListener != null && slideBackListener.onSlideBack()) {
                    return true;
                }
                if (this.mTimeoutCancel == null) {
                    makeActive();
                    return true;
                }
                makeInactive();
                if (!this.mFalsingManager.isFalseDoubleTap()) {
                    return this.mDoubleTapListener.onDoubleTap();
                }
            } else {
                makeInactive();
            }
        } else if (actionMasked != 2) {
            if (actionMasked == 3) {
                makeInactive();
                this.mTrackTouch = false;
            }
        } else if (this.mTrackTouch && !this.mFalsingManager.isSimpleTap()) {
            makeInactive();
            this.mTrackTouch = false;
        }
        return this.mTrackTouch;
    }

    public final void makeActive() {
        this.mTimeoutCancel = this.mExecutor.executeDelayed(new NotificationTapHelper$$ExternalSyntheticLambda0(this), 1200);
        this.mActivationListener.onActiveChanged(true);
    }

    public final void makeInactive() {
        this.mActivationListener.onActiveChanged(false);
        Runnable runnable = this.mTimeoutCancel;
        if (runnable != null) {
            runnable.run();
            this.mTimeoutCancel = null;
        }
    }

    public static class Factory {
        public final DelayableExecutor mDelayableExecutor;
        public final FalsingManager mFalsingManager;

        public Factory(FalsingManager falsingManager, DelayableExecutor delayableExecutor) {
            this.mFalsingManager = falsingManager;
            this.mDelayableExecutor = delayableExecutor;
        }

        public NotificationTapHelper create(ActivationListener activationListener, DoubleTapListener doubleTapListener, SlideBackListener slideBackListener) {
            return new NotificationTapHelper(this.mFalsingManager, this.mDelayableExecutor, activationListener, doubleTapListener, slideBackListener);
        }
    }
}
