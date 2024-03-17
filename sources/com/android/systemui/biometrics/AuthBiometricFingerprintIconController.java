package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthBiometricFingerprintIconController.kt */
public class AuthBiometricFingerprintIconController extends AuthIconController {
    public boolean shouldAnimateForTransition(int i, int i2) {
        if (i2 == 1 || i2 == 2) {
            if (!(i == 4 || i == 3)) {
                return false;
            }
        } else if (!(i2 == 3 || i2 == 4)) {
            return false;
        }
        return true;
    }

    public AuthBiometricFingerprintIconController(@NotNull Context context, @NotNull ImageView imageView) {
        super(context, imageView);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.biometric_dialog_fingerprint_icon_size);
        imageView.getLayoutParams().width = dimensionPixelSize;
        imageView.getLayoutParams().height = dimensionPixelSize;
    }

    public void updateIcon(int i, int i2) {
        Drawable animationForTransition = getAnimationForTransition(i, i2);
        if (animationForTransition != null) {
            getIconView().setImageDrawable(animationForTransition);
            CharSequence iconContentDescription = getIconContentDescription(i2);
            if (iconContentDescription != null) {
                getIconView().setContentDescription(iconContentDescription);
            }
            AnimatedVectorDrawable animatedVectorDrawable = animationForTransition instanceof AnimatedVectorDrawable ? (AnimatedVectorDrawable) animationForTransition : null;
            if (animatedVectorDrawable != null) {
                animatedVectorDrawable.reset();
                if (shouldAnimateForTransition(i, i2)) {
                    animatedVectorDrawable.forceAnimationOnUI();
                    animatedVectorDrawable.start();
                }
            }
        }
    }

    public final CharSequence getIconContentDescription(int i) {
        Integer num;
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 5:
            case 6:
                num = Integer.valueOf(R$string.accessibility_fingerprint_dialog_fingerprint_icon);
                break;
            case 3:
            case 4:
                num = Integer.valueOf(R$string.biometric_dialog_try_again);
                break;
            default:
                num = null;
                break;
        }
        if (num != null) {
            return getContext().getString(num.intValue());
        }
        return null;
    }

    @Nullable
    public Drawable getAnimationForTransition(int i, int i2) {
        int i3;
        if (i2 == 1 || i2 == 2) {
            if (i == 3 || i == 4) {
                i3 = R$drawable.fingerprint_dialog_error_to_fp;
            } else {
                i3 = R$drawable.fingerprint_dialog_fp_to_error;
            }
        } else if (i2 == 3 || i2 == 4) {
            i3 = R$drawable.fingerprint_dialog_fp_to_error;
        } else if (i2 != 6) {
            return null;
        } else {
            i3 = R$drawable.fingerprint_dialog_fp_to_error;
        }
        return getContext().getDrawable(i3);
    }
}
