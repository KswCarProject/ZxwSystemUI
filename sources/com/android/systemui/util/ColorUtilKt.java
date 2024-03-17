package com.android.systemui.util;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorUtil.kt */
public final class ColorUtilKt {
    public static final int getColorWithAlpha(int i, float f) {
        return Color.argb((int) (f * ((float) 255)), Color.red(i), Color.green(i), Color.blue(i));
    }

    public static final int getPrivateAttrColorIfUnset(@NotNull ContextThemeWrapper contextThemeWrapper, @NotNull TypedArray typedArray, int i, int i2, int i3) {
        if (typedArray.hasValue(i)) {
            return typedArray.getColor(i, i2);
        }
        TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(new int[]{i3});
        int color = obtainStyledAttributes.getColor(0, i2);
        obtainStyledAttributes.recycle();
        return color;
    }
}
