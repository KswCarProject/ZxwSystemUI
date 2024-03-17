package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Canvas;
import org.jetbrains.annotations.NotNull;

/* compiled from: UdfpsFpDrawable.kt */
public final class UdfpsFpDrawable extends UdfpsDrawable {
    public UdfpsFpDrawable(@NotNull Context context) {
        super(context);
    }

    public void draw(@NotNull Canvas canvas) {
        if (!isIlluminationShowing()) {
            getFingerprintDrawable().draw(canvas);
        }
    }
}
