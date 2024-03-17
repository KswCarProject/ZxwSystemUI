package com.android.systemui.monet;

import androidx.constraintlayout.widget.R$styleable;
import com.android.internal.graphics.cam.Cam;
import com.android.systemui.monet.Hue;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class HueVibrantTertiary implements Hue {
    @NotNull
    public final List<Pair<Integer, Integer>> hueToRotations = CollectionsKt__CollectionsKt.listOf(new Pair(0, 35), new Pair(41, 30), new Pair(61, 20), new Pair(Integer.valueOf(R$styleable.Constraint_layout_goneMarginRight), 25), new Pair(131, 30), new Pair(181, 35), new Pair(251, 30), new Pair(301, 25), new Pair(360, 25));

    public double getHueRotation(float f, @NotNull List<Pair<Integer, Integer>> list) {
        return Hue.DefaultImpls.getHueRotation(this, f, list);
    }

    public double get(@NotNull Cam cam) {
        return getHueRotation(cam.getHue(), this.hueToRotations);
    }
}
