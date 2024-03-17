package com.android.systemui.biometrics;

import com.android.systemui.biometrics.UdfpsController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ UdfpsController.UdfpsOverlayController f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4(UdfpsController.UdfpsOverlayController udfpsOverlayController, int i, int i2, boolean z) {
        this.f$0 = udfpsOverlayController;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = z;
    }

    public final void run() {
        this.f$0.lambda$onAcquired$3(this.f$1, this.f$2, this.f$3);
    }
}
