package com.android.systemui.doze;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DozeLog_Factory implements Factory<DozeLog> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<DozeLogger> loggerProvider;

    public DozeLog_Factory(Provider<KeyguardUpdateMonitor> provider, Provider<DumpManager> provider2, Provider<DozeLogger> provider3) {
        this.keyguardUpdateMonitorProvider = provider;
        this.dumpManagerProvider = provider2;
        this.loggerProvider = provider3;
    }

    public DozeLog get() {
        return newInstance(this.keyguardUpdateMonitorProvider.get(), this.dumpManagerProvider.get(), this.loggerProvider.get());
    }

    public static DozeLog_Factory create(Provider<KeyguardUpdateMonitor> provider, Provider<DumpManager> provider2, Provider<DozeLogger> provider3) {
        return new DozeLog_Factory(provider, provider2, provider3);
    }

    public static DozeLog newInstance(KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, DozeLogger dozeLogger) {
        return new DozeLog(keyguardUpdateMonitor, dumpManager, dozeLogger);
    }
}
