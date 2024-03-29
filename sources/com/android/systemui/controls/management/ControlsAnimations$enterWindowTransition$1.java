package com.android.systemui.controls.management;

import android.animation.Animator;
import android.view.View;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsAnimations.kt */
public final class ControlsAnimations$enterWindowTransition$1 extends Lambda implements Function1<View, Animator> {
    public static final ControlsAnimations$enterWindowTransition$1 INSTANCE = new ControlsAnimations$enterWindowTransition$1();

    public ControlsAnimations$enterWindowTransition$1() {
        super(1);
    }

    @NotNull
    public final Animator invoke(@NotNull View view) {
        return ControlsAnimations.INSTANCE.enterAnimation(view);
    }
}
