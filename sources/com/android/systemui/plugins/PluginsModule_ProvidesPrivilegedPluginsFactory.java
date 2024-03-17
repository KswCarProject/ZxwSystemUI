package com.android.systemui.plugins;

import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.List;
import javax.inject.Provider;

public final class PluginsModule_ProvidesPrivilegedPluginsFactory implements Factory<List<String>> {
    private final Provider<Context> contextProvider;

    public PluginsModule_ProvidesPrivilegedPluginsFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public List<String> get() {
        return providesPrivilegedPlugins(this.contextProvider.get());
    }

    public static PluginsModule_ProvidesPrivilegedPluginsFactory create(Provider<Context> provider) {
        return new PluginsModule_ProvidesPrivilegedPluginsFactory(provider);
    }

    public static List<String> providesPrivilegedPlugins(Context context) {
        return (List) Preconditions.checkNotNullFromProvides(PluginsModule.providesPrivilegedPlugins(context));
    }
}
