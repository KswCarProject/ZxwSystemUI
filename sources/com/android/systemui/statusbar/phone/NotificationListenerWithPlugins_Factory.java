package com.android.systemui.statusbar.phone;

import com.android.systemui.shared.plugins.PluginManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationListenerWithPlugins_Factory implements Factory<NotificationListenerWithPlugins> {
    public final Provider<PluginManager> pluginManagerProvider;

    public NotificationListenerWithPlugins_Factory(Provider<PluginManager> provider) {
        this.pluginManagerProvider = provider;
    }

    public NotificationListenerWithPlugins get() {
        return newInstance(this.pluginManagerProvider.get());
    }

    public static NotificationListenerWithPlugins_Factory create(Provider<PluginManager> provider) {
        return new NotificationListenerWithPlugins_Factory(provider);
    }

    public static NotificationListenerWithPlugins newInstance(PluginManager pluginManager) {
        return new NotificationListenerWithPlugins(pluginManager);
    }
}
