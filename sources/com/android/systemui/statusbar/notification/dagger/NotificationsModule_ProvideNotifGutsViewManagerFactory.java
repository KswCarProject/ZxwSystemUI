package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationsModule_ProvideNotifGutsViewManagerFactory implements Factory<NotifGutsViewManager> {
    public final Provider<NotificationGutsManager> notificationGutsManagerProvider;

    public NotificationsModule_ProvideNotifGutsViewManagerFactory(Provider<NotificationGutsManager> provider) {
        this.notificationGutsManagerProvider = provider;
    }

    public NotifGutsViewManager get() {
        return provideNotifGutsViewManager(this.notificationGutsManagerProvider.get());
    }

    public static NotificationsModule_ProvideNotifGutsViewManagerFactory create(Provider<NotificationGutsManager> provider) {
        return new NotificationsModule_ProvideNotifGutsViewManagerFactory(provider);
    }

    public static NotifGutsViewManager provideNotifGutsViewManager(NotificationGutsManager notificationGutsManager) {
        return (NotifGutsViewManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideNotifGutsViewManager(notificationGutsManager));
    }
}
