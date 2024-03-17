package com.android.systemui.biometrics;

import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.util.Log;

/* compiled from: UdfpsShell.kt */
public final class UdfpsShell$showOverlay$1 extends IUdfpsOverlayControllerCallback.Stub {
    public void onUserCanceled() {
        Log.e("UdfpsShell", "User cancelled");
    }
}
