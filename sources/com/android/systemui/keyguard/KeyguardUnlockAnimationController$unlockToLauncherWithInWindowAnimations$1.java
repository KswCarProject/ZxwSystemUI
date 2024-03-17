package com.android.systemui.keyguard;

import android.util.Log;

/* compiled from: KeyguardUnlockAnimationController.kt */
public final class KeyguardUnlockAnimationController$unlockToLauncherWithInWindowAnimations$1 implements Runnable {
    public final /* synthetic */ KeyguardUnlockAnimationController this$0;

    public KeyguardUnlockAnimationController$unlockToLauncherWithInWindowAnimations$1(KeyguardUnlockAnimationController keyguardUnlockAnimationController) {
        this.this$0 = keyguardUnlockAnimationController;
    }

    public final void run() {
        if (!((KeyguardViewMediator) this.this$0.keyguardViewMediator.get()).isShowingAndNotOccluded() || this.this$0.keyguardStateController.isKeyguardGoingAway()) {
            ((KeyguardViewMediator) this.this$0.keyguardViewMediator.get()).onKeyguardExitRemoteAnimationFinished(false);
        } else {
            Log.e("KeyguardUnlock", "Finish keyguard exit animation delayed Runnable ran, but we are showing and not going away.");
        }
    }
}
