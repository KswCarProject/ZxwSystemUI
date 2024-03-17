package com.android.systemui.biometrics;

import com.android.systemui.biometrics.AuthController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AuthController$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AuthController.AnonymousClass3 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ AuthController$3$$ExternalSyntheticLambda0(AuthController.AnonymousClass3 r1, int i, int i2, boolean z) {
        this.f$0 = r1;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = z;
    }

    public final void run() {
        this.f$0.lambda$onEnrollmentsChanged$0(this.f$1, this.f$2, this.f$3);
    }
}
