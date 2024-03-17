package com.android.systemui.privacy;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PrivacyConfig_Factory implements Factory<PrivacyConfig> {
    public final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> uiExecutorProvider;

    public PrivacyConfig_Factory(Provider<DelayableExecutor> provider, Provider<DeviceConfigProxy> provider2, Provider<DumpManager> provider3) {
        this.uiExecutorProvider = provider;
        this.deviceConfigProxyProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    public PrivacyConfig get() {
        return newInstance(this.uiExecutorProvider.get(), this.deviceConfigProxyProvider.get(), this.dumpManagerProvider.get());
    }

    public static PrivacyConfig_Factory create(Provider<DelayableExecutor> provider, Provider<DeviceConfigProxy> provider2, Provider<DumpManager> provider3) {
        return new PrivacyConfig_Factory(provider, provider2, provider3);
    }

    public static PrivacyConfig newInstance(DelayableExecutor delayableExecutor, DeviceConfigProxy deviceConfigProxy, DumpManager dumpManager) {
        return new PrivacyConfig(delayableExecutor, deviceConfigProxy, dumpManager);
    }
}
