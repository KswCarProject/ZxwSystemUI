package com.google.android.material.circularreveal;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.circularreveal.CircularRevealWidget;
import com.google.android.material.math.MathUtils;

public class CircularRevealHelper {
    public static final int STRATEGY = 2;
    public boolean buildingCircularRevealCache;
    public final Delegate delegate;
    public boolean hasCircularRevealCache;
    public Drawable overlayDrawable;
    public CircularRevealWidget.RevealInfo revealInfo;
    public final Paint revealPaint = new Paint(7);
    public final Path revealPath = new Path();
    public final Paint scrimPaint;
    public final View view;

    public interface Delegate {
        void actualDraw(Canvas canvas);

        boolean actualIsOpaque();
    }

    public CircularRevealHelper(Delegate delegate2) {
        this.delegate = delegate2;
        View view2 = (View) delegate2;
        this.view = view2;
        view2.setWillNotDraw(false);
        Paint paint = new Paint(1);
        this.scrimPaint = paint;
        paint.setColor(0);
    }

    public void buildCircularRevealCache() {
        if (STRATEGY == 0) {
            this.buildingCircularRevealCache = true;
            this.hasCircularRevealCache = false;
            this.view.buildDrawingCache();
            Bitmap drawingCache = this.view.getDrawingCache();
            if (!(drawingCache != null || this.view.getWidth() == 0 || this.view.getHeight() == 0)) {
                drawingCache = Bitmap.createBitmap(this.view.getWidth(), this.view.getHeight(), Bitmap.Config.ARGB_8888);
                this.view.draw(new Canvas(drawingCache));
            }
            if (drawingCache != null) {
                Paint paint = this.revealPaint;
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                paint.setShader(new BitmapShader(drawingCache, tileMode, tileMode));
            }
            this.buildingCircularRevealCache = false;
            this.hasCircularRevealCache = true;
        }
    }

    public void destroyCircularRevealCache() {
        if (STRATEGY == 0) {
            this.hasCircularRevealCache = false;
            this.view.destroyDrawingCache();
            this.revealPaint.setShader((Shader) null);
            this.view.invalidate();
        }
    }

    public void setRevealInfo(CircularRevealWidget.RevealInfo revealInfo2) {
        if (revealInfo2 == null) {
            this.revealInfo = null;
        } else {
            CircularRevealWidget.RevealInfo revealInfo3 = this.revealInfo;
            if (revealInfo3 == null) {
                this.revealInfo = new CircularRevealWidget.RevealInfo(revealInfo2);
            } else {
                revealInfo3.set(revealInfo2);
            }
            if (MathUtils.geq(revealInfo2.radius, getDistanceToFurthestCorner(revealInfo2), 1.0E-4f)) {
                this.revealInfo.radius = Float.MAX_VALUE;
            }
        }
        invalidateRevealInfo();
    }

    public CircularRevealWidget.RevealInfo getRevealInfo() {
        CircularRevealWidget.RevealInfo revealInfo2 = this.revealInfo;
        if (revealInfo2 == null) {
            return null;
        }
        CircularRevealWidget.RevealInfo revealInfo3 = new CircularRevealWidget.RevealInfo(revealInfo2);
        if (revealInfo3.isInvalid()) {
            revealInfo3.radius = getDistanceToFurthestCorner(revealInfo3);
        }
        return revealInfo3;
    }

    public void setCircularRevealScrimColor(int i) {
        this.scrimPaint.setColor(i);
        this.view.invalidate();
    }

    public int getCircularRevealScrimColor() {
        return this.scrimPaint.getColor();
    }

    public void setCircularRevealOverlayDrawable(Drawable drawable) {
        this.overlayDrawable = drawable;
        this.view.invalidate();
    }

    public final void invalidateRevealInfo() {
        if (STRATEGY == 1) {
            this.revealPath.rewind();
            CircularRevealWidget.RevealInfo revealInfo2 = this.revealInfo;
            if (revealInfo2 != null) {
                this.revealPath.addCircle(revealInfo2.centerX, revealInfo2.centerY, revealInfo2.radius, Path.Direction.CW);
            }
        }
        this.view.invalidate();
    }

    public final float getDistanceToFurthestCorner(CircularRevealWidget.RevealInfo revealInfo2) {
        return MathUtils.distanceToFurthestCorner(revealInfo2.centerX, revealInfo2.centerY, 0.0f, 0.0f, (float) this.view.getWidth(), (float) this.view.getHeight());
    }

    public void draw(Canvas canvas) {
        if (shouldDrawCircularReveal()) {
            int i = STRATEGY;
            if (i == 0) {
                CircularRevealWidget.RevealInfo revealInfo2 = this.revealInfo;
                canvas.drawCircle(revealInfo2.centerX, revealInfo2.centerY, revealInfo2.radius, this.revealPaint);
                if (shouldDrawScrim()) {
                    CircularRevealWidget.RevealInfo revealInfo3 = this.revealInfo;
                    canvas.drawCircle(revealInfo3.centerX, revealInfo3.centerY, revealInfo3.radius, this.scrimPaint);
                }
            } else if (i == 1) {
                int save = canvas.save();
                canvas.clipPath(this.revealPath);
                this.delegate.actualDraw(canvas);
                if (shouldDrawScrim()) {
                    canvas.drawRect(0.0f, 0.0f, (float) this.view.getWidth(), (float) this.view.getHeight(), this.scrimPaint);
                }
                canvas.restoreToCount(save);
            } else if (i == 2) {
                this.delegate.actualDraw(canvas);
                if (shouldDrawScrim()) {
                    canvas.drawRect(0.0f, 0.0f, (float) this.view.getWidth(), (float) this.view.getHeight(), this.scrimPaint);
                }
            } else {
                throw new IllegalStateException("Unsupported strategy " + i);
            }
        } else {
            this.delegate.actualDraw(canvas);
            if (shouldDrawScrim()) {
                canvas.drawRect(0.0f, 0.0f, (float) this.view.getWidth(), (float) this.view.getHeight(), this.scrimPaint);
            }
        }
        drawOverlayDrawable(canvas);
    }

    public final void drawOverlayDrawable(Canvas canvas) {
        if (shouldDrawOverlayDrawable()) {
            Rect bounds = this.overlayDrawable.getBounds();
            float width = this.revealInfo.centerX - (((float) bounds.width()) / 2.0f);
            float height = this.revealInfo.centerY - (((float) bounds.height()) / 2.0f);
            canvas.translate(width, height);
            this.overlayDrawable.draw(canvas);
            canvas.translate(-width, -height);
        }
    }

    public boolean isOpaque() {
        return this.delegate.actualIsOpaque() && !shouldDrawCircularReveal();
    }

    public final boolean shouldDrawCircularReveal() {
        CircularRevealWidget.RevealInfo revealInfo2 = this.revealInfo;
        boolean z = revealInfo2 == null || revealInfo2.isInvalid();
        if (STRATEGY != 0) {
            return !z;
        }
        if (z || !this.hasCircularRevealCache) {
            return false;
        }
        return true;
    }

    public final boolean shouldDrawScrim() {
        return !this.buildingCircularRevealCache && Color.alpha(this.scrimPaint.getColor()) != 0;
    }

    public final boolean shouldDrawOverlayDrawable() {
        return (this.buildingCircularRevealCache || this.overlayDrawable == null || this.revealInfo == null) ? false : true;
    }
}
