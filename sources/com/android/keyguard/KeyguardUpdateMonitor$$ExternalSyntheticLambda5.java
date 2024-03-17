package com.android.keyguard;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardUpdateMonitor$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ KeyguardUpdateMonitor f$0;

    public /* synthetic */ KeyguardUpdateMonitor$$ExternalSyntheticLambda5(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.f$0 = keyguardUpdateMonitor;
    }

    public final void accept(Object obj) {
        this.f$0.notifyStrongAuthStateChanged(((Integer) obj).intValue());
    }
}
