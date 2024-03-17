package com.android.systemui.volume;

import android.animation.TimeInterpolator;

public final class SystemUIInterpolators$LogDecelerateInterpolator implements TimeInterpolator {
    public final float mBase;
    public final float mDrift;
    public final float mOutputScale;
    public final float mTimeScale;

    public SystemUIInterpolators$LogDecelerateInterpolator() {
        this(400.0f, 1.4f, 0.0f);
    }

    public SystemUIInterpolators$LogDecelerateInterpolator(float f, float f2, float f3) {
        this.mBase = f;
        this.mDrift = f3;
        this.mTimeScale = 1.0f / f2;
        this.mOutputScale = 1.0f / computeLog(1.0f);
    }

    public final float computeLog(float f) {
        return (1.0f - ((float) Math.pow((double) this.mBase, (double) ((-f) * this.mTimeScale)))) + (this.mDrift * f);
    }

    public float getInterpolation(float f) {
        return computeLog(f) * this.mOutputScale;
    }
}
