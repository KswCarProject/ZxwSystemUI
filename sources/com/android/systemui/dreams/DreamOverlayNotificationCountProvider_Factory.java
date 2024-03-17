package com.android.systemui.dreams;

import com.android.systemui.statusbar.NotificationListener;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DreamOverlayNotificationCountProvider_Factory implements Factory<DreamOverlayNotificationCountProvider> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<NotificationListener> notificationListenerProvider;

    public DreamOverlayNotificationCountProvider_Factory(Provider<NotificationListener> provider, Provider<Executor> provider2) {
        this.notificationListenerProvider = provider;
        this.bgExecutorProvider = provider2;
    }

    public DreamOverlayNotificationCountProvider get() {
        return newInstance(this.notificationListenerProvider.get(), this.bgExecutorProvider.get());
    }

    public static DreamOverlayNotificationCountProvider_Factory create(Provider<NotificationListener> provider, Provider<Executor> provider2) {
        return new DreamOverlayNotificationCountProvider_Factory(provider, provider2);
    }

    public static DreamOverlayNotificationCountProvider newInstance(NotificationListener notificationListener, Executor executor) {
        return new DreamOverlayNotificationCountProvider(notificationListener, executor);
    }
}
