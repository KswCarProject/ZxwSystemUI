package com.android.keyguard;

import android.hardware.biometrics.BiometricSourceType;
import com.android.keyguard.KeyguardBiometricLockoutLogger;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardBiometricLockoutLogger.kt */
public final class KeyguardBiometricLockoutLogger$mKeyguardUpdateMonitorCallback$1 extends KeyguardUpdateMonitorCallback {
    public final /* synthetic */ KeyguardBiometricLockoutLogger this$0;

    public KeyguardBiometricLockoutLogger$mKeyguardUpdateMonitorCallback$1(KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger) {
        this.this$0 = keyguardBiometricLockoutLogger;
    }

    public void onLockedOutStateChanged(@NotNull BiometricSourceType biometricSourceType) {
        if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
            boolean isFingerprintLockedOut = this.this$0.keyguardUpdateMonitor.isFingerprintLockedOut();
            if (isFingerprintLockedOut && !this.this$0.fingerprintLockedOut) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT);
            } else if (!isFingerprintLockedOut && this.this$0.fingerprintLockedOut) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT_RESET);
            }
            this.this$0.fingerprintLockedOut = isFingerprintLockedOut;
        } else if (biometricSourceType == BiometricSourceType.FACE) {
            boolean isFaceLockedOut = this.this$0.keyguardUpdateMonitor.isFaceLockedOut();
            if (isFaceLockedOut && !this.this$0.faceLockedOut) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT);
            } else if (!isFaceLockedOut && this.this$0.faceLockedOut) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT_RESET);
            }
            this.this$0.faceLockedOut = isFaceLockedOut;
        }
    }

    public void onStrongAuthStateChanged(int i) {
        if (i == KeyguardUpdateMonitor.getCurrentUser()) {
            int strongAuthForUser = this.this$0.keyguardUpdateMonitor.getStrongAuthTracker().getStrongAuthForUser(i);
            boolean isEncryptedOrLockdown = this.this$0.keyguardUpdateMonitor.isEncryptedOrLockdown(i);
            if (isEncryptedOrLockdown && !this.this$0.encryptedOrLockdown) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_ENCRYPTED_OR_LOCKDOWN);
            }
            this.this$0.encryptedOrLockdown = isEncryptedOrLockdown;
            boolean access$isUnattendedUpdate = this.this$0.isUnattendedUpdate(strongAuthForUser);
            if (access$isUnattendedUpdate && !this.this$0.unattendedUpdate) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_UNATTENDED_UPDATE);
            }
            this.this$0.unattendedUpdate = access$isUnattendedUpdate;
            boolean access$isStrongAuthTimeout = this.this$0.isStrongAuthTimeout(strongAuthForUser);
            if (access$isStrongAuthTimeout && !this.this$0.timeout) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_TIMEOUT);
            }
            this.this$0.timeout = access$isStrongAuthTimeout;
        }
    }
}
