package com.android.wm.shell.animation;

import android.graphics.Rect;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import org.jetbrains.annotations.Nullable;

/* compiled from: FloatProperties.kt */
public final class FloatProperties$Companion$RECT_Y$1 extends FloatPropertyCompat<Rect> {
    public FloatProperties$Companion$RECT_Y$1() {
        super("RectY");
    }

    public void setValue(@Nullable Rect rect, float f) {
        if (rect != null) {
            rect.offsetTo(rect.left, (int) f);
        }
    }

    public float getValue(@Nullable Rect rect) {
        if (rect == null) {
            return -3.4028235E38f;
        }
        return (float) rect.top;
    }
}
