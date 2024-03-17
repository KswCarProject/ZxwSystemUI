package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import com.android.systemui.R$string;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthBiometricFingerprintAndFaceView.kt */
public final class AuthBiometricFingerprintAndFaceView extends AuthBiometricFingerprintView {
    public boolean forceRequireConfirmation(int i) {
        return i == 8;
    }

    public boolean ignoreUnsuccessfulEventsFrom(int i) {
        return i == 8;
    }

    public AuthBiometricFingerprintAndFaceView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AuthBiometricFingerprintAndFaceView(@NotNull Context context) {
        this(context, (AttributeSet) null);
    }

    public int getConfirmationPrompt() {
        return R$string.biometric_dialog_tap_confirm_with_face;
    }

    public boolean onPointerDown(@NotNull Set<Integer> set) {
        return set.contains(8);
    }

    @NotNull
    public AuthIconController createIconController() {
        return new AuthBiometricFingerprintAndFaceIconController(this.mContext, this.mIconView);
    }
}
