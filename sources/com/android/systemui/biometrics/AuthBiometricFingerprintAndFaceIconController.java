package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.android.systemui.R$drawable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthBiometricFingerprintAndFaceIconController.kt */
public final class AuthBiometricFingerprintAndFaceIconController extends AuthBiometricFingerprintIconController {
    public final boolean actsAsConfirmButton = true;

    public AuthBiometricFingerprintAndFaceIconController(@NotNull Context context, @NotNull ImageView imageView) {
        super(context, imageView);
    }

    public boolean getActsAsConfirmButton() {
        return this.actsAsConfirmButton;
    }

    public boolean shouldAnimateForTransition(int i, int i2) {
        if (i2 == 5) {
            return true;
        }
        if (i2 != 6) {
            return super.shouldAnimateForTransition(i, i2);
        }
        return false;
    }

    @Nullable
    public Drawable getAnimationForTransition(int i, int i2) {
        if (i2 != 5) {
            if (i2 != 6) {
                return super.getAnimationForTransition(i, i2);
            }
            return null;
        } else if (i == 3 || i == 4) {
            return getContext().getDrawable(R$drawable.fingerprint_dialog_error_to_unlock);
        } else {
            return getContext().getDrawable(R$drawable.fingerprint_dialog_fp_to_unlock);
        }
    }
}
