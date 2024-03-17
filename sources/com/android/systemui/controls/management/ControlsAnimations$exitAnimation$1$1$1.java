package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsAnimations.kt */
public final class ControlsAnimations$exitAnimation$1$1$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ Runnable $it;

    public ControlsAnimations$exitAnimation$1$1$1(Runnable runnable) {
        this.$it = runnable;
    }

    public void onAnimationEnd(@NotNull Animator animator) {
        this.$it.run();
    }
}
