package com.android.systemui.biometrics;

import android.os.Bundle;
import android.view.WindowManager;

public interface AuthDialog {
    void animateToCredentialUI();

    void dismissFromSystemServer();

    void dismissWithoutCallback(boolean z);

    String getOpPackageName();

    long getRequestId();

    boolean isAllowDeviceCredentials();

    void onAuthenticationFailed(int i, String str);

    void onAuthenticationSucceeded(int i);

    void onError(int i, String str);

    void onHelp(int i, String str);

    void onOrientationChanged();

    void onPointerDown();

    void onSaveState(Bundle bundle);

    void show(WindowManager windowManager, Bundle bundle);

    public static class LayoutParams {
        public final int mMediumHeight;
        public final int mMediumWidth;

        public LayoutParams(int i, int i2) {
            this.mMediumWidth = i;
            this.mMediumHeight = i2;
        }
    }
}
