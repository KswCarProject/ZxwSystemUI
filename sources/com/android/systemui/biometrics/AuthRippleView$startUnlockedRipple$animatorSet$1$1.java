package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthRippleView.kt */
public final class AuthRippleView$startUnlockedRipple$animatorSet$1$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ Runnable $onAnimationEnd;
    public final /* synthetic */ AuthRippleView this$0;

    public AuthRippleView$startUnlockedRipple$animatorSet$1$1(AuthRippleView authRippleView, Runnable runnable) {
        this.this$0 = authRippleView;
        this.$onAnimationEnd = runnable;
    }

    public void onAnimationStart(@Nullable Animator animator) {
        this.this$0.unlockedRippleInProgress = true;
        this.this$0.rippleShader.setShouldFadeOutRipple(true);
        this.this$0.drawRipple = true;
        this.this$0.setVisibility(0);
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        Runnable runnable = this.$onAnimationEnd;
        if (runnable != null) {
            runnable.run();
        }
        this.this$0.unlockedRippleInProgress = false;
        this.this$0.drawRipple = false;
        this.this$0.setVisibility(8);
    }
}
