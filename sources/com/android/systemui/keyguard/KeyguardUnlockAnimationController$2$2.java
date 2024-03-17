package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardUnlockAnimationController.kt */
public final class KeyguardUnlockAnimationController$2$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ KeyguardUnlockAnimationController this$0;

    public KeyguardUnlockAnimationController$2$2(KeyguardUnlockAnimationController keyguardUnlockAnimationController) {
        this.this$0 = keyguardUnlockAnimationController;
    }

    public void onAnimationEnd(@NotNull Animator animator) {
        this.this$0.setPlayingCannedUnlockAnimation(false);
        ((KeyguardViewMediator) this.this$0.keyguardViewMediator.get()).onKeyguardExitRemoteAnimationFinished(false);
    }
}
