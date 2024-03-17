package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: ToggleRangeBehavior.kt */
public final class ToggleRangeBehavior$updateRange$1$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ ToggleRangeBehavior this$0;

    public ToggleRangeBehavior$updateRange$1$2(ToggleRangeBehavior toggleRangeBehavior) {
        this.this$0 = toggleRangeBehavior;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.rangeAnimator = null;
    }
}
