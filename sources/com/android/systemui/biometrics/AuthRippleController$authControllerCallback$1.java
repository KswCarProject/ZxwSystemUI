package com.android.systemui.biometrics;

import com.android.systemui.biometrics.AuthController;

/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$authControllerCallback$1 implements AuthController.Callback {
    public final /* synthetic */ AuthRippleController this$0;

    public AuthRippleController$authControllerCallback$1(AuthRippleController authRippleController) {
        this.this$0 = authRippleController;
    }

    public void onAllAuthenticatorsRegistered() {
        this.this$0.updateUdfpsDependentParams();
        this.this$0.updateSensorLocation();
    }

    public void onUdfpsLocationChanged() {
        this.this$0.updateUdfpsDependentParams();
        this.this$0.updateSensorLocation();
    }
}
