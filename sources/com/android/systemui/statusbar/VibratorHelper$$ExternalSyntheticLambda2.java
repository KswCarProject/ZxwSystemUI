package com.android.systemui.statusbar;

import android.os.VibrationAttributes;
import android.os.VibrationEffect;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class VibratorHelper$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ VibratorHelper f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ VibrationEffect f$3;
    public final /* synthetic */ String f$4;
    public final /* synthetic */ VibrationAttributes f$5;

    public /* synthetic */ VibratorHelper$$ExternalSyntheticLambda2(VibratorHelper vibratorHelper, int i, String str, VibrationEffect vibrationEffect, String str2, VibrationAttributes vibrationAttributes) {
        this.f$0 = vibratorHelper;
        this.f$1 = i;
        this.f$2 = str;
        this.f$3 = vibrationEffect;
        this.f$4 = str2;
        this.f$5 = vibrationAttributes;
    }

    public final void run() {
        this.f$0.lambda$vibrate$1(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
