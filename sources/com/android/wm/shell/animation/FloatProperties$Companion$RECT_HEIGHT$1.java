package com.android.wm.shell.animation;

import android.graphics.Rect;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import org.jetbrains.annotations.NotNull;

/* compiled from: FloatProperties.kt */
public final class FloatProperties$Companion$RECT_HEIGHT$1 extends FloatPropertyCompat<Rect> {
    public FloatProperties$Companion$RECT_HEIGHT$1() {
        super("RectHeight");
    }

    public float getValue(@NotNull Rect rect) {
        return (float) rect.height();
    }

    public void setValue(@NotNull Rect rect, float f) {
        rect.bottom = rect.top + ((int) f);
    }
}
