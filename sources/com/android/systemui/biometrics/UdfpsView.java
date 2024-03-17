package com.android.systemui.biometrics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.android.systemui.doze.DozeReceiver;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UdfpsView.kt */
public final class UdfpsView extends FrameLayout implements DozeReceiver {
    @Nullable
    public UdfpsAnimationViewController<?> animationViewController;
    @Nullable
    public String debugMessage;
    @NotNull
    public final Paint debugTextPaint;
    public boolean halControlsIllumination;
    @Nullable
    public UdfpsHbmProvider hbmProvider;
    public boolean isIlluminationRequested;
    public final long onIlluminatedDelayMs;
    @NotNull
    public UdfpsOverlayParams overlayParams;
    @NotNull
    public final RectF sensorRect = new RectF();
    public final float sensorTouchAreaCoefficient;

    /* JADX WARNING: type inference failed for: r12v1, types: [java.lang.AutoCloseable, android.content.res.TypedArray] */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x006d, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x006e, code lost:
        kotlin.jdk7.AutoCloseableKt.closeFinally(r12, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0071, code lost:
        throw r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public UdfpsView(@org.jetbrains.annotations.NotNull android.content.Context r11, @org.jetbrains.annotations.Nullable android.util.AttributeSet r12) {
        /*
            r10 = this;
            r10.<init>(r11, r12)
            android.graphics.RectF r0 = new android.graphics.RectF
            r0.<init>()
            r10.sensorRect = r0
            android.graphics.Paint r0 = new android.graphics.Paint
            r0.<init>()
            r1 = 1
            r0.setAntiAlias(r1)
            r2 = -16776961(0xffffffffff0000ff, float:-1.7014636E38)
            r0.setColor(r2)
            r2 = 1107296256(0x42000000, float:32.0)
            r0.setTextSize(r2)
            r10.debugTextPaint = r0
            android.content.res.Resources$Theme r0 = r11.getTheme()
            int[] r2 = com.android.systemui.R$styleable.UdfpsView
            r3 = 0
            android.content.res.TypedArray r12 = r0.obtainStyledAttributes(r12, r2, r3, r3)
            int r0 = com.android.systemui.R$styleable.UdfpsView_sensorTouchAreaCoefficient     // Catch:{ all -> 0x006b }
            boolean r2 = r12.hasValue(r0)     // Catch:{ all -> 0x006b }
            if (r2 == 0) goto L_0x005f
            r2 = 0
            float r0 = r12.getFloat(r0, r2)     // Catch:{ all -> 0x006b }
            r2 = 0
            kotlin.jdk7.AutoCloseableKt.closeFinally(r12, r2)
            r10.sensorTouchAreaCoefficient = r0
            android.content.res.Resources r11 = r11.getResources()
            r12 = 17694956(0x10e00ec, float:2.6081942E-38)
            int r11 = r11.getInteger(r12)
            long r11 = (long) r11
            r10.onIlluminatedDelayMs = r11
            com.android.systemui.biometrics.UdfpsOverlayParams r11 = new com.android.systemui.biometrics.UdfpsOverlayParams
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 31
            r9 = 0
            r2 = r11
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)
            r10.overlayParams = r11
            r10.halControlsIllumination = r1
            return
        L_0x005f:
            java.lang.String r10 = "UdfpsView must contain sensorTouchAreaCoefficient"
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x006b }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x006b }
            r11.<init>(r10)     // Catch:{ all -> 0x006b }
            throw r11     // Catch:{ all -> 0x006b }
        L_0x006b:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x006d }
        L_0x006d:
            r11 = move-exception
            kotlin.jdk7.AutoCloseableKt.closeFinally(r12, r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsView.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    @Nullable
    public final UdfpsAnimationViewController<?> getAnimationViewController() {
        return this.animationViewController;
    }

    public final void setAnimationViewController(@Nullable UdfpsAnimationViewController<?> udfpsAnimationViewController) {
        this.animationViewController = udfpsAnimationViewController;
    }

    public final void setOverlayParams(@NotNull UdfpsOverlayParams udfpsOverlayParams) {
        this.overlayParams = udfpsOverlayParams;
    }

    public final boolean getHalControlsIllumination() {
        return this.halControlsIllumination;
    }

    public final void setHalControlsIllumination(boolean z) {
        this.halControlsIllumination = z;
    }

    public final void setDebugMessage(@Nullable String str) {
        this.debugMessage = str;
        postInvalidate();
    }

    public final boolean isIlluminationRequested() {
        return this.isIlluminationRequested;
    }

    public void setHbmProvider(@Nullable UdfpsHbmProvider udfpsHbmProvider) {
        this.hbmProvider = udfpsHbmProvider;
    }

    public boolean onInterceptTouchEvent(@NotNull MotionEvent motionEvent) {
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            Intrinsics.checkNotNull(udfpsAnimationViewController);
            return !udfpsAnimationViewController.shouldPauseAuth();
        }
    }

    public void dozeTimeTick() {
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.dozeTimeTick();
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        int i5 = 0;
        int paddingX = udfpsAnimationViewController == null ? 0 : udfpsAnimationViewController.getPaddingX();
        UdfpsAnimationViewController<?> udfpsAnimationViewController2 = this.animationViewController;
        if (udfpsAnimationViewController2 != null) {
            i5 = udfpsAnimationViewController2.getPaddingY();
        }
        this.sensorRect.set((float) paddingX, (float) i5, (float) (this.overlayParams.getSensorBounds().width() + paddingX), (float) (this.overlayParams.getSensorBounds().height() + i5));
        UdfpsAnimationViewController<?> udfpsAnimationViewController3 = this.animationViewController;
        if (udfpsAnimationViewController3 != null) {
            udfpsAnimationViewController3.onSensorRectUpdated(new RectF(this.sensorRect));
        }
    }

    public final void onTouchOutsideView() {
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onTouchOutsideView();
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.v("UdfpsView", "onAttachedToWindow");
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.v("UdfpsView", "onDetachedFromWindow");
    }

    public void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);
        if (!this.isIlluminationRequested) {
            String str = this.debugMessage;
            if (!(str == null || str.length() == 0)) {
                String str2 = this.debugMessage;
                Intrinsics.checkNotNull(str2);
                canvas.drawText(str2, 0.0f, 160.0f, this.debugTextPaint);
            }
        }
    }

    public final boolean isWithinSensorArea(float f, float f2) {
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        PointF touchTranslation = udfpsAnimationViewController == null ? null : udfpsAnimationViewController.getTouchTranslation();
        if (touchTranslation == null) {
            touchTranslation = new PointF(0.0f, 0.0f);
        }
        float centerX = this.sensorRect.centerX() + touchTranslation.x;
        float centerY = this.sensorRect.centerY() + touchTranslation.y;
        RectF rectF = this.sensorRect;
        float f3 = (rectF.right - rectF.left) / 2.0f;
        float f4 = (rectF.bottom - rectF.top) / 2.0f;
        float f5 = this.sensorTouchAreaCoefficient;
        if (f <= centerX - (f3 * f5) || f >= centerX + (f3 * f5) || f2 <= centerY - (f4 * f5) || f2 >= centerY + (f4 * f5)) {
            return false;
        }
        UdfpsAnimationViewController<?> udfpsAnimationViewController2 = this.animationViewController;
        if (!(udfpsAnimationViewController2 == null ? false : udfpsAnimationViewController2.shouldPauseAuth())) {
            return true;
        }
        return false;
    }

    public void startIllumination(@Nullable Runnable runnable) {
        this.isIlluminationRequested = true;
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStarting();
        }
        doIlluminate(runnable);
    }

    public final void doIlluminate(Runnable runnable) {
        UdfpsHbmProvider udfpsHbmProvider = this.hbmProvider;
        if (udfpsHbmProvider != null) {
            udfpsHbmProvider.enableHbm(this.halControlsIllumination, new UdfpsView$doIlluminate$1(runnable, this));
        }
    }

    public void stopIllumination() {
        this.isIlluminationRequested = false;
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStopped();
        }
        UdfpsHbmProvider udfpsHbmProvider = this.hbmProvider;
        if (udfpsHbmProvider != null) {
            udfpsHbmProvider.disableHbm((Runnable) null);
        }
    }
}
