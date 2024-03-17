package com.android.systemui.volume;

import android.animation.TimeInterpolator;

public final class SystemUIInterpolators$LogAccelerateInterpolator implements TimeInterpolator {
    public final int mBase;
    public final int mDrift;
    public final float mLogScale;

    public SystemUIInterpolators$LogAccelerateInterpolator() {
        this(100, 0);
    }

    public SystemUIInterpolators$LogAccelerateInterpolator(int i, int i2) {
        this.mBase = i;
        this.mDrift = i2;
        this.mLogScale = 1.0f / computeLog(1.0f, i, i2);
    }

    public static float computeLog(float f, int i, int i2) {
        return ((float) (-Math.pow((double) i, (double) (-f)))) + 1.0f + (((float) i2) * f);
    }

    public float getInterpolation(float f) {
        return 1.0f - (computeLog(1.0f - f, this.mBase, this.mDrift) * this.mLogScale);
    }
}
