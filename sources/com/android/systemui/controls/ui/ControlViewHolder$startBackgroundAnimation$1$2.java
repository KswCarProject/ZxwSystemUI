package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlViewHolder.kt */
public final class ControlViewHolder$startBackgroundAnimation$1$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ ControlViewHolder this$0;

    public ControlViewHolder$startBackgroundAnimation$1$2(ControlViewHolder controlViewHolder) {
        this.this$0 = controlViewHolder;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.stateAnimator = null;
    }
}
