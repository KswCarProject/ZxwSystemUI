package com.android.systemui.accessibility;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.RemoteException;
import android.util.Log;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import android.view.animation.AccelerateInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$integer;

public class WindowMagnificationAnimationController implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    public static final boolean DEBUG = Log.isLoggable("WindowMagnificationAnimationController", 3);
    public IRemoteMagnificationAnimationCallback mAnimationCallback;
    public final Context mContext;
    public WindowMagnificationController mController;
    public boolean mEndAnimationCanceled;
    public final AnimationSpec mEndSpec;
    public float mMagnificationFrameOffsetRatioX;
    public float mMagnificationFrameOffsetRatioY;
    public final AnimationSpec mStartSpec;
    public int mState;
    public final ValueAnimator mValueAnimator;

    public void onAnimationEnd(Animator animator) {
    }

    public void onAnimationRepeat(Animator animator) {
    }

    public WindowMagnificationAnimationController(Context context) {
        this(context, newValueAnimator(context.getResources()));
    }

    @VisibleForTesting
    public WindowMagnificationAnimationController(Context context, ValueAnimator valueAnimator) {
        this.mStartSpec = new AnimationSpec();
        this.mEndSpec = new AnimationSpec();
        this.mMagnificationFrameOffsetRatioX = 0.0f;
        this.mMagnificationFrameOffsetRatioY = 0.0f;
        this.mEndAnimationCanceled = false;
        this.mState = 0;
        this.mContext = context;
        this.mValueAnimator = valueAnimator;
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(this);
    }

    public void setWindowMagnificationController(WindowMagnificationController windowMagnificationController) {
        this.mController = windowMagnificationController;
    }

    public void enableWindowMagnification(float f, float f2, float f3, float f4, float f5, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        if (this.mController != null) {
            sendAnimationCallback(false);
            this.mMagnificationFrameOffsetRatioX = f4;
            this.mMagnificationFrameOffsetRatioY = f5;
            if (iRemoteMagnificationAnimationCallback == null) {
                int i = this.mState;
                if (i == 3 || i == 2) {
                    this.mValueAnimator.cancel();
                }
                this.mController.enableWindowMagnificationInternal(f, f2, f3, this.mMagnificationFrameOffsetRatioX, this.mMagnificationFrameOffsetRatioY);
                setState(1);
                return;
            }
            this.mAnimationCallback = iRemoteMagnificationAnimationCallback;
            setupEnableAnimationSpecs(f, f2, f3);
            if (this.mEndSpec.equals(this.mStartSpec)) {
                int i2 = this.mState;
                if (i2 == 0) {
                    this.mController.enableWindowMagnificationInternal(f, f2, f3, this.mMagnificationFrameOffsetRatioX, this.mMagnificationFrameOffsetRatioY);
                } else if (i2 == 3 || i2 == 2) {
                    this.mValueAnimator.cancel();
                }
                sendAnimationCallback(true);
                setState(1);
                return;
            }
            int i3 = this.mState;
            if (i3 == 2) {
                this.mValueAnimator.reverse();
            } else {
                if (i3 == 3) {
                    this.mValueAnimator.cancel();
                }
                this.mValueAnimator.start();
            }
            setState(3);
        }
    }

    public void moveWindowMagnifierToPosition(float f, float f2, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        int i = this.mState;
        if (i == 1) {
            this.mValueAnimator.setDuration((long) this.mContext.getResources().getInteger(17694720));
            enableWindowMagnification(Float.NaN, f, f2, Float.NaN, Float.NaN, iRemoteMagnificationAnimationCallback);
        } else if (i == 3) {
            sendAnimationCallback(false);
            this.mAnimationCallback = iRemoteMagnificationAnimationCallback;
            this.mValueAnimator.setDuration((long) this.mContext.getResources().getInteger(17694720));
            setupEnableAnimationSpecs(Float.NaN, f, f2);
        }
    }

    public final void setupEnableAnimationSpecs(float f, float f2, float f3) {
        WindowMagnificationController windowMagnificationController = this.mController;
        if (windowMagnificationController != null) {
            float scale = windowMagnificationController.getScale();
            float centerX = this.mController.getCenterX();
            float centerY = this.mController.getCenterY();
            if (this.mState == 0) {
                this.mStartSpec.set(1.0f, f2, f3);
                AnimationSpec animationSpec = this.mEndSpec;
                if (Float.isNaN(f)) {
                    f = (float) this.mContext.getResources().getInteger(R$integer.magnification_default_scale);
                }
                animationSpec.set(f, f2, f3);
            } else {
                this.mStartSpec.set(scale, centerX, centerY);
                if (this.mState == 3) {
                    scale = this.mEndSpec.mScale;
                }
                if (this.mState == 3) {
                    centerX = this.mEndSpec.mCenterX;
                }
                if (this.mState == 3) {
                    centerY = this.mEndSpec.mCenterY;
                }
                AnimationSpec animationSpec2 = this.mEndSpec;
                if (Float.isNaN(f)) {
                    f = scale;
                }
                if (Float.isNaN(f2)) {
                    f2 = centerX;
                }
                if (Float.isNaN(f3)) {
                    f3 = centerY;
                }
                animationSpec2.set(f, f2, f3);
            }
            if (DEBUG) {
                Log.d("WindowMagnificationAnimationController", "SetupEnableAnimationSpecs : mStartSpec = " + this.mStartSpec + ", endSpec = " + this.mEndSpec);
            }
        }
    }

    public boolean isAnimating() {
        return this.mValueAnimator.isRunning();
    }

    public void deleteWindowMagnification(IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        if (this.mController != null) {
            sendAnimationCallback(false);
            if (iRemoteMagnificationAnimationCallback == null) {
                int i = this.mState;
                if (i == 3 || i == 2) {
                    this.mValueAnimator.cancel();
                }
                this.mController.deleteWindowMagnification();
                setState(0);
                return;
            }
            this.mAnimationCallback = iRemoteMagnificationAnimationCallback;
            int i2 = this.mState;
            if (i2 != 0 && i2 != 2) {
                this.mStartSpec.set(1.0f, Float.NaN, Float.NaN);
                this.mEndSpec.set(this.mController.getScale(), Float.NaN, Float.NaN);
                this.mValueAnimator.reverse();
                setState(2);
            } else if (i2 == 0) {
                sendAnimationCallback(true);
            }
        }
    }

    public final void setState(int i) {
        if (DEBUG) {
            Log.d("WindowMagnificationAnimationController", "setState from " + this.mState + " to " + i);
        }
        this.mState = i;
    }

    public void onAnimationStart(Animator animator) {
        this.mEndAnimationCanceled = false;
    }

    public void onAnimationEnd(Animator animator, boolean z) {
        WindowMagnificationController windowMagnificationController;
        if (!this.mEndAnimationCanceled && (windowMagnificationController = this.mController) != null) {
            if (Float.isNaN(windowMagnificationController.getScale())) {
                setState(0);
            } else {
                setState(1);
            }
            sendAnimationCallback(true);
            this.mValueAnimator.setDuration((long) this.mContext.getResources().getInteger(17694722));
        }
    }

    public void onAnimationCancel(Animator animator) {
        this.mEndAnimationCanceled = true;
    }

    public final void sendAnimationCallback(boolean z) {
        IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback = this.mAnimationCallback;
        if (iRemoteMagnificationAnimationCallback != null) {
            try {
                iRemoteMagnificationAnimationCallback.onResult(z);
                if (DEBUG) {
                    Log.d("WindowMagnificationAnimationController", "sendAnimationCallback success = " + z);
                }
            } catch (RemoteException e) {
                Log.w("WindowMagnificationAnimationController", "sendAnimationCallback failed : " + e);
            }
            this.mAnimationCallback = null;
        }
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (this.mController != null) {
            float animatedFraction = valueAnimator.getAnimatedFraction();
            this.mController.enableWindowMagnificationInternal(this.mStartSpec.mScale + ((this.mEndSpec.mScale - this.mStartSpec.mScale) * animatedFraction), this.mStartSpec.mCenterX + ((this.mEndSpec.mCenterX - this.mStartSpec.mCenterX) * animatedFraction), this.mStartSpec.mCenterY + ((this.mEndSpec.mCenterY - this.mStartSpec.mCenterY) * animatedFraction), this.mMagnificationFrameOffsetRatioX, this.mMagnificationFrameOffsetRatioY);
        }
    }

    public static ValueAnimator newValueAnimator(Resources resources) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration((long) resources.getInteger(17694722));
        valueAnimator.setInterpolator(new AccelerateInterpolator(2.5f));
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        return valueAnimator;
    }

    public static class AnimationSpec {
        public float mCenterX;
        public float mCenterY;
        public float mScale;

        public AnimationSpec() {
            this.mScale = Float.NaN;
            this.mCenterX = Float.NaN;
            this.mCenterY = Float.NaN;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            AnimationSpec animationSpec = (AnimationSpec) obj;
            if (this.mScale == animationSpec.mScale && this.mCenterX == animationSpec.mCenterX && this.mCenterY == animationSpec.mCenterY) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            float f = this.mScale;
            int i = 0;
            int floatToIntBits = (f != 0.0f ? Float.floatToIntBits(f) : 0) * 31;
            float f2 = this.mCenterX;
            int floatToIntBits2 = (floatToIntBits + (f2 != 0.0f ? Float.floatToIntBits(f2) : 0)) * 31;
            float f3 = this.mCenterY;
            if (f3 != 0.0f) {
                i = Float.floatToIntBits(f3);
            }
            return floatToIntBits2 + i;
        }

        public void set(float f, float f2, float f3) {
            this.mScale = f;
            this.mCenterX = f2;
            this.mCenterY = f3;
        }

        public String toString() {
            return "AnimationSpec{mScale=" + this.mScale + ", mCenterX=" + this.mCenterX + ", mCenterY=" + this.mCenterY + '}';
        }
    }
}
