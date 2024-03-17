package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import java.util.List;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public interface Hue {
    double get(@NotNull Cam cam);

    /* compiled from: ColorScheme.kt */
    public static final class DefaultImpls {
        public static double getHueRotation(@NotNull Hue hue, float f, @NotNull List<Pair<Integer, Integer>> list) {
            int i = 0;
            float floatValue = ((f < 0.0f || f >= 360.0f) ? 0 : Float.valueOf(f)).floatValue();
            int size = list.size() - 2;
            if (size >= 0) {
                while (true) {
                    int i2 = i + 1;
                    float intValue = (float) ((Number) list.get(i2).getFirst()).intValue();
                    if (((float) ((Number) list.get(i).getFirst()).intValue()) <= floatValue && floatValue < intValue) {
                        return ColorScheme.Companion.wrapDegreesDouble(((double) floatValue) + ((Number) list.get(i).getSecond()).doubleValue());
                    }
                    if (i == size) {
                        break;
                    }
                    i = i2;
                }
            }
            return (double) f;
        }
    }
}
