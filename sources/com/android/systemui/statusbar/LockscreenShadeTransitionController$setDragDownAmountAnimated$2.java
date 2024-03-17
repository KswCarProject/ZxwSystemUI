package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.Nullable;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$setDragDownAmountAnimated$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ Function0<Unit> $endlistener;

    public LockscreenShadeTransitionController$setDragDownAmountAnimated$2(Function0<Unit> function0) {
        this.$endlistener = function0;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.$endlistener.invoke();
    }
}
