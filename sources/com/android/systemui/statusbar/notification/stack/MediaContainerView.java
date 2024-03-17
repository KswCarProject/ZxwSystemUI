package com.android.systemui.statusbar.notification.stack;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaContainerView.kt */
public final class MediaContainerView extends ExpandableView {
    public int clipHeight;
    @NotNull
    public Path clipPath = new Path();
    @NotNull
    public RectF clipRect = new RectF();
    public float cornerRadius;

    public void performAddAnimation(long j, long j2, boolean z, @Nullable Runnable runnable) {
    }

    public long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, @Nullable Runnable runnable, @Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        return 0;
    }

    public MediaContainerView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        setWillNotDraw(false);
        updateResources();
    }

    public void onConfigurationChanged(@Nullable Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    public final void updateResources() {
        this.cornerRadius = (float) getContext().getResources().getDimensionPixelSize(R$dimen.notification_corner_radius);
    }

    public void updateClipping() {
        if (this.clipHeight != getActualHeight()) {
            this.clipHeight = getActualHeight();
        }
        invalidate();
    }

    public void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);
        Rect clipBounds = canvas.getClipBounds();
        clipBounds.bottom = this.clipHeight;
        this.clipRect.set(clipBounds);
        this.clipPath.reset();
        Path path = this.clipPath;
        RectF rectF = this.clipRect;
        float f = this.cornerRadius;
        path.addRoundRect(rectF, f, f, Path.Direction.CW);
        canvas.clipPath(this.clipPath);
    }
}
