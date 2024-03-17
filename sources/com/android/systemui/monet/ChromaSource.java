package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class ChromaSource implements Chroma {
    public double get(@NotNull Cam cam) {
        return (double) cam.getChroma();
    }
}
