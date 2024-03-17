package com.android.systemui.biometrics;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UdfpsController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ UdfpsController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ float f$4;
    public final /* synthetic */ float f$5;

    public /* synthetic */ UdfpsController$$ExternalSyntheticLambda1(UdfpsController udfpsController, long j, int i, int i2, float f, float f2) {
        this.f$0 = udfpsController;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = f;
        this.f$5 = f2;
    }

    public final void run() {
        this.f$0.lambda$onFingerDown$3(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
