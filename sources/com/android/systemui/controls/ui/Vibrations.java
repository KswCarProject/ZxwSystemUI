package com.android.systemui.controls.ui;

import android.os.VibrationEffect;
import org.jetbrains.annotations.NotNull;

/* compiled from: Vibrations.kt */
public final class Vibrations {
    @NotNull
    public static final Vibrations INSTANCE;
    @NotNull
    public static final VibrationEffect rangeEdgeEffect;
    @NotNull
    public static final VibrationEffect rangeMiddleEffect;

    static {
        Vibrations vibrations = new Vibrations();
        INSTANCE = vibrations;
        rangeEdgeEffect = vibrations.initRangeEdgeEffect();
        rangeMiddleEffect = vibrations.initRangeMiddleEffect();
    }

    @NotNull
    public final VibrationEffect getRangeEdgeEffect() {
        return rangeEdgeEffect;
    }

    @NotNull
    public final VibrationEffect getRangeMiddleEffect() {
        return rangeMiddleEffect;
    }

    public final VibrationEffect initRangeEdgeEffect() {
        VibrationEffect.Composition startComposition = VibrationEffect.startComposition();
        startComposition.addPrimitive(7, 0.5f);
        return startComposition.compose();
    }

    public final VibrationEffect initRangeMiddleEffect() {
        VibrationEffect.Composition startComposition = VibrationEffect.startComposition();
        startComposition.addPrimitive(7, 0.1f);
        return startComposition.compose();
    }
}
