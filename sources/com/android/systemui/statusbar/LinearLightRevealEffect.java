package com.android.systemui.statusbar;

import android.util.MathUtils;
import android.view.animation.Interpolator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.LightRevealEffect;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: LightRevealScrim.kt */
public final class LinearLightRevealEffect implements LightRevealEffect {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public final Interpolator INTERPOLATOR = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
    public final boolean isVertical;

    public LinearLightRevealEffect(boolean z) {
        this.isVertical = z;
    }

    public void setRevealAmountOnScrim(float f, @NotNull LightRevealScrim lightRevealScrim) {
        float interpolation = this.INTERPOLATOR.getInterpolation(f);
        lightRevealScrim.setInterpolatedRevealAmount(interpolation);
        LightRevealEffect.Companion companion = LightRevealEffect.Companion;
        lightRevealScrim.setStartColorAlpha(companion.getPercentPastThreshold(((float) 1) - interpolation, 0.7f));
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - companion.getPercentPastThreshold(interpolation, 0.6f));
        float lerp = MathUtils.lerp(0.3f, 1.0f, interpolation);
        if (this.isVertical) {
            lightRevealScrim.setRevealGradientBounds(((float) (lightRevealScrim.getWidth() / 2)) - (((float) (lightRevealScrim.getWidth() / 2)) * lerp), 0.0f, ((float) (lightRevealScrim.getWidth() / 2)) + (((float) (lightRevealScrim.getWidth() / 2)) * lerp), (float) lightRevealScrim.getHeight());
        } else {
            lightRevealScrim.setRevealGradientBounds(0.0f, ((float) (lightRevealScrim.getHeight() / 2)) - (((float) (lightRevealScrim.getHeight() / 2)) * lerp), (float) lightRevealScrim.getWidth(), ((float) (lightRevealScrim.getHeight() / 2)) + (((float) (lightRevealScrim.getHeight() / 2)) * lerp));
        }
    }

    /* compiled from: LightRevealScrim.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
