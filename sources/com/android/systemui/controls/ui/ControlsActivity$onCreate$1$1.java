package com.android.systemui.controls.ui;

import android.view.View;
import android.view.WindowInsets;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsActivity.kt */
public final class ControlsActivity$onCreate$1$1 implements View.OnApplyWindowInsetsListener {
    public static final ControlsActivity$onCreate$1$1 INSTANCE = new ControlsActivity$onCreate$1$1();

    public final WindowInsets onApplyWindowInsets(@NotNull View view, @NotNull WindowInsets windowInsets) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsets.getInsets(WindowInsets.Type.systemBars()).bottom);
        return WindowInsets.CONSUMED;
    }
}
