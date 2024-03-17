package com.android.wm.shell.pip.phone;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;

public class PipTouchState {
    @VisibleForTesting
    public static final long DOUBLE_TAP_TIMEOUT = 200;
    public int mActivePointerId;
    public boolean mAllowDraggingOffscreen = false;
    public boolean mAllowTouches = true;
    public final Runnable mDoubleTapTimeoutCallback;
    public final PointF mDownDelta = new PointF();
    public final PointF mDownTouch = new PointF();
    public long mDownTouchTime = 0;
    public final Runnable mHoverExitTimeoutCallback;
    public boolean mIsDoubleTap = false;
    public boolean mIsDragging = false;
    public boolean mIsUserInteracting = false;
    public boolean mIsWaitingForDoubleTap = false;
    public final PointF mLastDelta = new PointF();
    public long mLastDownTouchTime = 0;
    public final PointF mLastTouch = new PointF();
    public int mLastTouchDisplayId = -1;
    public final ShellExecutor mMainExecutor;
    public boolean mPreviouslyDragging = false;
    public boolean mStartedDragging = false;
    public long mUpTouchTime = 0;
    public final PointF mVelocity = new PointF();
    public VelocityTracker mVelocityTracker;
    public final ViewConfiguration mViewConfig;

    public PipTouchState(ViewConfiguration viewConfiguration, Runnable runnable, Runnable runnable2, ShellExecutor shellExecutor) {
        this.mViewConfig = viewConfiguration;
        this.mDoubleTapTimeoutCallback = runnable;
        this.mHoverExitTimeoutCallback = runnable2;
        this.mMainExecutor = shellExecutor;
    }

