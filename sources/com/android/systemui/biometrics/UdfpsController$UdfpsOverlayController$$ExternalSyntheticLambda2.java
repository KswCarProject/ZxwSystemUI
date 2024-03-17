package com.android.systemui.biometrics;

import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import com.android.systemui.biometrics.UdfpsController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ UdfpsController.UdfpsOverlayController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ IUdfpsOverlayControllerCallback f$3;

    public /* synthetic */ UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda2(UdfpsController.UdfpsOverlayController udfpsOverlayController, long j, int i, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
        this.f$0 = udfpsOverlayController;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = iUdfpsOverlayControllerCallback;
    }

    public final void run() {
        this.f$0.lambda$showUdfpsOverlay$1(this.f$1, this.f$2, this.f$3);
    }
}
