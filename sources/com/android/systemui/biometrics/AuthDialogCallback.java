package com.android.systemui.biometrics;

public interface AuthDialogCallback {
    void onDeviceCredentialPressed();

    void onDialogAnimatedIn();

    void onDismissed(int i, byte[] bArr);

    void onSystemEvent(int i);

    void onTryAgainPressed();
}
