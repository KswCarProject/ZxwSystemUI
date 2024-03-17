package com.android.systemui.plugins;

import android.content.Context;
import com.android.systemui.shared.plugins.PluginPrefs;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PluginsModule_ProvidesPluginPrefsFactory implements Factory<PluginPrefs> {
    private final Provider<Context> contextProvider;

    public PluginsModule_ProvidesPluginPrefsFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public PluginPrefs get() {
        return providesPluginPrefs(this.contextProvider.get());
    }

    public static PluginsModule_ProvidesPluginPrefsFactory create(Provider<Context> provider) {
        return new PluginsModule_ProvidesPluginPrefsFactory(provider);
    }

    public static PluginPrefs providesPluginPrefs(Context context) {
        return (PluginPrefs) Preconditions.checkNotNullFromProvides(PluginsModule.providesPluginPrefs(context));
    }
}
