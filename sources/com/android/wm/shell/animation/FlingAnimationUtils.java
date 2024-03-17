package com.android.wm.shell.animation;

import android.animation.Animator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

public class FlingAnimationUtils {
    public AnimatorProperties mAnimatorProperties;
    public float mCachedStartGradient;
    public float mCachedVelocityFactor;
    public float mHighVelocityPxPerSecond;
    public PathInterpolator mInterpolator;
    public float mLinearOutSlowInX2;
    public float mMaxLengthSeconds;
    public float mMinVelocityPxPerSecond;
    public final float mSpeedUpFactor;
    public final float mY2;

    public static float interpolate(float f, float f2, float f3) {
        return (f * (1.0f - f3)) + (f2 * f3);
    }

    public FlingAnimationUtils(DisplayMetrics displayMetrics, float f) {
        this(displayMetrics, f, 0.0f);
    }

    public FlingAnimationUtils(DisplayMetrics displayMetrics, float f, float f2) {
        this(displayMetrics, f, f2, -1.0f, 1.0f);
    }

    public FlingAnimationUtils(DisplayMetrics displayMetrics, float f, float f2, float f3, float f4) {
        this.mAnimatorProperties = new AnimatorProperties();
        this.mCachedStartGradient = -1.0f;
        this.mCachedVelocityFactor = -1.0f;
        this.mMaxLengthSeconds = f;
        this.mSpeedUpFactor = f2;
        if (f3 < 0.0f) {
            this.mLinearOutSlowInX2 = interpolate(0.35f, 0.68f, f2);
        } else {
            this.mLinearOutSlowInX2 = f3;
        }
        this.mY2 = f4;
        float f5 = displayMetrics.density;
        this.mMinVelocityPxPerSecond = 250.0f * f5;
        this.mHighVelocityPxPerSecond = f5 * 3000.0f;
    }

    public void apply(Animator animator, float f, float f2, float f3) {
        apply(animator, f, f2, f3, Math.abs(f2 - f));
    }

    public void apply(Animator animator, float f, float f2, float f3, float f4) {
        AnimatorProperties properties = getProperties(f, f2, f3, f4);
        animator.setDuration(properties.mDuration);
        animator.setInterpolator(properties.mInterpolator);
    }

    public final AnimatorProperties getProperties(float f, float f2, float f3, float f4) {
        float f5 = f2 - f;
        float sqrt = (float) (((double) this.mMaxLengthSeconds) * Math.sqrt((double) (Math.abs(f5) / f4)));
        float abs = Math.abs(f5);
        float abs2 = Math.abs(f3);
        float f6 = 1.0f;
        if (this.mSpeedUpFactor != 0.0f) {
            f6 = Math.min(abs2 / 3000.0f, 1.0f);
        }
        float interpolate = interpolate(0.75f, this.mY2 / this.mLinearOutSlowInX2, f6);
        float f7 = (interpolate * abs) / abs2;
        Interpolator interpolator = getInterpolator(interpolate, f6);
        if (f7 <= sqrt) {
            this.mAnimatorProperties.mInterpolator = interpolator;
            sqrt = f7;
        } else if (abs2 >= this.mMinVelocityPxPerSecond) {
            this.mAnimatorProperties.mInterpolator = new InterpolatorInterpolator(new VelocityInterpolator(sqrt, abs2, abs), interpolator, Interpolators.LINEAR_OUT_SLOW_IN);
        } else {
            this.mAnimatorProperties.mInterpolator = Interpolators.FAST_OUT_SLOW_IN;
        }
        AnimatorProperties animatorProperties = this.mAnimatorProperties;
        animatorProperties.mDuration = (long) (sqrt * 1000.0f);
        return animatorProperties;
    }

    public final Interpolator getInterpolator(float f, float f2) {
        if (Float.isNaN(f2)) {
            Log.e("FlingAnimationUtils", "Invalid velocity factor", new Throwable());
            return Interpolators.LINEAR_OUT_SLOW_IN;
        }
        if (!(f == this.mCachedStartGradient && f2 == this.mCachedVelocityFactor)) {
            float f3 = this.mSpeedUpFactor * (1.0f - f2);
            float f4 = f3 * f;
            float f5 = this.mLinearOutSlowInX2;
            float f6 = this.mY2;
            try {
                this.mInterpolator = new PathInterpolator(f3, f4, f5, f6);
                this.mCachedStartGradient = f;
                this.mCachedVelocityFactor = f2;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Illegal path with x1=" + f3 + " y1=" + f4 + " x2=" + f5 + " y2=" + f6, e);
            }
        }
        return this.mInterpolator;
    }

