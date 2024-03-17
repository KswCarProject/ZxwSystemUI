package com.android.keyguard;

import android.app.admin.DevicePolicyManager;
import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardUpdateMonitor$$ExternalSyntheticLambda10 implements Supplier {
    public final /* synthetic */ KeyguardUpdateMonitor f$0;
    public final /* synthetic */ DevicePolicyManager f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ KeyguardUpdateMonitor$$ExternalSyntheticLambda10(KeyguardUpdateMonitor keyguardUpdateMonitor, DevicePolicyManager devicePolicyManager, int i) {
        this.f$0 = keyguardUpdateMonitor;
        this.f$1 = devicePolicyManager;
        this.f$2 = i;
    }

    public final Object get() {
        return this.f$0.lambda$isFaceDisabled$4(this.f$1, this.f$2);
    }
}
