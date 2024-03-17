package com.android.wm.shell.bubbles;

import android.graphics.drawable.GradientDrawable;
import android.util.IntProperty;
import org.jetbrains.annotations.NotNull;

/* compiled from: DismissView.kt */
public final class DismissView$GRADIENT_ALPHA$1 extends IntProperty<GradientDrawable> {
    public DismissView$GRADIENT_ALPHA$1() {
        super("alpha");
    }

    public void setValue(@NotNull GradientDrawable gradientDrawable, int i) {
        gradientDrawable.setAlpha(i);
    }

    @NotNull
    public Integer get(@NotNull GradientDrawable gradientDrawable) {
        return Integer.valueOf(gradientDrawable.getAlpha());
    }
}
