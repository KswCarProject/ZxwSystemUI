package com.android.systemui.plugins;

import com.android.systemui.shared.plugins.PluginInstance;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.List;
import javax.inject.Provider;

public final class PluginsModule_ProvidesPluginInstanceFactoryFactory implements Factory<PluginInstance.Factory> {
    private final Provider<Boolean> isDebugProvider;
    private final Provider<List<String>> privilegedPluginsProvider;

    public PluginsModule_ProvidesPluginInstanceFactoryFactory(Provider<List<String>> provider, Provider<Boolean> provider2) {
        this.privilegedPluginsProvider = provider;
        this.isDebugProvider = provider2;
    }

    public PluginInstance.Factory get() {
        return providesPluginInstanceFactory(this.privilegedPluginsProvider.get(), this.isDebugProvider.get().booleanValue());
    }

    public static PluginsModule_ProvidesPluginInstanceFactoryFactory create(Provider<List<String>> provider, Provider<Boolean> provider2) {
        return new PluginsModule_ProvidesPluginInstanceFactoryFactory(provider, provider2);
    }

    public static PluginInstance.Factory providesPluginInstanceFactory(List<String> list, boolean z) {
        return (PluginInstance.Factory) Preconditions.checkNotNullFromProvides(PluginsModule.providesPluginInstanceFactory(list, z));
    }
}
