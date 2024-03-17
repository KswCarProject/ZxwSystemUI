package com.android.wm.shell.animation;

import android.graphics.Rect;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import org.jetbrains.annotations.NotNull;

/* compiled from: FloatProperties.kt */
public final class FloatProperties$Companion$RECT_WIDTH$1 extends FloatPropertyCompat<Rect> {
    public FloatProperties$Companion$RECT_WIDTH$1() {
        super("RectWidth");
    }

    public float getValue(@NotNull Rect rect) {
        return (float) rect.width();
    }

    public void setValue(@NotNull Rect rect, float f) {
        rect.right = rect.left + ((int) f);
    }
}
