package com.android.systemui.privacy;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;

public final class PrivacyItemController_Factory implements Factory<PrivacyItemController> {
    public final Provider<DelayableExecutor> bgExecutorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<PrivacyLogger> loggerProvider;
    public final Provider<PrivacyConfig> privacyConfigProvider;
    public final Provider<Set<PrivacyItemMonitor>> privacyItemMonitorsProvider;
    public final Provider<SystemClock> systemClockProvider;
    public final Provider<DelayableExecutor> uiExecutorProvider;

    public PrivacyItemController_Factory(Provider<DelayableExecutor> provider, Provider<DelayableExecutor> provider2, Provider<PrivacyConfig> provider3, Provider<Set<PrivacyItemMonitor>> provider4, Provider<PrivacyLogger> provider5, Provider<SystemClock> provider6, Provider<DumpManager> provider7) {
        this.uiExecutorProvider = provider;
        this.bgExecutorProvider = provider2;
        this.privacyConfigProvider = provider3;
        this.privacyItemMonitorsProvider = provider4;
        this.loggerProvider = provider5;
        this.systemClockProvider = provider6;
        this.dumpManagerProvider = provider7;
    }

    public PrivacyItemController get() {
        return newInstance(this.uiExecutorProvider.get(), this.bgExecutorProvider.get(), this.privacyConfigProvider.get(), this.privacyItemMonitorsProvider.get(), this.loggerProvider.get(), this.systemClockProvider.get(), this.dumpManagerProvider.get());
    }

    public static PrivacyItemController_Factory create(Provider<DelayableExecutor> provider, Provider<DelayableExecutor> provider2, Provider<PrivacyConfig> provider3, Provider<Set<PrivacyItemMonitor>> provider4, Provider<PrivacyLogger> provider5, Provider<SystemClock> provider6, Provider<DumpManager> provider7) {
        return new PrivacyItemController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static PrivacyItemController newInstance(DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, PrivacyConfig privacyConfig, Set<PrivacyItemMonitor> set, PrivacyLogger privacyLogger, SystemClock systemClock, DumpManager dumpManager) {
        return new PrivacyItemController(delayableExecutor, delayableExecutor2, privacyConfig, set, privacyLogger, systemClock, dumpManager);
    }
}
