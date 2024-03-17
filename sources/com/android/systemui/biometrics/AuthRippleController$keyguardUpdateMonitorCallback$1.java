package com.android.systemui.biometrics;

import android.hardware.biometrics.BiometricFingerprintConstants;
import android.hardware.biometrics.BiometricSourceType;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$keyguardUpdateMonitorCallback$1 extends KeyguardUpdateMonitorCallback {
    public final /* synthetic */ AuthRippleController this$0;

    public AuthRippleController$keyguardUpdateMonitorCallback$1(AuthRippleController authRippleController) {
        this.this$0 = authRippleController;
    }

    public void onBiometricAuthenticated(int i, @Nullable BiometricSourceType biometricSourceType, boolean z) {
        if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
            ((AuthRippleView) this.this$0.mView).fadeDwellRipple();
        }
        this.this$0.showUnlockRipple(biometricSourceType);
    }

    public void onBiometricAuthFailed(@Nullable BiometricSourceType biometricSourceType) {
        if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
            ((AuthRippleView) this.this$0.mView).retractDwellRipple();
        }
    }

    public void onBiometricAcquired(@Nullable BiometricSourceType biometricSourceType, int i) {
        if (biometricSourceType == BiometricSourceType.FINGERPRINT && BiometricFingerprintConstants.shouldTurnOffHbm(i) && i != 0) {
            ((AuthRippleView) this.this$0.mView).retractDwellRipple();
        }
    }

    public void onKeyguardBouncerStateChanged(boolean z) {
        if (z) {
            ((AuthRippleView) this.this$0.mView).fadeDwellRipple();
        }
    }
}