    public void applyDismissing(Animator animator, float f, float f2, float f3, float f4) {
        AnimatorProperties dismissingProperties = getDismissingProperties(f, f2, f3, f4);
        animator.setDuration(dismissingProperties.mDuration);
        animator.setInterpolator(dismissingProperties.mInterpolator);
    }

    public final AnimatorProperties getDismissingProperties(float f, float f2, float f3, float f4) {
        float f5 = f2 - f;
        float pow = (float) (((double) this.mMaxLengthSeconds) * Math.pow((double) (Math.abs(f5) / f4), 0.5d));
        float abs = Math.abs(f5);
        float abs2 = Math.abs(f3);
        float calculateLinearOutFasterInY2 = calculateLinearOutFasterInY2(abs2);
        PathInterpolator pathInterpolator = new PathInterpolator(0.0f, 0.0f, 0.5f, calculateLinearOutFasterInY2);
        float f6 = ((calculateLinearOutFasterInY2 / 0.5f) * abs) / abs2;
        if (f6 <= pow) {
            this.mAnimatorProperties.mInterpolator = pathInterpolator;
            pow = f6;
        } else if (abs2 >= this.mMinVelocityPxPerSecond) {
            this.mAnimatorProperties.mInterpolator = new InterpolatorInterpolator(new VelocityInterpolator(pow, abs2, abs), pathInterpolator, Interpolators.LINEAR_OUT_SLOW_IN);
        } else {
            this.mAnimatorProperties.mInterpolator = Interpolators.FAST_OUT_LINEAR_IN;
        }
        AnimatorProperties animatorProperties = this.mAnimatorProperties;
        animatorProperties.mDuration = (long) (pow * 1000.0f);
        return animatorProperties;
    }

    public final float calculateLinearOutFasterInY2(float f) {
        float f2 = this.mMinVelocityPxPerSecond;
        float max = Math.max(0.0f, Math.min(1.0f, (f - f2) / (this.mHighVelocityPxPerSecond - f2)));
        return ((1.0f - max) * 0.4f) + (max * 0.5f);
    }

    public float getMinVelocityPxPerSecond() {
        return this.mMinVelocityPxPerSecond;
    }

    public float getHighVelocityPxPerSecond() {
        return this.mHighVelocityPxPerSecond;
    }

    public static final class InterpolatorInterpolator implements Interpolator {
        public Interpolator mCrossfader;
        public Interpolator mInterpolator1;
        public Interpolator mInterpolator2;

        public InterpolatorInterpolator(Interpolator interpolator, Interpolator interpolator2, Interpolator interpolator3) {
            this.mInterpolator1 = interpolator;
            this.mInterpolator2 = interpolator2;
            this.mCrossfader = interpolator3;
        }

        public float getInterpolation(float f) {
            float interpolation = this.mCrossfader.getInterpolation(f);
            return ((1.0f - interpolation) * this.mInterpolator1.getInterpolation(f)) + (interpolation * this.mInterpolator2.getInterpolation(f));
        }
    }

    public static final class VelocityInterpolator implements Interpolator {
        public float mDiff;
        public float mDurationSeconds;
        public float mVelocity;

        public VelocityInterpolator(float f, float f2, float f3) {
            this.mDurationSeconds = f;
            this.mVelocity = f2;
            this.mDiff = f3;
        }

        public float getInterpolation(float f) {
            return ((f * this.mDurationSeconds) * this.mVelocity) / this.mDiff;
        }
    }

    public static class AnimatorProperties {
        public long mDuration;
        public Interpolator mInterpolator;

        public AnimatorProperties() {
        }
    }

    public static class Builder {
        public final DisplayMetrics mDisplayMetrics;
        public float mMaxLengthSeconds;
        public float mSpeedUpFactor;
        public float mX2;
        public float mY2;

        public Builder(DisplayMetrics displayMetrics) {
            this.mDisplayMetrics = displayMetrics;
            reset();
        }

        public Builder setMaxLengthSeconds(float f) {
            this.mMaxLengthSeconds = f;
            return this;
        }

        public Builder setSpeedUpFactor(float f) {
            this.mSpeedUpFactor = f;
            return this;
        }

        public Builder setX2(float f) {
            this.mX2 = f;
            return this;
        }

        public Builder setY2(float f) {
            this.mY2 = f;
            return this;
        }

        public Builder reset() {
            this.mMaxLengthSeconds = 0.0f;
            this.mSpeedUpFactor = 0.0f;
            this.mX2 = -1.0f;
            this.mY2 = 1.0f;
            return this;
        }

        public FlingAnimationUtils build() {
            return new FlingAnimationUtils(this.mDisplayMetrics, this.mMaxLengthSeconds, this.mSpeedUpFactor, this.mX2, this.mY2);
        }
    }
}
