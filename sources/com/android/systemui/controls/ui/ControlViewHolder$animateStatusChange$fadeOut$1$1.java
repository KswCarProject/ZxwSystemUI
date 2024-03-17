package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlViewHolder.kt */
public final class ControlViewHolder$animateStatusChange$fadeOut$1$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ Function0<Unit> $statusRowUpdater;

    public ControlViewHolder$animateStatusChange$fadeOut$1$1(Function0<Unit> function0) {
        this.$statusRowUpdater = function0;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.$statusRowUpdater.invoke();
    }
}
