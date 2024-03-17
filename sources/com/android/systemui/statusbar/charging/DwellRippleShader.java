package com.android.systemui.statusbar.charging;

import android.graphics.PointF;
import android.graphics.RuntimeShader;
import android.util.MathUtils;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: DwellRippleShader.kt */
public final class DwellRippleShader extends RuntimeShader {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public int color = 16777215;
    public float distortionStrength;
    public float maxRadius;
    @NotNull
    public PointF origin = new PointF();
    public float progress;
    public float time;

    /* compiled from: DwellRippleShader.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public DwellRippleShader() {
        super("uniform vec2 in_origin;\n                uniform float in_time;\n                uniform float in_radius;\n                uniform float in_blur;\n                layout(color) uniform vec4 in_color;\n                uniform float in_phase1;\n                uniform float in_phase2;\n                uniform float in_distortion_strength;\n                float softCircle(vec2 uv, vec2 xy, float radius, float blur) {\n                  float blurHalf = blur * 0.5;\n                  float d = distance(uv, xy);\n                  return 1. - smoothstep(1. - blurHalf, 1. + blurHalf, d / radius);\n                }\n\n                float softRing(vec2 uv, vec2 xy, float radius, float blur) {\n                  float thickness_half = radius * 0.25;\n                  float circle_outer = softCircle(uv, xy, radius + thickness_half, blur);\n                  float circle_inner = softCircle(uv, xy, radius - thickness_half, blur);\n                  return circle_outer - circle_inner;\n                }\n\n                vec2 distort(vec2 p, float time, float distort_amount_xy, float frequency) {\n                    return p + vec2(sin(p.y * frequency + in_phase1),\n                                    cos(p.x * frequency * -1.23 + in_phase2)) * distort_amount_xy;\n                }\n\n                vec4 ripple(vec2 p, float distort_xy, float frequency) {\n                    vec2 p_distorted = distort(p, in_time, distort_xy, frequency);\n                    float circle = softCircle(p_distorted, in_origin, in_radius * 1.2, in_blur);\n                    float rippleAlpha = max(circle,\n                        softRing(p_distorted, in_origin, in_radius, in_blur)) * 0.25;\n                    return in_color * rippleAlpha;\n                }\n                vec4 main(vec2 p) {\n                    vec4 color1 = ripple(p,\n                        34 * in_distortion_strength, // distort_xy\n                        0.012 // frequency\n                    );\n                    vec4 color2 = ripple(p,\n                        49 * in_distortion_strength, // distort_xy\n                        0.018 // frequency\n                    );\n                    // Alpha blend between two layers.\n                    return vec4(color1.xyz + color2.xyz\n                        * (1 - color1.w), color1.w + color2.w * (1-color1.w));\n                }");
    }

    public final void setMaxRadius(float f) {
        this.maxRadius = f;
    }

    public final void setOrigin(@NotNull PointF pointF) {
        this.origin = pointF;
        setFloatUniform("in_origin", pointF.x, pointF.y);
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        this.progress = f;
        float f2 = (float) 1;
        float f3 = f2 - f;
        setFloatUniform("in_radius", (f2 - ((f3 * f3) * f3)) * this.maxRadius);
        setFloatUniform("in_blur", MathUtils.lerp(1.0f, 0.7f, f));
    }

    public final void setDistortionStrength(float f) {
        this.distortionStrength = f;
        setFloatUniform("in_distortion_strength", f);
    }

    public final void setTime(float f) {
        float f2 = f * 0.001f;
        this.time = f2;
        setFloatUniform("in_time", f2);
        setFloatUniform("in_phase1", (this.time * 3.0f) + 0.367f);
        setFloatUniform("in_phase2", this.time * 7.2f * 1.531f);
    }

    public final int getColor() {
        return this.color;
    }

    public final void setColor(int i) {
        this.color = i;
        setColorUniform("in_color", i);
    }
}
