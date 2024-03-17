package com.android.systemui.biometrics;

import android.graphics.Point;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UdfpsController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ UdfpsController f$0;
    public final /* synthetic */ Point f$1;

    public /* synthetic */ UdfpsController$$ExternalSyntheticLambda0(UdfpsController udfpsController, Point point) {
        this.f$0 = udfpsController;
        this.f$1 = point;
    }

    public final void run() {
        this.f$0.lambda$onTouch$0(this.f$1);
    }
}
