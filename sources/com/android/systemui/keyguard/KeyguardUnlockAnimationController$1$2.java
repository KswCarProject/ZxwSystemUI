package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardUnlockAnimationController.kt */
public final class KeyguardUnlockAnimationController$1$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ KeyguardUnlockAnimationController this$0;

    public KeyguardUnlockAnimationController$1$2(KeyguardUnlockAnimationController keyguardUnlockAnimationController) {
        this.this$0 = keyguardUnlockAnimationController;
    }

    public void onAnimationEnd(@NotNull Animator animator) {
        if (this.this$0.surfaceBehindAlpha == 0.0f) {
            ((KeyguardViewMediator) this.this$0.keyguardViewMediator.get()).finishSurfaceBehindRemoteAnimation(false);
        }
    }
}
