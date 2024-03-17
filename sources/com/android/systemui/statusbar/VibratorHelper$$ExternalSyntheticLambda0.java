package com.android.systemui.statusbar;

import android.media.AudioAttributes;
import android.os.VibrationEffect;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class VibratorHelper$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ VibratorHelper f$0;
    public final /* synthetic */ VibrationEffect f$1;
    public final /* synthetic */ AudioAttributes f$2;

    public /* synthetic */ VibratorHelper$$ExternalSyntheticLambda0(VibratorHelper vibratorHelper, VibrationEffect vibrationEffect, AudioAttributes audioAttributes) {
        this.f$0 = vibratorHelper;
        this.f$1 = vibrationEffect;
        this.f$2 = audioAttributes;
    }

    public final void run() {
        this.f$0.lambda$vibrate$2(this.f$1, this.f$2);
    }
}
