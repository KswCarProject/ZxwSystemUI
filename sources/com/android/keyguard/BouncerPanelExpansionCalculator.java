package com.android.keyguard;

import android.util.MathUtils;
import org.jetbrains.annotations.NotNull;

/* compiled from: BouncerPanelExpansionCalculator.kt */
public final class BouncerPanelExpansionCalculator {
    @NotNull
    public static final BouncerPanelExpansionCalculator INSTANCE = new BouncerPanelExpansionCalculator();

    public static final float getDreamYPositionScaledExpansion(float f) {
        if (f >= 0.98f) {
            return 1.0f;
        }
        if (((double) f) < 0.93d) {
            return 0.0f;
        }
        return (f - 0.93f) / 0.05f;
    }

    public static final float showBouncerProgress(float f) {
        if (f >= 0.9f) {
            return 1.0f;
        }
        if (((double) f) < 0.6d) {
            return 0.0f;
        }
        return (f - 0.6f) / 0.3f;
    }

    public static final float aboutToShowBouncerProgress(float f) {
        return MathUtils.constrain((f - 0.9f) / 0.1f, 0.0f, 1.0f);
    }

    public static final float getKeyguardClockScaledExpansion(float f) {
        return MathUtils.constrain((f - 0.7f) / 0.3f, 0.0f, 1.0f);
    }

    public static final float getDreamAlphaScaledExpansion(float f) {
        return MathUtils.constrain((f - 0.94f) / 0.06f, 0.0f, 1.0f);
    }
}
