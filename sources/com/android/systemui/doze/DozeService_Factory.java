package com.android.systemui.doze;

import com.android.systemui.doze.dagger.DozeComponent;
import com.android.systemui.shared.plugins.PluginManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DozeService_Factory implements Factory<DozeService> {
    public final Provider<DozeComponent.Builder> dozeComponentBuilderProvider;
    public final Provider<PluginManager> pluginManagerProvider;

    public DozeService_Factory(Provider<DozeComponent.Builder> provider, Provider<PluginManager> provider2) {
        this.dozeComponentBuilderProvider = provider;
        this.pluginManagerProvider = provider2;
    }

    public DozeService get() {
        return newInstance(this.dozeComponentBuilderProvider.get(), this.pluginManagerProvider.get());
    }

    public static DozeService_Factory create(Provider<DozeComponent.Builder> provider, Provider<PluginManager> provider2) {
        return new DozeService_Factory(provider, provider2);
    }

    public static DozeService newInstance(DozeComponent.Builder builder, PluginManager pluginManager) {
        return new DozeService(builder, pluginManager);
    }
}
