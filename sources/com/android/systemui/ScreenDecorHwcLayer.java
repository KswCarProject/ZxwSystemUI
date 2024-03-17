package com.android.systemui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.hardware.graphics.common.DisplayDecorationSupport;
import android.view.DisplayCutout;
import kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ScreenDecorHwcLayer.kt */
public final class ScreenDecorHwcLayer extends DisplayCutoutBaseView {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final boolean DEBUG_COLOR = ScreenDecorations.DEBUG_COLOR;
    public final int bgColor;
    @NotNull
    public final Paint clearPaint;
    public final int color;
    public final int colorMode;
    @NotNull
    public final ColorFilter cornerBgFilter;
    @NotNull
    public final ColorFilter cornerFilter;
    @Nullable
    public final Paint debugTransparentRegionPaint;
    public boolean hasBottomRoundedCorner;
    public boolean hasTopRoundedCorner;
    public int roundedCornerBottomSize;
    @Nullable
    public Drawable roundedCornerDrawableBottom;
    @Nullable
    public Drawable roundedCornerDrawableTop;
    public int roundedCornerTopSize;
    @NotNull
    public final Rect tempRect = new Rect();
    @NotNull
    public final Rect transparentRect = new Rect();
    public final boolean useInvertedAlphaColor;

    public ScreenDecorHwcLayer(@NotNull Context context, @NotNull DisplayDecorationSupport displayDecorationSupport) {
        super(context);
        if (displayDecorationSupport.format == 56) {
            if (DEBUG_COLOR) {
                this.color = -16711936;
                this.bgColor = 0;
                this.colorMode = 0;
                this.useInvertedAlphaColor = false;
                Paint paint = new Paint();
                paint.setColor(788594432);
                paint.setStyle(Paint.Style.FILL);
                this.debugTransparentRegionPaint = paint;
            } else {
                this.colorMode = 4;
                boolean z = displayDecorationSupport.alphaInterpretation == 0;
                this.useInvertedAlphaColor = z;
                if (z) {
                    this.color = 0;
                    this.bgColor = -16777216;
                } else {
                    this.color = -16777216;
                    this.bgColor = 0;
                }
                this.debugTransparentRegionPaint = null;
            }
            this.cornerFilter = new PorterDuffColorFilter(this.color, PorterDuff.Mode.SRC_IN);
            this.cornerBgFilter = new PorterDuffColorFilter(this.bgColor, PorterDuff.Mode.SRC_OUT);
            Paint paint2 = new Paint();
            this.clearPaint = paint2;
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            return;
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("Attempting to use unsupported mode ", PixelFormat.formatToString(displayDecorationSupport.format)));
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getParent().requestTransparentRegion(this);
        if (!DEBUG_COLOR) {
            getViewRootImpl().setDisplayDecoration(true);
        }
        if (this.useInvertedAlphaColor) {
            this.paint.set(this.clearPaint);
            return;
        }
        this.paint.setColor(this.color);
        this.paint.setStyle(Paint.Style.FILL);
    }

    public void onUpdate() {
        getParent().requestTransparentRegion(this);
    }

    public void onDraw(@NotNull Canvas canvas) {
        if (this.useInvertedAlphaColor) {
            canvas.drawColor(this.bgColor);
        }
        drawRoundedCorners(canvas);
        super.onDraw(canvas);
        Paint paint = this.debugTransparentRegionPaint;
        if (paint != null) {
            canvas.drawRect(this.transparentRect, paint);
        }
    }

    public boolean gatherTransparentRegion(@Nullable Region region) {
        if (region == null) {
            return false;
        }
        calculateTransparentRect();
        if (DEBUG_COLOR) {
            region.setEmpty();
            return false;
        }
        region.op(this.transparentRect, Region.Op.INTERSECT);
        return false;
    }

    public final void calculateTransparentRect() {
        this.transparentRect.set(0, 0, getWidth(), getHeight());
        removeCutoutFromTransparentRegion();
        removeCutoutProtectionFromTransparentRegion();
        removeRoundedCornersFromTransparentRegion();
    }

