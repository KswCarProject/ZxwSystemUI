package com.android.systemui.animation;

import android.graphics.Insets;
import android.view.View;
import android.view.WindowInsets;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$start$1 implements View.OnApplyWindowInsetsListener {
    public static final AnimatedDialog$start$1 INSTANCE = new AnimatedDialog$start$1();

    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        Insets insets = windowInsets.getInsets(WindowInsets.Type.displayCutout());
        view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
        return WindowInsets.CONSUMED;
    }
}
