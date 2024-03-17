package com.android.systemui.doze;

import com.android.keyguard.KeyguardUpdateMonitor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DozeAuthRemover_Factory implements Factory<DozeAuthRemover> {
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;

    public DozeAuthRemover_Factory(Provider<KeyguardUpdateMonitor> provider) {
        this.keyguardUpdateMonitorProvider = provider;
    }

    public DozeAuthRemover get() {
        return newInstance(this.keyguardUpdateMonitorProvider.get());
    }

    public static DozeAuthRemover_Factory create(Provider<KeyguardUpdateMonitor> provider) {
        return new DozeAuthRemover_Factory(provider);
    }

    public static DozeAuthRemover newInstance(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new DozeAuthRemover(keyguardUpdateMonitor);
    }
}