    public final void removeCutoutFromTransparentRegion() {
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if (displayCutout != null) {
            if (!displayCutout.getBoundingRectLeft().isEmpty()) {
                this.transparentRect.left = RangesKt___RangesKt.coerceAtLeast(displayCutout.getBoundingRectLeft().right, this.transparentRect.left);
            }
            if (!displayCutout.getBoundingRectTop().isEmpty()) {
                this.transparentRect.top = RangesKt___RangesKt.coerceAtLeast(displayCutout.getBoundingRectTop().bottom, this.transparentRect.top);
            }
            if (!displayCutout.getBoundingRectRight().isEmpty()) {
                this.transparentRect.right = RangesKt___RangesKt.coerceAtMost(displayCutout.getBoundingRectRight().left, this.transparentRect.right);
            }
            if (!displayCutout.getBoundingRectBottom().isEmpty()) {
                this.transparentRect.bottom = RangesKt___RangesKt.coerceAtMost(displayCutout.getBoundingRectBottom().top, this.transparentRect.bottom);
            }
        }
    }

    public final void removeCutoutProtectionFromTransparentRegion() {
        if (!this.protectionRect.isEmpty()) {
            float centerX = this.protectionRect.centerX();
            float centerY = this.protectionRect.centerY();
            float cameraProtectionProgress = (centerX - this.protectionRect.left) * getCameraProtectionProgress();
            float cameraProtectionProgress2 = (centerY - this.protectionRect.top) * getCameraProtectionProgress();
            this.tempRect.set((int) ((float) Math.floor((double) (centerX - cameraProtectionProgress))), (int) ((float) Math.floor((double) (centerY - cameraProtectionProgress2))), (int) ((float) Math.ceil((double) (centerX + cameraProtectionProgress))), (int) ((float) Math.ceil((double) (centerY + cameraProtectionProgress2))));
            Rect rect = this.tempRect;
            int i = rect.left;
            int i2 = rect.top;
            int width = getWidth() - this.tempRect.right;
            int height = getHeight() - this.tempRect.bottom;
            int minOf = ComparisonsKt___ComparisonsJvmKt.minOf(i, i2, width, height);
            if (minOf == i) {
                Rect rect2 = this.transparentRect;
                rect2.left = RangesKt___RangesKt.coerceAtLeast(this.tempRect.right, rect2.left);
            } else if (minOf == i2) {
                Rect rect3 = this.transparentRect;
                rect3.top = RangesKt___RangesKt.coerceAtLeast(this.tempRect.bottom, rect3.top);
            } else if (minOf == width) {
                Rect rect4 = this.transparentRect;
                rect4.right = RangesKt___RangesKt.coerceAtMost(this.tempRect.left, rect4.right);
            } else if (minOf == height) {
                Rect rect5 = this.transparentRect;
                rect5.bottom = RangesKt___RangesKt.coerceAtMost(this.tempRect.top, rect5.bottom);
            }
        }
    }

    public final void removeRoundedCornersFromTransparentRegion() {
        boolean z;
        boolean z2;
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if (displayCutout == null) {
            z2 = false;
            z = false;
        } else {
            z = !displayCutout.getBoundingRectTop().isEmpty() || !displayCutout.getBoundingRectBottom().isEmpty();
            z2 = !displayCutout.getBoundingRectLeft().isEmpty() || !displayCutout.getBoundingRectRight().isEmpty();
        }
        if (getWidth() < getHeight()) {
            if (z || !z2) {
                this.transparentRect.top = RangesKt___RangesKt.coerceAtLeast(getRoundedCornerSizeByPosition(1), this.transparentRect.top);
                this.transparentRect.bottom = RangesKt___RangesKt.coerceAtMost(getHeight() - getRoundedCornerSizeByPosition(3), this.transparentRect.bottom);
                return;
            }
            this.transparentRect.left = RangesKt___RangesKt.coerceAtLeast(getRoundedCornerSizeByPosition(0), this.transparentRect.left);
            this.transparentRect.right = RangesKt___RangesKt.coerceAtMost(getWidth() - getRoundedCornerSizeByPosition(2), this.transparentRect.right);
        } else if (!z || z2) {
            this.transparentRect.left = RangesKt___RangesKt.coerceAtLeast(getRoundedCornerSizeByPosition(0), this.transparentRect.left);
            this.transparentRect.right = RangesKt___RangesKt.coerceAtMost(getWidth() - getRoundedCornerSizeByPosition(2), this.transparentRect.right);
        } else {
            this.transparentRect.top = RangesKt___RangesKt.coerceAtLeast(getRoundedCornerSizeByPosition(1), this.transparentRect.top);
            this.transparentRect.bottom = RangesKt___RangesKt.coerceAtMost(getHeight() - getRoundedCornerSizeByPosition(3), this.transparentRect.bottom);
        }
    }

