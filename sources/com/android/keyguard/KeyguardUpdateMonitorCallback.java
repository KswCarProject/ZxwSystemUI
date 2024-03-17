package com.android.keyguard;

import android.hardware.biometrics.BiometricSourceType;
import android.os.SystemClock;
import android.telephony.ServiceState;
import com.android.settingslib.fuelgauge.BatteryStatus;
import java.util.TimeZone;

public class KeyguardUpdateMonitorCallback {
    public boolean mShowing;
    public long mVisibilityChangedCalled;

    public void onBiometricAcquired(BiometricSourceType biometricSourceType, int i) {
    }

    public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
    }

    public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
    }

    public void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
    }

    public void onBiometricHelp(int i, String str, BiometricSourceType biometricSourceType) {
    }

    public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
    }

    public void onBiometricsCleared() {
    }

    public void onClockVisibilityChanged() {
    }

    public void onDevicePolicyManagerStateChanged() {
    }

    public void onDeviceProvisioned() {
    }

    public void onDreamingStateChanged(boolean z) {
    }

    public void onEmergencyCallAction() {
    }

    public void onFaceUnlockStateChanged(boolean z, int i) {
    }

    @Deprecated
    public void onFinishedGoingToSleep(int i) {
    }

    public void onKeyguardBouncerFullyShowingChanged(boolean z) {
    }

    public void onKeyguardBouncerStateChanged(boolean z) {
    }

    public void onKeyguardDismissAnimationFinished() {
    }

    public void onKeyguardOccludedChanged(boolean z) {
    }

    public void onKeyguardVisibilityChanged(boolean z) {
    }

    public void onLockedOutStateChanged(BiometricSourceType biometricSourceType) {
    }

    public void onLogoutEnabledChanged() {
    }

    public void onPhoneStateChanged(int i) {
    }

    public void onRefreshBatteryInfo(BatteryStatus batteryStatus) {
    }

    public void onRefreshCarrierInfo() {
    }

    public void onRequireUnlockForNfc() {
    }

    public void onSecondaryLockscreenRequirementChanged(int i) {
    }

    public void onServiceStateChanged(int i, ServiceState serviceState) {
    }

    public void onShadeExpandedChanged(boolean z) {
    }

    public void onSimStateChanged(int i, int i2, int i3) {
    }

    @Deprecated
    public void onStartedGoingToSleep(int i) {
    }

    @Deprecated
    public void onStartedWakingUp() {
    }

    public void onStrongAuthStateChanged(int i) {
    }

    public void onTelephonyCapable(boolean z) {
    }

    public void onTimeChanged() {
    }

    public void onTimeFormatChanged(String str) {
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
    }

    public void onTrustAgentErrorMessage(CharSequence charSequence) {
    }

    public void onTrustChanged(int i) {
    }

    public void onTrustGrantedWithFlags(int i, int i2) {
    }

    public void onTrustManagedChanged(int i) {
    }

    public void onUserInfoChanged(int i) {
    }

    public void onUserSwitchComplete(int i) {
    }

    public void onUserSwitching(int i) {
    }

    public void onUserUnlocked() {
    }

    public void showTrustGrantedMessage(CharSequence charSequence) {
    }

    public void onKeyguardVisibilityChangedRaw(boolean z) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (z != this.mShowing || elapsedRealtime - this.mVisibilityChangedCalled >= 1000) {
            onKeyguardVisibilityChanged(z);
            this.mVisibilityChangedCalled = elapsedRealtime;
            this.mShowing = z;
        }
    }
}
