package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class ChromaConstant implements Chroma {
    public final double chroma;

    public ChromaConstant(double d) {
        this.chroma = d;
    }

    public double get(@NotNull Cam cam) {
        return this.chroma;
    }
}
