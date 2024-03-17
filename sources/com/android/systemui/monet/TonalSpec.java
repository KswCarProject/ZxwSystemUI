package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class TonalSpec {
    @NotNull
    public final Chroma chroma;
    @NotNull
    public final Hue hue;

    public TonalSpec(@NotNull Hue hue2, @NotNull Chroma chroma2) {
        this.hue = hue2;
        this.chroma = chroma2;
    }

    @NotNull
    public final List<Integer> shades(@NotNull Cam cam) {
        return ArraysKt___ArraysKt.toList(Shades.of((float) this.hue.get(cam), (float) this.chroma.get(cam)));
    }
}
