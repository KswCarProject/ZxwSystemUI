package com.android.systemui.biometrics;

import android.graphics.PointF;
import android.util.Log;
import com.android.systemui.biometrics.UdfpsController;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$udfpsControllerCallback$1 implements UdfpsController.Callback {
    public final /* synthetic */ AuthRippleController this$0;

    public AuthRippleController$udfpsControllerCallback$1(AuthRippleController authRippleController) {
        this.this$0 = authRippleController;
    }

    public void onFingerDown() {
        if (this.this$0.getFingerprintSensorLocation() == null) {
            Log.e("AuthRipple", "fingerprintSensorLocation=null onFingerDown. Skip showing dwell ripple");
            return;
        }
        PointF fingerprintSensorLocation = this.this$0.getFingerprintSensorLocation();
        Intrinsics.checkNotNull(fingerprintSensorLocation);
        ((AuthRippleView) this.this$0.mView).setFingerprintSensorLocation(fingerprintSensorLocation, this.this$0.udfpsRadius);
        this.this$0.showDwellRipple();
    }

    public void onFingerUp() {
        ((AuthRippleView) this.this$0.mView).retractDwellRipple();
    }
}
