package com.android.systemui.biometrics;

import android.view.MotionEvent;
import android.view.View;
import com.android.systemui.biometrics.UdfpsController;
import kotlin.jvm.functions.Function3;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda6 implements Function3 {
    public final /* synthetic */ UdfpsController.UdfpsOverlayController f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda6(UdfpsController.UdfpsOverlayController udfpsOverlayController, long j) {
        this.f$0 = udfpsOverlayController;
        this.f$1 = j;
    }

    public final Object invoke(Object obj, Object obj2, Object obj3) {
        return this.f$0.lambda$showUdfpsOverlay$0(this.f$1, (View) obj, (MotionEvent) obj2, (Boolean) obj3);
    }
}