    public final int getRoundedCornerSizeByPosition(int i) {
        int displayRotation = ((getDisplayRotation() + 0) + i) % 4;
        if (displayRotation == 0) {
            return RangesKt___RangesKt.coerceAtLeast(this.roundedCornerTopSize, this.roundedCornerBottomSize);
        }
        if (displayRotation == 1) {
            return this.roundedCornerTopSize;
        }
        if (displayRotation == 2) {
            return RangesKt___RangesKt.coerceAtLeast(this.roundedCornerTopSize, this.roundedCornerBottomSize);
        }
        if (displayRotation == 3) {
            return this.roundedCornerBottomSize;
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("Incorrect position: ", Integer.valueOf(i)));
    }

    public final void drawRoundedCorners(Canvas canvas) {
        if (this.hasTopRoundedCorner || this.hasBottomRoundedCorner) {
            int i = 0;
            while (i < 4) {
                int i2 = i + 1;
                canvas.save();
                int roundedCornerRotationDegree = getRoundedCornerRotationDegree(i * 90);
                canvas.rotate((float) roundedCornerRotationDegree);
                canvas.translate((float) getRoundedCornerTranslationX(roundedCornerRotationDegree), (float) getRoundedCornerTranslationY(roundedCornerRotationDegree));
                if (this.hasTopRoundedCorner && (i == 0 || i == 1)) {
                    drawRoundedCorner(canvas, this.roundedCornerDrawableTop, this.roundedCornerTopSize);
                } else if (this.hasBottomRoundedCorner && (i == 3 || i == 2)) {
                    drawRoundedCorner(canvas, this.roundedCornerDrawableBottom, this.roundedCornerBottomSize);
                }
                canvas.restore();
                i = i2;
            }
        }
    }

    public final void drawRoundedCorner(Canvas canvas, Drawable drawable, int i) {
        if (this.useInvertedAlphaColor) {
            float f = (float) i;
            canvas.drawRect(0.0f, 0.0f, f, f, this.clearPaint);
            if (drawable != null) {
                drawable.setColorFilter(this.cornerBgFilter);
            }
        } else if (drawable != null) {
            drawable.setColorFilter(this.cornerFilter);
        }
        if (drawable != null) {
            drawable.draw(canvas);
        }
        if (drawable != null) {
            drawable.clearColorFilter();
        }
    }

    public final int getRoundedCornerRotationDegree(int i) {
        return ((i - (getDisplayRotation() * 90)) + 360) % 360;
    }

    public final int getRoundedCornerTranslationX(int i) {
        int i2;
        if (i == 0 || i == 90) {
            return 0;
        }
        if (i == 180) {
            i2 = getWidth();
        } else if (i == 270) {
            i2 = getHeight();
        } else {
            throw new IllegalArgumentException(Intrinsics.stringPlus("Incorrect degree: ", Integer.valueOf(i)));
        }
        return -i2;
    }

    public final int getRoundedCornerTranslationY(int i) {
        int i2;
        if (i != 0) {
            if (i == 90) {
                i2 = getWidth();
            } else if (i == 180) {
                i2 = getHeight();
            } else if (i != 270) {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Incorrect degree: ", Integer.valueOf(i)));
            }
            return -i2;
        }
        return 0;
    }

    public final void updateRoundedCornerDrawable(@NotNull Drawable drawable, @NotNull Drawable drawable2) {
        this.roundedCornerDrawableTop = drawable;
        this.roundedCornerDrawableBottom = drawable2;
        updateRoundedCornerDrawableBounds();
        invalidate();
    }

    public final void updateRoundedCornerExistenceAndSize(boolean z, boolean z2, int i, int i2) {
        if (this.hasTopRoundedCorner != z || this.hasBottomRoundedCorner != z2 || this.roundedCornerTopSize != i || this.roundedCornerBottomSize != i2) {
            this.hasTopRoundedCorner = z;
            this.hasBottomRoundedCorner = z2;
            this.roundedCornerTopSize = i;
            this.roundedCornerBottomSize = i2;
            updateRoundedCornerDrawableBounds();
            requestLayout();
        }
    }

    public final void updateRoundedCornerDrawableBounds() {
        Drawable drawable = this.roundedCornerDrawableTop;
        if (!(drawable == null || drawable == null)) {
            int i = this.roundedCornerTopSize;
            drawable.setBounds(0, 0, i, i);
        }
        Drawable drawable2 = this.roundedCornerDrawableBottom;
        if (!(drawable2 == null || drawable2 == null)) {
            int i2 = this.roundedCornerBottomSize;
            drawable2.setBounds(0, 0, i2, i2);
        }
        invalidate();
    }

    /* compiled from: ScreenDecorHwcLayer.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
