package com.android.keyguard;

import java.lang.ref.WeakReference;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardUpdateMonitor$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ KeyguardUpdateMonitorCallback f$0;

    public /* synthetic */ KeyguardUpdateMonitor$$ExternalSyntheticLambda0(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        this.f$0 = keyguardUpdateMonitorCallback;
    }

    public final boolean test(Object obj) {
        return KeyguardUpdateMonitor.lambda$removeCallback$9(this.f$0, (WeakReference) obj);
    }
}
