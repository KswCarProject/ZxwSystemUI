package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: TextAnimator.kt */
public final class TextAnimator$animator$1$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ TextAnimator this$0;

    public TextAnimator$animator$1$2(TextAnimator textAnimator) {
        this.this$0 = textAnimator;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().rebase();
    }

    public void onAnimationCancel(@Nullable Animator animator) {
        this.this$0.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().rebase();
    }
}
