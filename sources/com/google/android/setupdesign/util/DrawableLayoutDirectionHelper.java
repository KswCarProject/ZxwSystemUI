package com.google.android.setupdesign.util;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.view.View;

public class DrawableLayoutDirectionHelper {
    @SuppressLint({"InlinedApi"})
    public static InsetDrawable createRelativeInsetDrawable(Drawable drawable, int i, int i2, int i3, int i4, View view) {
        boolean z = true;
        if (view.getLayoutDirection() != 1) {
            z = false;
        }
        return createRelativeInsetDrawable(drawable, i, i2, i3, i4, z);
    }

    public static InsetDrawable createRelativeInsetDrawable(Drawable drawable, int i, int i2, int i3, int i4, boolean z) {
        if (z) {
            return new InsetDrawable(drawable, i3, i2, i, i4);
        }
        return new InsetDrawable(drawable, i, i2, i3, i4);
    }
}
