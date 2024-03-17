package com.android.systemui.monet;

import androidx.appcompat.R$styleable;
import com.android.internal.graphics.cam.Cam;
import com.android.systemui.monet.Hue;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class HueExpressiveSecondary implements Hue {
    @NotNull
    public final List<Pair<Integer, Integer>> hueToRotations = CollectionsKt__CollectionsKt.listOf(new Pair(0, 45), new Pair(21, 95), new Pair(51, 45), new Pair(Integer.valueOf(R$styleable.AppCompatTheme_windowFixedHeightMinor), 20), new Pair(151, 45), new Pair(191, 90), new Pair(271, 45), new Pair(321, 45), new Pair(360, 45));

    public double getHueRotation(float f, @NotNull List<Pair<Integer, Integer>> list) {
        return Hue.DefaultImpls.getHueRotation(this, f, list);
    }

    public double get(@NotNull Cam cam) {
        return getHueRotation(cam.getHue(), this.hueToRotations);
    }
}
