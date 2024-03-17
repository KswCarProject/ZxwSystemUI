package com.android.systemui.statusbar;

import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: LightRevealScrim.kt */
public interface LightRevealEffect {
    @NotNull
    public static final Companion Companion = Companion.$$INSTANCE;

    void setRevealAmountOnScrim(float f, @NotNull LightRevealScrim lightRevealScrim);

    /* compiled from: LightRevealScrim.kt */
    public static final class Companion {
        public static final /* synthetic */ Companion $$INSTANCE = new Companion();

        public final float getPercentPastThreshold(float f, float f2) {
            return RangesKt___RangesKt.coerceAtLeast(f - f2, 0.0f) * (1.0f / (1.0f - f2));
        }
    }
}
