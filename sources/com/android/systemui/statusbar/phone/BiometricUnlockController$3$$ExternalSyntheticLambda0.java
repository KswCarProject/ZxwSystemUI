package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.BiometricUnlockController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BiometricUnlockController$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BiometricUnlockController.AnonymousClass3 f$0;
    public final /* synthetic */ BiometricUnlockController.PendingAuthenticated f$1;

    public /* synthetic */ BiometricUnlockController$3$$ExternalSyntheticLambda0(BiometricUnlockController.AnonymousClass3 r1, BiometricUnlockController.PendingAuthenticated pendingAuthenticated) {
        this.f$0 = r1;
        this.f$1 = pendingAuthenticated;
    }

    public final void run() {
        this.f$0.lambda$onFinishedGoingToSleep$0(this.f$1);
    }
}
