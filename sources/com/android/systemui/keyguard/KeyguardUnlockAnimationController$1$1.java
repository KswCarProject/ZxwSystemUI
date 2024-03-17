package com.android.systemui.keyguard;

import android.animation.ValueAnimator;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardUnlockAnimationController.kt */
public final class KeyguardUnlockAnimationController$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ KeyguardUnlockAnimationController this$0;

    public KeyguardUnlockAnimationController$1$1(KeyguardUnlockAnimationController keyguardUnlockAnimationController) {
        this.this$0 = keyguardUnlockAnimationController;
    }

    public final void onAnimationUpdate(@NotNull ValueAnimator valueAnimator) {
        KeyguardUnlockAnimationController keyguardUnlockAnimationController = this.this$0;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            keyguardUnlockAnimationController.surfaceBehindAlpha = ((Float) animatedValue).floatValue();
            this.this$0.updateSurfaceBehindAppearAmount();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
