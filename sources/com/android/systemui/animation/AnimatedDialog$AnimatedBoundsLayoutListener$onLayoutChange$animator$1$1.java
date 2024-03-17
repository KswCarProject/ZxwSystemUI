package com.android.systemui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.android.systemui.animation.AnimatedDialog;
import org.jetbrains.annotations.NotNull;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$AnimatedBoundsLayoutListener$onLayoutChange$animator$1$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ AnimatedDialog.AnimatedBoundsLayoutListener this$0;

    public AnimatedDialog$AnimatedBoundsLayoutListener$onLayoutChange$animator$1$1(AnimatedDialog.AnimatedBoundsLayoutListener animatedBoundsLayoutListener) {
        this.this$0 = animatedBoundsLayoutListener;
    }

    public void onAnimationEnd(@NotNull Animator animator) {
        this.this$0.currentAnimator = null;
    }
}
