package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class HueAdd implements Hue {
    public final double amountDegrees;

    public HueAdd(double d) {
        this.amountDegrees = d;
    }

    public double get(@NotNull Cam cam) {
        return ColorScheme.Companion.wrapDegreesDouble(((double) cam.getHue()) + this.amountDegrees);
    }
}
