package com.android.systemui.statusbar;

import com.android.systemui.animation.Interpolators;
import org.jetbrains.annotations.NotNull;

/* compiled from: LightRevealScrim.kt */
public final class PowerButtonReveal implements LightRevealEffect {
    public final float OFF_SCREEN_START_AMOUNT = 0.05f;
    public final float WIDTH_INCREASE_MULTIPLIER = 1.25f;
    public final float powerButtonY;

    public PowerButtonReveal(float f) {
        this.powerButtonY = f;
    }

    public final float getPowerButtonY() {
        return this.powerButtonY;
    }

    public void setRevealAmountOnScrim(float f, @NotNull LightRevealScrim lightRevealScrim) {
        float interpolation = Interpolators.FAST_OUT_SLOW_IN_REVERSE.getInterpolation(f);
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - LightRevealEffect.Companion.getPercentPastThreshold(interpolation, 0.5f));
        lightRevealScrim.setInterpolatedRevealAmount(interpolation);
        lightRevealScrim.setRevealGradientBounds((((float) lightRevealScrim.getWidth()) * (this.OFF_SCREEN_START_AMOUNT + 1.0f)) - ((((float) lightRevealScrim.getWidth()) * this.WIDTH_INCREASE_MULTIPLIER) * interpolation), getPowerButtonY() - (((float) lightRevealScrim.getHeight()) * interpolation), (((float) lightRevealScrim.getWidth()) * (this.OFF_SCREEN_START_AMOUNT + 1.0f)) + (((float) lightRevealScrim.getWidth()) * this.WIDTH_INCREASE_MULTIPLIER * interpolation), getPowerButtonY() + (((float) lightRevealScrim.getHeight()) * interpolation));
    }
}
