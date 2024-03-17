package com.android.systemui.statusbar;

import android.media.AudioAttributes;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

public class VibratorHelper {
    public static final VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(18);
    public final Executor mExecutor;
    public final Vibrator mVibrator;

    public VibratorHelper(Vibrator vibrator, Executor executor) {
        this.mExecutor = executor;
        this.mVibrator = vibrator;
    }

    public void vibrate(int i) {
        if (hasVibrator()) {
            this.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda3(this, i));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$vibrate$0(int i) {
        this.mVibrator.vibrate(VibrationEffect.get(i, false), TOUCH_VIBRATION_ATTRIBUTES);
    }

    public void vibrate(int i, String str, VibrationEffect vibrationEffect, String str2, VibrationAttributes vibrationAttributes) {
        if (hasVibrator()) {
            this.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda2(this, i, str, vibrationEffect, str2, vibrationAttributes));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$vibrate$1(int i, String str, VibrationEffect vibrationEffect, String str2, VibrationAttributes vibrationAttributes) {
        this.mVibrator.vibrate(i, str, vibrationEffect, str2, vibrationAttributes);
    }

    public void vibrate(VibrationEffect vibrationEffect, AudioAttributes audioAttributes) {
        if (hasVibrator()) {
            this.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda0(this, vibrationEffect, audioAttributes));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$vibrate$2(VibrationEffect vibrationEffect, AudioAttributes audioAttributes) {
        this.mVibrator.vibrate(vibrationEffect, audioAttributes);
    }

    public void vibrate(@NotNull VibrationEffect vibrationEffect) {
        if (hasVibrator()) {
            this.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda1(this, vibrationEffect));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$vibrate$3(VibrationEffect vibrationEffect) {
        this.mVibrator.vibrate(vibrationEffect);
    }

    public boolean hasVibrator() {
        Vibrator vibrator = this.mVibrator;
        return vibrator != null && vibrator.hasVibrator();
    }
}
