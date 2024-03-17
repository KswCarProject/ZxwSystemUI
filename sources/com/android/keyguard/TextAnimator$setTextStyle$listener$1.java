package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: TextAnimator.kt */
public final class TextAnimator$setTextStyle$listener$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ Runnable $onAnimationEnd;
    public final /* synthetic */ TextAnimator this$0;

    public TextAnimator$setTextStyle$listener$1(Runnable runnable, TextAnimator textAnimator) {
        this.$onAnimationEnd = runnable;
        this.this$0 = textAnimator;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.$onAnimationEnd.run();
        this.this$0.getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().removeListener(this);
    }

    public void onAnimationCancel(@Nullable Animator animator) {
        this.this$0.getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().removeListener(this);
    }
}
