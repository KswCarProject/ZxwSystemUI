package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlViewHolder.kt */
public final class ControlViewHolder$animateStatusChange$2$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ ControlViewHolder this$0;

    public ControlViewHolder$animateStatusChange$2$1(ControlViewHolder controlViewHolder) {
        this.this$0 = controlViewHolder;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.getStatus().setAlpha(1.0f);
        this.this$0.statusAnimator = null;
    }
}
