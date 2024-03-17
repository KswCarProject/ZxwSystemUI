package com.android.systemui.statusbar.charging;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ChargingRippleView.kt */
public final class ChargingRippleView extends View {
    public final int defaultColor = -1;
    public long duration;
    @NotNull
    public PointF origin;
    public float radius;
    public boolean rippleInProgress;
    @NotNull
    public final Paint ripplePaint;
    @NotNull
    public final RippleShader rippleShader;

    public final void startRipple() {
        startRipple$default(this, (Runnable) null, 1, (Object) null);
    }

    /* JADX WARNING: type inference failed for: r4v1, types: [com.android.systemui.statusbar.charging.RippleShader, android.graphics.Shader] */
    public ChargingRippleView(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        ? rippleShader2 = new RippleShader();
        this.rippleShader = rippleShader2;
        Paint paint = new Paint();
        this.ripplePaint = paint;
        this.origin = new PointF();
        this.duration = 1750;
        rippleShader2.setColor(-1);
        rippleShader2.setProgress(0.0f);
        rippleShader2.setSparkleStrength(0.3f);
        paint.setShader(rippleShader2);
    }

    public final boolean getRippleInProgress() {
        return this.rippleInProgress;
    }

    public final void setRippleInProgress(boolean z) {
        this.rippleInProgress = z;
    }

    public final void setRadius(float f) {
        this.rippleShader.setRadius(f);
        this.radius = f;
    }

    public final void setOrigin(@NotNull PointF pointF) {
        this.rippleShader.setOrigin(pointF);
        this.origin = pointF;
    }

    public final void setDuration(long j) {
        this.duration = j;
    }

    public void onConfigurationChanged(@Nullable Configuration configuration) {
        this.rippleShader.setPixelDensity(getResources().getDisplayMetrics().density);
        super.onConfigurationChanged(configuration);
    }

    public void onAttachedToWindow() {
        this.rippleShader.setPixelDensity(getResources().getDisplayMetrics().density);
        super.onAttachedToWindow();
    }

    public static /* synthetic */ void startRipple$default(ChargingRippleView chargingRippleView, Runnable runnable, int i, Object obj) {
        if ((i & 1) != 0) {
            runnable = null;
        }
        chargingRippleView.startRipple(runnable);
    }

    public final void startRipple(@Nullable Runnable runnable) {
        if (!this.rippleInProgress) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.setDuration(this.duration);
            ofFloat.addUpdateListener(new ChargingRippleView$startRipple$1(this));
            ofFloat.addListener(new ChargingRippleView$startRipple$2(this, runnable));
            ofFloat.start();
            this.rippleInProgress = true;
        }
    }

    public final void setColor(int i) {
        this.rippleShader.setColor(i);
    }

    public void onDraw(@Nullable Canvas canvas) {
        if (canvas != null && canvas.isHardwareAccelerated()) {
            float f = (float) 1;
            float progress = (f - (((f - this.rippleShader.getProgress()) * (f - this.rippleShader.getProgress())) * (f - this.rippleShader.getProgress()))) * this.radius * ((float) 2);
            PointF pointF = this.origin;
            canvas.drawCircle(pointF.x, pointF.y, progress, this.ripplePaint);
        }
    }
}
