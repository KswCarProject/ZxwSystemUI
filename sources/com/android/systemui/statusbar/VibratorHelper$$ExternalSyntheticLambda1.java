package com.android.systemui.statusbar;

import android.os.VibrationEffect;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class VibratorHelper$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ VibratorHelper f$0;
    public final /* synthetic */ VibrationEffect f$1;

    public /* synthetic */ VibratorHelper$$ExternalSyntheticLambda1(VibratorHelper vibratorHelper, VibrationEffect vibrationEffect) {
        this.f$0 = vibratorHelper;
        this.f$1 = vibrationEffect;
    }

    public final void run() {
        this.f$0.lambda$vibrate$3(this.f$1);
    }
}
