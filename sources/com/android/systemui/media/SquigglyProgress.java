package com.android.systemui.media;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.MathUtils;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.animation.Interpolators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SquigglyProgress.kt */
public final class SquigglyProgress extends Drawable {
    public boolean animate;
    @Nullable
    public ValueAnimator heightAnimator;
    public float heightFraction;
    public long lastFrameTime = -1;
    public float lineAmplitude;
    @NotNull
    public final Paint linePaint;
    public final float matchedWaveEndpoint = 0.6f;
    public final float minWaveEndpoint = 0.2f;
    @NotNull
    public final Path path = new Path();
    public float phaseOffset;
    public float phaseSpeed;
    public float strokeWidth;
    public boolean transitionEnabled = true;
    public final float transitionPeriods = 1.5f;
    public float waveLength;
    @NotNull
    public final Paint wavePaint;

    public int getOpacity() {
        return -3;
    }

    public SquigglyProgress() {
        Paint paint = new Paint();
        this.wavePaint = paint;
        Paint paint2 = new Paint();
        this.linePaint = paint2;
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.STROKE);
        paint2.setAlpha(77);
    }

    public final void setWaveLength(float f) {
        this.waveLength = f;
    }

    public final float getLineAmplitude() {
        return this.lineAmplitude;
    }

    public final void setLineAmplitude(float f) {
        this.lineAmplitude = f;
    }

    public final void setPhaseSpeed(float f) {
        this.phaseSpeed = f;
    }

    public final void setStrokeWidth(float f) {
        if (!(this.strokeWidth == f)) {
            this.strokeWidth = f;
            this.wavePaint.setStrokeWidth(f);
            this.linePaint.setStrokeWidth(f);
        }
    }

    public final void setTransitionEnabled(boolean z) {
        this.transitionEnabled = z;
        invalidateSelf();
    }

    public final boolean getAnimate() {
        return this.animate;
    }

    public final void setAnimate(boolean z) {
        if (this.animate != z) {
            this.animate = z;
            if (z) {
                this.lastFrameTime = SystemClock.uptimeMillis();
            }
            ValueAnimator valueAnimator = this.heightAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.heightFraction;
            fArr[1] = this.animate ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            if (getAnimate()) {
                ofFloat.setStartDelay(60);
                ofFloat.setDuration(800);
                ofFloat.setInterpolator(Interpolators.EMPHASIZED_DECELERATE);
            } else {
                ofFloat.setDuration(550);
                ofFloat.setInterpolator(Interpolators.STANDARD_DECELERATE);
            }
            ofFloat.addUpdateListener(new SquigglyProgress$animate$1$1(this));
            ofFloat.addListener(new SquigglyProgress$animate$1$2(this));
            ofFloat.start();
            this.heightAnimator = ofFloat;
        }
    }

    public void draw(@NotNull Canvas canvas) {
        Canvas canvas2 = canvas;
        if (this.animate) {
            invalidateSelf();
            long uptimeMillis = SystemClock.uptimeMillis();
            this.phaseOffset = (this.phaseOffset + ((((float) (uptimeMillis - this.lastFrameTime)) / 1000.0f) * this.phaseSpeed)) % this.waveLength;
            this.lastFrameTime = uptimeMillis;
        }
        float level = ((float) getLevel()) / 10000.0f;
        float width = ((float) getBounds().width()) * level;
        float width2 = (float) getBounds().width();
        if (this.transitionEnabled) {
            float f = this.matchedWaveEndpoint;
            if (level <= f) {
                level = MathUtils.lerp(this.minWaveEndpoint, f, MathUtils.lerpInv(0.0f, f, level));
            }
        }
        float f2 = width2 * level;
        float f3 = -this.phaseOffset;
        SquigglyProgress$draw$computeAmplitude$1 squigglyProgress$draw$computeAmplitude$1 = new SquigglyProgress$draw$computeAmplitude$1(this, f2, this.transitionEnabled ? this.transitionPeriods * this.waveLength : 0.01f);
        float f4 = (float) 2;
        float f5 = this.phaseOffset < this.waveLength / f4 ? 1.0f : -1.0f;
        this.path.rewind();
        this.path.moveTo((float) getBounds().width(), 0.0f);
        this.path.lineTo(f2, 0.0f);
        float f6 = f2 - (this.phaseOffset % (this.waveLength / f4));
        float floatValue = ((Number) squigglyProgress$draw$computeAmplitude$1.invoke(Float.valueOf(f6), Float.valueOf(f5))).floatValue();
        this.path.cubicTo(f2, floatValue * 0.25f, MathUtils.lerp(f6, f2, 0.25f), floatValue, f6, floatValue);
        float f7 = (((float) -1) * this.waveLength) / 2.0f;
        float f8 = floatValue;
        float f9 = f6;
        while (f9 > f3) {
            f5 = -f5;
            float f10 = f9 + f7;
            float f11 = f9 + (f7 / f4);
            float floatValue2 = ((Number) squigglyProgress$draw$computeAmplitude$1.invoke(Float.valueOf(f10), Float.valueOf(f5))).floatValue();
            this.path.cubicTo(f11, f8, f11, floatValue2, f10, floatValue2);
            f9 = f10;
            f8 = floatValue2;
        }
        canvas.save();
        canvas2.translate((float) getBounds().left, (float) getBounds().centerY());
        float f12 = this.lineAmplitude;
        float f13 = this.strokeWidth;
        canvas2.clipRect(0.0f, (-f12) - f13, width, f12 + f13);
        canvas2.drawPath(this.path, this.wavePaint);
        canvas.restore();
        canvas.save();
        canvas2.translate((float) getBounds().left, (float) getBounds().centerY());
        canvas2.clipRect(width, (-this.lineAmplitude) - this.strokeWidth, (float) getBounds().width(), this.lineAmplitude + this.strokeWidth);
        canvas2.drawPath(this.path, this.linePaint);
        canvas.restore();
        canvas2.drawPoint((float) getBounds().left, ((float) getBounds().centerY()) + (((float) Math.cos((double) ((Math.abs(f2 - this.phaseOffset) / this.waveLength) * 6.2831855f))) * this.lineAmplitude * this.heightFraction), this.wavePaint);
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        this.wavePaint.setColorFilter(colorFilter);
        this.linePaint.setColorFilter(colorFilter);
    }

    public void setAlpha(int i) {
        updateColors(this.wavePaint.getColor(), i);
    }

    public int getAlpha() {
        return this.wavePaint.getAlpha();
    }

    public void setTint(int i) {
        updateColors(i, getAlpha());
    }

    public boolean onLevelChange(int i) {
        return this.animate;
    }

    public void setTintList(@Nullable ColorStateList colorStateList) {
        if (colorStateList != null) {
            updateColors(colorStateList.getDefaultColor(), getAlpha());
        }
    }

    public final void updateColors(int i, int i2) {
        this.wavePaint.setColor(ColorUtils.setAlphaComponent(i, i2));
        this.linePaint.setColor(ColorUtils.setAlphaComponent(i, (int) (((float) 77) * (((float) i2) / 255.0f))));
    }
}
