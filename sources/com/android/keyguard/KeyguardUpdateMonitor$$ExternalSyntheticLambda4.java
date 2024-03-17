package com.android.keyguard;

import android.hardware.face.FaceManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardUpdateMonitor$$ExternalSyntheticLambda4 implements FaceManager.FaceDetectionCallback {
    public final /* synthetic */ KeyguardUpdateMonitor f$0;

    public /* synthetic */ KeyguardUpdateMonitor$$ExternalSyntheticLambda4(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.f$0 = keyguardUpdateMonitor;
    }

    public final void onFaceDetected(int i, int i2, boolean z) {
        this.f$0.lambda$new$6(i, i2, z);
    }
}
