package com.android.systemui.util.view;

import android.view.View;
import org.jetbrains.annotations.NotNull;

/* compiled from: ViewUtil.kt */
public final class ViewUtil {
    public final boolean touchIsWithinView(@NotNull View view, float f, float f2) {
        int i = view.getLocationOnScreen()[0];
        int i2 = view.getLocationOnScreen()[1];
        if (((float) i) > f || f > ((float) (i + view.getWidth())) || ((float) i2) > f2 || f2 > ((float) (i2 + view.getHeight()))) {
            return false;
        }
        return true;
    }
}
