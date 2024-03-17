package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: SquigglyProgress.kt */
public final class SquigglyProgress$animate$1$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ SquigglyProgress this$0;

    public SquigglyProgress$animate$1$2(SquigglyProgress squigglyProgress) {
        this.this$0 = squigglyProgress;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.heightAnimator = null;
    }
}
