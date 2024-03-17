package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.BiometricUnlockController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BiometricUnlockController$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ BiometricUnlockController f$0;

    public /* synthetic */ BiometricUnlockController$$ExternalSyntheticLambda3(BiometricUnlockController biometricUnlockController) {
        this.f$0 = biometricUnlockController;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onBiometricAuthFailed$2((BiometricUnlockController.BiometricUiEvent) obj);
    }
}
