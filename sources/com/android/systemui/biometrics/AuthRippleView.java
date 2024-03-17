package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.charging.DwellRippleShader;
import com.android.systemui.statusbar.charging.RippleShader;
import kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthRippleView.kt */
public final class AuthRippleView extends View {
    public long alphaInDuration;
    public boolean drawDwell;
    public boolean drawRipple;
    public final long dwellExpandDuration = (2000 - 100);
    @NotNull
    public PointF dwellOrigin;
    @NotNull
    public final Paint dwellPaint;
    public final long dwellPulseDuration = 100;
    @Nullable
    public Animator dwellPulseOutAnimator;
    public float dwellRadius;
    @NotNull
    public final DwellRippleShader dwellShader;
    public final long fadeDuration = 83;
    @Nullable
    public Animator fadeDwellAnimator;
    public int lockScreenColorVal = -1;
    @NotNull
    public PointF origin;
    public float radius;
    public final long retractDuration = 400;
    @Nullable
    public Animator retractDwellAnimator;
    @NotNull
    public final PathInterpolator retractInterpolator = new PathInterpolator(0.05f, 0.93f, 0.1f, 1.0f);
    @NotNull
    public final Paint ripplePaint;
    @NotNull
    public final RippleShader rippleShader;
    public boolean unlockedRippleInProgress;

    /* JADX WARNING: type inference failed for: r7v2, types: [android.graphics.Shader, com.android.systemui.statusbar.charging.DwellRippleShader] */
    /* JADX WARNING: type inference failed for: r1v1, types: [com.android.systemui.statusbar.charging.RippleShader, android.graphics.Shader] */
    public AuthRippleView(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        ? dwellRippleShader = new DwellRippleShader();
        this.dwellShader = dwellRippleShader;
        Paint paint = new Paint();
        this.dwellPaint = paint;
        ? rippleShader2 = new RippleShader();
        this.rippleShader = rippleShader2;
        Paint paint2 = new Paint();
        this.ripplePaint = paint2;
        this.dwellOrigin = new PointF();
        this.origin = new PointF();
        rippleShader2.setColor(-1);
        rippleShader2.setProgress(0.0f);
        rippleShader2.setSparkleStrength(0.4f);
        paint2.setShader(rippleShader2);
        dwellRippleShader.setColor(-1);
        dwellRippleShader.setProgress(0.0f);
        dwellRippleShader.setDistortionStrength(0.4f);
        paint.setShader(dwellRippleShader);
        setVisibility(8);
    }

    public final void setDwellRadius(float f) {
        this.dwellShader.setMaxRadius(f);
        this.dwellRadius = f;
    }

    public final void setDwellOrigin(PointF pointF) {
        this.dwellShader.setOrigin(pointF);
        this.dwellOrigin = pointF;
    }

    public final void setRadius(float f) {
        this.rippleShader.setRadius(f);
        this.radius = f;
    }

    public final void setOrigin(PointF pointF) {
        this.rippleShader.setOrigin(pointF);
        this.origin = pointF;
    }

    public final void setSensorLocation(@NotNull PointF pointF) {
        setOrigin(pointF);
        setRadius(ComparisonsKt___ComparisonsJvmKt.maxOf(pointF.x, pointF.y, ((float) getWidth()) - pointF.x, ((float) getHeight()) - pointF.y));
    }

    public final void setFingerprintSensorLocation(@NotNull PointF pointF, float f) {
        setOrigin(pointF);
        setRadius(ComparisonsKt___ComparisonsJvmKt.maxOf(pointF.x, pointF.y, ((float) getWidth()) - pointF.x, ((float) getHeight()) - pointF.y));
        setDwellOrigin(pointF);
        setDwellRadius(f * 1.5f);
    }

    public final void setAlphaInDuration(long j) {
        this.alphaInDuration = j;
    }

