package com.android.systemui.monet;

import androidx.appcompat.R$styleable;
import com.android.internal.graphics.cam.Cam;
import com.android.systemui.monet.Hue;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class HueExpressiveTertiary implements Hue {
    @NotNull
    public final List<Pair<Integer, Integer>> hueToRotations;

    public HueExpressiveTertiary() {
        Integer valueOf = Integer.valueOf(R$styleable.AppCompatTheme_windowFixedHeightMajor);
        this.hueToRotations = CollectionsKt__CollectionsKt.listOf(new Pair(0, valueOf), new Pair(21, valueOf), new Pair(51, 20), new Pair(Integer.valueOf(R$styleable.AppCompatTheme_windowFixedHeightMinor), 45), new Pair(151, 20), new Pair(191, 15), new Pair(271, 20), new Pair(321, valueOf), new Pair(360, valueOf));
    }

    public double getHueRotation(float f, @NotNull List<Pair<Integer, Integer>> list) {
        return Hue.DefaultImpls.getHueRotation(this, f, list);
    }

    public double get(@NotNull Cam cam) {
        return getHueRotation(cam.getHue(), this.hueToRotations);
    }
}
