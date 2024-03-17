package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: ViewGroupFadeHelper.kt */
public final class ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ Runnable $endRunnable;

    public ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$2(Runnable runnable) {
        this.$endRunnable = runnable;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        Runnable runnable = this.$endRunnable;
        if (runnable != null) {
            runnable.run();
        }
    }
}