    public final void retractDwellRipple() {
        Animator animator = this.retractDwellAnimator;
        if (!(animator != null && animator.isRunning())) {
            Animator animator2 = this.fadeDwellAnimator;
            if (!(animator2 != null && animator2.isRunning())) {
                Animator animator3 = this.dwellPulseOutAnimator;
                if (animator3 != null && animator3.isRunning()) {
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.dwellShader.getProgress(), 0.0f});
                    ofFloat.setInterpolator(this.retractInterpolator);
                    ofFloat.setDuration(this.retractDuration);
                    ofFloat.addUpdateListener(new AuthRippleView$retractDwellRipple$retractDwellRippleAnimator$1$1(this));
                    ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{255, 0});
                    ofInt.setInterpolator(Interpolators.LINEAR);
                    ofInt.setDuration(this.retractDuration);
                    ofInt.addUpdateListener(new AuthRippleView$retractDwellRipple$retractAlphaAnimator$1$1(this));
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(new Animator[]{ofFloat, ofInt});
                    animatorSet.addListener(new AuthRippleView$retractDwellRipple$1$1(this));
                    animatorSet.start();
                    this.retractDwellAnimator = animatorSet;
                }
            }
        }
    }

    public final void fadeDwellRipple() {
        Animator animator = this.fadeDwellAnimator;
        if (!(animator != null && animator.isRunning())) {
            Animator animator2 = this.dwellPulseOutAnimator;
            if (!(animator2 != null && animator2.isRunning())) {
                Animator animator3 = this.retractDwellAnimator;
                if (!(animator3 != null && animator3.isRunning())) {
                    return;
                }
            }
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{Color.alpha(this.dwellShader.getColor()), 0});
            ofInt.setInterpolator(Interpolators.LINEAR);
            ofInt.setDuration(this.fadeDuration);
            ofInt.addUpdateListener(new AuthRippleView$fadeDwellRipple$1$1(this));
            ofInt.addListener(new AuthRippleView$fadeDwellRipple$1$2(this));
            ofInt.start();
            this.fadeDwellAnimator = ofInt;
        }
    }

    public final void startDwellRipple(boolean z) {
        if (!this.unlockedRippleInProgress) {
            Animator animator = this.dwellPulseOutAnimator;
            if (!(animator != null && animator.isRunning())) {
                updateDwellRippleColor(z);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 0.8f});
                ofFloat.setInterpolator(Interpolators.LINEAR);
                ofFloat.setDuration(this.dwellPulseDuration);
                ofFloat.addUpdateListener(new AuthRippleView$startDwellRipple$dwellPulseOutRippleAnimator$1$1(this));
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.8f, 1.0f});
                ofFloat2.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat2.setDuration(this.dwellExpandDuration);
                ofFloat2.addUpdateListener(new AuthRippleView$startDwellRipple$expandDwellRippleAnimator$1$1(this));
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(new Animator[]{ofFloat, ofFloat2});
                animatorSet.addListener(new AuthRippleView$startDwellRipple$1$1(this));
                animatorSet.start();
                this.dwellPulseOutAnimator = animatorSet;
            }
        }
    }

    public final void startUnlockedRipple(@Nullable Runnable runnable) {
        if (!this.unlockedRippleInProgress) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
            ofFloat.setDuration(1533);
            ofFloat.addUpdateListener(new AuthRippleView$startUnlockedRipple$rippleAnimator$1$1(this));
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{0, 255});
            ofInt.setDuration(this.alphaInDuration);
            ofInt.addUpdateListener(new AuthRippleView$startUnlockedRipple$alphaInAnimator$1$1(this));
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(new Animator[]{ofFloat, ofInt});
            animatorSet.addListener(new AuthRippleView$startUnlockedRipple$animatorSet$1$1(this, runnable));
            animatorSet.start();
        }
    }

    public final void resetRippleAlpha() {
        RippleShader rippleShader2 = this.rippleShader;
        rippleShader2.setColor(ColorUtils.setAlphaComponent(rippleShader2.getColor(), 255));
    }

    public final void setLockScreenColor(int i) {
        this.lockScreenColorVal = i;
        this.rippleShader.setColor(i);
        resetRippleAlpha();
    }

    public final void updateDwellRippleColor(boolean z) {
        if (z) {
            this.dwellShader.setColor(-1);
        } else {
            this.dwellShader.setColor(this.lockScreenColorVal);
        }
        resetDwellAlpha();
    }

    public final void resetDwellAlpha() {
        DwellRippleShader dwellRippleShader = this.dwellShader;
        dwellRippleShader.setColor(ColorUtils.setAlphaComponent(dwellRippleShader.getColor(), 255));
    }

    public void onDraw(@Nullable Canvas canvas) {
        if (this.drawDwell) {
            float f = (float) 1;
            float progress = (f - (((f - this.dwellShader.getProgress()) * (f - this.dwellShader.getProgress())) * (f - this.dwellShader.getProgress()))) * this.dwellRadius * 2.0f;
            if (canvas != null) {
                PointF pointF = this.dwellOrigin;
                canvas.drawCircle(pointF.x, pointF.y, progress, this.dwellPaint);
            }
        }
        if (this.drawRipple) {
            float f2 = (float) 1;
            float progress2 = (f2 - (((f2 - this.rippleShader.getProgress()) * (f2 - this.rippleShader.getProgress())) * (f2 - this.rippleShader.getProgress()))) * this.radius * 2.0f;
            if (canvas != null) {
                PointF pointF2 = this.origin;
                canvas.drawCircle(pointF2.x, pointF2.y, progress2, this.ripplePaint);
            }
        }
    }
}
