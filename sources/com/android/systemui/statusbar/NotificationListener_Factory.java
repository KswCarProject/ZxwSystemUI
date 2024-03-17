package com.android.systemui.statusbar;

import android.app.NotificationManager;
import android.content.Context;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class NotificationListener_Factory implements Factory<NotificationListener> {
    public final Provider<Context> contextProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<NotificationManager> notificationManagerProvider;
    public final Provider<PluginManager> pluginManagerProvider;
    public final Provider<SystemClock> systemClockProvider;

    public NotificationListener_Factory(Provider<Context> provider, Provider<NotificationManager> provider2, Provider<SystemClock> provider3, Provider<Executor> provider4, Provider<PluginManager> provider5) {
        this.contextProvider = provider;
        this.notificationManagerProvider = provider2;
        this.systemClockProvider = provider3;
        this.mainExecutorProvider = provider4;
        this.pluginManagerProvider = provider5;
    }

    public NotificationListener get() {
        return newInstance(this.contextProvider.get(), this.notificationManagerProvider.get(), this.systemClockProvider.get(), this.mainExecutorProvider.get(), this.pluginManagerProvider.get());
    }

    public static NotificationListener_Factory create(Provider<Context> provider, Provider<NotificationManager> provider2, Provider<SystemClock> provider3, Provider<Executor> provider4, Provider<PluginManager> provider5) {
        return new NotificationListener_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static NotificationListener newInstance(Context context, NotificationManager notificationManager, SystemClock systemClock, Executor executor, PluginManager pluginManager) {
        return new NotificationListener(context, notificationManager, systemClock, executor, pluginManager);
    }
}
