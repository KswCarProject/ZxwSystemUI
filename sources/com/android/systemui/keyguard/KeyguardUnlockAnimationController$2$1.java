package com.android.systemui.keyguard;

import android.animation.ValueAnimator;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardUnlockAnimationController.kt */
public final class KeyguardUnlockAnimationController$2$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ KeyguardUnlockAnimationController this$0;

    public KeyguardUnlockAnimationController$2$1(KeyguardUnlockAnimationController keyguardUnlockAnimationController) {
        this.this$0 = keyguardUnlockAnimationController;
    }

    public final void onAnimationUpdate(@NotNull ValueAnimator valueAnimator) {
        KeyguardUnlockAnimationController keyguardUnlockAnimationController = this.this$0;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            keyguardUnlockAnimationController.surfaceBehindAlpha = ((Float) animatedValue).floatValue();
            KeyguardUnlockAnimationController keyguardUnlockAnimationController2 = this.this$0;
            Object animatedValue2 = valueAnimator.getAnimatedValue();
            if (animatedValue2 != null) {
                keyguardUnlockAnimationController2.setSurfaceBehindAppearAmount(((Float) animatedValue2).floatValue());
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
