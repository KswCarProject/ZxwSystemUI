package com.android.systemui.media;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaHierarchyManager.kt */
public final class MediaHierarchyManagerKt {
    @NotNull
    public static final Rect EMPTY_RECT = new Rect();

    public static final boolean isShownNotFaded(@NotNull View view) {
        ViewParent parent;
        while (view.getVisibility() == 0) {
            if ((view.getAlpha() == 0.0f) || (parent = view.getParent()) == null) {
                return false;
            }
            if (!(parent instanceof View)) {
                return true;
            }
            view = (View) parent;
        }
        return false;
    }
}