    public void reset() {
        this.mAllowDraggingOffscreen = false;
        this.mIsDragging = false;
        this.mStartedDragging = false;
        this.mIsUserInteracting = false;
        this.mLastTouchDisplayId = -1;
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        this.mLastTouchDisplayId = motionEvent.getDisplayId();
        int actionMasked = motionEvent.getActionMasked();
        boolean z = false;
        boolean z2 = true;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked != 6) {
                            if (actionMasked == 11) {
                                removeHoverExitTimeoutCallback();
                                return;
                            }
                            return;
                        } else if (this.mIsUserInteracting) {
                            addMovementToVelocityTracker(motionEvent);
                            int actionIndex = motionEvent.getActionIndex();
                            if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
                                if (actionIndex == 0) {
                                    z = true;
                                }
                                this.mActivePointerId = motionEvent.getPointerId(z ? 1 : 0);
                                this.mLastTouch.set(motionEvent.getRawX(z), motionEvent.getRawY(z));
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    }
                } else if (this.mIsUserInteracting) {
                    addMovementToVelocityTracker(motionEvent);
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex != -1) {
                        float rawX = motionEvent.getRawX(findPointerIndex);
                        float rawY = motionEvent.getRawY(findPointerIndex);
                        PointF pointF = this.mLastDelta;
                        PointF pointF2 = this.mLastTouch;
                        pointF.set(rawX - pointF2.x, rawY - pointF2.y);
                        PointF pointF3 = this.mDownDelta;
                        PointF pointF4 = this.mDownTouch;
                        pointF3.set(rawX - pointF4.x, rawY - pointF4.y);
                        boolean z3 = this.mDownDelta.length() > ((float) this.mViewConfig.getScaledTouchSlop());
                        if (this.mIsDragging) {
                            this.mStartedDragging = false;
                        } else if (z3) {
                            this.mIsDragging = true;
                            this.mStartedDragging = true;
                        }
                        this.mLastTouch.set(rawX, rawY);
                        return;
                    } else if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                        ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1168568957, 4, (String) null, "PipTouchState", Long.valueOf((long) this.mActivePointerId));
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else if (this.mIsUserInteracting) {
                addMovementToVelocityTracker(motionEvent);
                this.mVelocityTracker.computeCurrentVelocity(1000, (float) this.mViewConfig.getScaledMaximumFlingVelocity());
                this.mVelocity.set(this.mVelocityTracker.getXVelocity(), this.mVelocityTracker.getYVelocity());
                int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                if (findPointerIndex2 != -1) {
                    this.mUpTouchTime = motionEvent.getEventTime();
                    this.mLastTouch.set(motionEvent.getRawX(findPointerIndex2), motionEvent.getRawY(findPointerIndex2));
                    boolean z4 = this.mIsDragging;
                    this.mPreviouslyDragging = z4;
                    if (!this.mIsDoubleTap && !z4 && this.mUpTouchTime - this.mDownTouchTime < 200) {
                        z = true;
                    }
                    this.mIsWaitingForDoubleTap = z;
                } else if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1325170887, 4, (String) null, "PipTouchState", Long.valueOf((long) this.mActivePointerId));
                    return;
                } else {
                    return;
                }
            } else {
                return;
            }
            recycleVelocityTracker();
        } else if (this.mAllowTouches) {
            initOrResetVelocityTracker();
            addMovementToVelocityTracker(motionEvent);
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mLastTouch.set(motionEvent.getRawX(), motionEvent.getRawY());
            this.mDownTouch.set(this.mLastTouch);
            this.mAllowDraggingOffscreen = true;
            this.mIsUserInteracting = true;
            long eventTime = motionEvent.getEventTime();
            this.mDownTouchTime = eventTime;
            if (this.mPreviouslyDragging || eventTime - this.mLastDownTouchTime >= 200) {
                z2 = false;
            }
            this.mIsDoubleTap = z2;
            this.mIsWaitingForDoubleTap = false;
            this.mIsDragging = false;
            this.mLastDownTouchTime = eventTime;
            Runnable runnable = this.mDoubleTapTimeoutCallback;
            if (runnable != null) {
                this.mMainExecutor.removeCallbacks(runnable);
            }
        }
    }

    public PointF getVelocity() {
        return this.mVelocity;
    }

    public PointF getLastTouchPosition() {
        return this.mLastTouch;
    }

    public PointF getLastTouchDelta() {
        return this.mLastDelta;
    }

    public PointF getDownTouchPosition() {
        return this.mDownTouch;
    }

    public boolean isDragging() {
        return this.mIsDragging;
    }

    public boolean isUserInteracting() {
        return this.mIsUserInteracting;
    }

    public boolean startedDragging() {
        return this.mStartedDragging;
    }

    public void setAllowTouches(boolean z) {
        this.mAllowTouches = z;
        if (this.mIsUserInteracting) {
            reset();
        }
    }

    public boolean isDoubleTap() {
        return this.mIsDoubleTap;
    }

    public boolean isWaitingForDoubleTap() {
        return this.mIsWaitingForDoubleTap;
    }

    public void scheduleDoubleTapTimeoutCallback() {
        if (this.mIsWaitingForDoubleTap) {
            long doubleTapTimeoutCallbackDelay = getDoubleTapTimeoutCallbackDelay();
            this.mMainExecutor.removeCallbacks(this.mDoubleTapTimeoutCallback);
            this.mMainExecutor.executeDelayed(this.mDoubleTapTimeoutCallback, doubleTapTimeoutCallbackDelay);
        }
    }

    @VisibleForTesting
    public long getDoubleTapTimeoutCallbackDelay() {
        if (this.mIsWaitingForDoubleTap) {
            return Math.max(0, 200 - (this.mUpTouchTime - this.mDownTouchTime));
        }
        return -1;
    }

    public void removeDoubleTapTimeoutCallback() {
        this.mIsWaitingForDoubleTap = false;
        this.mMainExecutor.removeCallbacks(this.mDoubleTapTimeoutCallback);
    }

    @VisibleForTesting
    public void scheduleHoverExitTimeoutCallback() {
        this.mMainExecutor.removeCallbacks(this.mHoverExitTimeoutCallback);
        this.mMainExecutor.executeDelayed(this.mHoverExitTimeoutCallback, 50);
    }

    public void removeHoverExitTimeoutCallback() {
        this.mMainExecutor.removeCallbacks(this.mHoverExitTimeoutCallback);
    }

    public void addMovementToVelocityTracker(MotionEvent motionEvent) {
        if (this.mVelocityTracker != null) {
            float rawX = motionEvent.getRawX() - motionEvent.getX();
            float rawY = motionEvent.getRawY() - motionEvent.getY();
            motionEvent.offsetLocation(rawX, rawY);
            this.mVelocityTracker.addMovement(motionEvent);
            motionEvent.offsetLocation(-rawX, -rawY);
        }
    }

    public final void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    public final void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + "PipTouchState");
        printWriter.println(str2 + "mAllowTouches=" + this.mAllowTouches);
        printWriter.println(str2 + "mActivePointerId=" + this.mActivePointerId);
        printWriter.println(str2 + "mLastTouchDisplayId=" + this.mLastTouchDisplayId);
        printWriter.println(str2 + "mDownTouch=" + this.mDownTouch);
        printWriter.println(str2 + "mDownDelta=" + this.mDownDelta);
        printWriter.println(str2 + "mLastTouch=" + this.mLastTouch);
        printWriter.println(str2 + "mLastDelta=" + this.mLastDelta);
        printWriter.println(str2 + "mVelocity=" + this.mVelocity);
        printWriter.println(str2 + "mIsUserInteracting=" + this.mIsUserInteracting);
        printWriter.println(str2 + "mIsDragging=" + this.mIsDragging);
        printWriter.println(str2 + "mStartedDragging=" + this.mStartedDragging);
        printWriter.println(str2 + "mAllowDraggingOffscreen=" + this.mAllowDraggingOffscreen);
    }
}