package com.android.systemui.biometrics;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AuthController$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AuthController f$0;

    public /* synthetic */ AuthController$1$$ExternalSyntheticLambda0(AuthController authController) {
        this.f$0 = authController;
    }

    public final void run() {
        this.f$0.cancelIfOwnerIsNotInForeground();
    }
}
