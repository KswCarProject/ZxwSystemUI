package com.android.systemui.biometrics;

import com.android.systemui.biometrics.AuthController;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AuthController$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AuthController.AnonymousClass2 f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ AuthController$2$$ExternalSyntheticLambda0(AuthController.AnonymousClass2 r1, List list) {
        this.f$0 = r1;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.lambda$onAllAuthenticatorsRegistered$0(this.f$1);
    }
}
