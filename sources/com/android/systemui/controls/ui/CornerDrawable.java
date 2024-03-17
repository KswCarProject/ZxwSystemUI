package com.android.systemui.controls.ui;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import org.jetbrains.annotations.NotNull;

/* compiled from: CornerDrawable.kt */
public final class CornerDrawable extends DrawableWrapper {
    public final float cornerRadius;
    @NotNull
    public final Path path = new Path();
    @NotNull
    public final Drawable wrapped;

    public CornerDrawable(@NotNull Drawable drawable, float f) {
        super(drawable);
        this.wrapped = drawable;
        this.cornerRadius = f;
        updatePath(new RectF(getBounds()));
    }

    public void draw(@NotNull Canvas canvas) {
        canvas.clipPath(this.path);
        super.draw(canvas);
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        updatePath(new RectF((float) i, (float) i2, (float) i3, (float) i4));
        super.setBounds(i, i2, i3, i4);
    }

    public void setBounds(@NotNull Rect rect) {
        updatePath(new RectF(rect));
        super.setBounds(rect);
    }

    public final void updatePath(RectF rectF) {
        this.path.reset();
        Path path2 = this.path;
        float f = this.cornerRadius;
        path2.addRoundRect(rectF, f, f, Path.Direction.CW);
    }
}
