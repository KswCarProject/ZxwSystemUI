package com.android.systemui.biometrics;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AuthController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ AuthController f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ AuthController$$ExternalSyntheticLambda2(AuthController authController, int i) {
        this.f$0 = authController;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$onBiometricError$1(this.f$1);
    }
}
