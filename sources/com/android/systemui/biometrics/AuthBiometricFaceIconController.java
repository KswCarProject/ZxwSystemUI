package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: AuthBiometricFaceIconController.kt */
public final class AuthBiometricFaceIconController extends AuthIconController {
    public boolean lastPulseLightToDark;
    public int state;

    public AuthBiometricFaceIconController(@NotNull Context context, @NotNull ImageView imageView) {
        super(context, imageView);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.biometric_dialog_face_icon_size);
        imageView.getLayoutParams().width = dimensionPixelSize;
        imageView.getLayoutParams().height = dimensionPixelSize;
        showStaticDrawable(R$drawable.face_dialog_pulse_dark_to_light);
    }

    public final void startPulsing() {
        this.lastPulseLightToDark = false;
        animateIcon(R$drawable.face_dialog_pulse_dark_to_light, true);
    }

    public final void pulseInNextDirection() {
        int i;
        if (this.lastPulseLightToDark) {
            i = R$drawable.face_dialog_pulse_dark_to_light;
        } else {
            i = R$drawable.face_dialog_pulse_light_to_dark;
        }
        animateIcon(i, true);
        this.lastPulseLightToDark = !this.lastPulseLightToDark;
    }

    public void handleAnimationEnd(@NotNull Drawable drawable) {
        int i = this.state;
        if (i == 2 || i == 3) {
            pulseInNextDirection();
        }
    }

    public void updateIcon(int i, int i2) {
        boolean z = i == 4 || i == 3;
        if (i2 == 1) {
            showStaticDrawable(R$drawable.face_dialog_pulse_dark_to_light);
            getIconView().setContentDescription(getContext().getString(R$string.biometric_dialog_face_icon_description_authenticating));
        } else if (i2 == 2) {
            startPulsing();
            getIconView().setContentDescription(getContext().getString(R$string.biometric_dialog_face_icon_description_authenticating));
        } else if (i == 5 && i2 == 6) {
            animateIconOnce(R$drawable.face_dialog_dark_to_checkmark);
            getIconView().setContentDescription(getContext().getString(R$string.biometric_dialog_face_icon_description_confirmed));
        } else if (z && i2 == 0) {
            animateIconOnce(R$drawable.face_dialog_error_to_idle);
            getIconView().setContentDescription(getContext().getString(R$string.biometric_dialog_face_icon_description_idle));
        } else if (z && i2 == 6) {
            animateIconOnce(R$drawable.face_dialog_dark_to_checkmark);
            getIconView().setContentDescription(getContext().getString(R$string.biometric_dialog_face_icon_description_authenticated));
        } else if (i2 == 4 && i != 4) {
            animateIconOnce(R$drawable.face_dialog_dark_to_error);
        } else if (i == 2 && i2 == 6) {
            animateIconOnce(R$drawable.face_dialog_dark_to_checkmark);
            getIconView().setContentDescription(getContext().getString(R$string.biometric_dialog_face_icon_description_authenticated));
        } else if (i2 == 5) {
            animateIconOnce(R$drawable.face_dialog_wink_from_dark);
            getIconView().setContentDescription(getContext().getString(R$string.biometric_dialog_face_icon_description_authenticated));
        } else if (i2 == 0) {
            showStaticDrawable(R$drawable.face_dialog_idle_static);
            getIconView().setContentDescription(getContext().getString(R$string.biometric_dialog_face_icon_description_idle));
        } else {
            Log.w("AuthBiometricFaceIconController", Intrinsics.stringPlus("Unhandled state: ", Integer.valueOf(i2)));
        }
        this.state = i2;
    }
}
