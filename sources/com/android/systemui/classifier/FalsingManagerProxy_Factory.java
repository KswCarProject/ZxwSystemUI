package com.android.systemui.classifier;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class FalsingManagerProxy_Factory implements Factory<FalsingManagerProxy> {
    public final Provider<BrightLineFalsingManager> brightLineFalsingManagerProvider;
    public final Provider<DeviceConfigProxy> deviceConfigProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<PluginManager> pluginManagerProvider;

    public FalsingManagerProxy_Factory(Provider<PluginManager> provider, Provider<Executor> provider2, Provider<DeviceConfigProxy> provider3, Provider<DumpManager> provider4, Provider<BrightLineFalsingManager> provider5) {
        this.pluginManagerProvider = provider;
        this.executorProvider = provider2;
        this.deviceConfigProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.brightLineFalsingManagerProvider = provider5;
    }

    public FalsingManagerProxy get() {
        return newInstance(this.pluginManagerProvider.get(), this.executorProvider.get(), this.deviceConfigProvider.get(), this.dumpManagerProvider.get(), this.brightLineFalsingManagerProvider);
    }

    public static FalsingManagerProxy_Factory create(Provider<PluginManager> provider, Provider<Executor> provider2, Provider<DeviceConfigProxy> provider3, Provider<DumpManager> provider4, Provider<BrightLineFalsingManager> provider5) {
        return new FalsingManagerProxy_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static FalsingManagerProxy newInstance(PluginManager pluginManager, Executor executor, DeviceConfigProxy deviceConfigProxy, DumpManager dumpManager, Provider<BrightLineFalsingManager> provider) {
        return new FalsingManagerProxy(pluginManager, executor, deviceConfigProxy, dumpManager, provider);
    }
}
