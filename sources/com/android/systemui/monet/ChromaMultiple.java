package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class ChromaMultiple implements Chroma {
    public final double multiple;

    public ChromaMultiple(double d) {
        this.multiple = d;
    }

    public double get(@NotNull Cam cam) {
        return ((double) cam.getChroma()) * this.multiple;
    }
}
