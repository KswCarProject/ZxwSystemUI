package com.android.wm.shell.animation;

import android.graphics.RectF;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import org.jetbrains.annotations.Nullable;

/* compiled from: FloatProperties.kt */
public final class FloatProperties$Companion$RECTF_X$1 extends FloatPropertyCompat<RectF> {
    public FloatProperties$Companion$RECTF_X$1() {
        super("RectFX");
    }

    public void setValue(@Nullable RectF rectF, float f) {
        if (rectF != null) {
            rectF.offsetTo(f, rectF.top);
        }
    }

    public float getValue(@Nullable RectF rectF) {
        if (rectF == null) {
            return -3.4028235E38f;
        }
        return rectF.left;
    }
}
