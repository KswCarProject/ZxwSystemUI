package com.android.systemui.plugins;

import com.android.systemui.shared.plugins.PluginManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PluginDependencyProvider_Factory implements Factory<PluginDependencyProvider> {
    private final Provider<PluginManager> managerLazyProvider;

    public PluginDependencyProvider_Factory(Provider<PluginManager> provider) {
        this.managerLazyProvider = provider;
    }

    public PluginDependencyProvider get() {
        return newInstance(DoubleCheck.lazy(this.managerLazyProvider));
    }

    public static PluginDependencyProvider_Factory create(Provider<PluginManager> provider) {
        return new PluginDependencyProvider_Factory(provider);
    }

    public static PluginDependencyProvider newInstance(Lazy<PluginManager> lazy) {
        return new PluginDependencyProvider(lazy);
    }
}
