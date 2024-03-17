package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.statusbar.notification.NotificationEntryManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LegacyNotificationPresenterExtensions_Factory implements Factory<LegacyNotificationPresenterExtensions> {
    public final Provider<NotificationEntryManager> entryManagerProvider;

    public LegacyNotificationPresenterExtensions_Factory(Provider<NotificationEntryManager> provider) {
        this.entryManagerProvider = provider;
    }

    public LegacyNotificationPresenterExtensions get() {
        return newInstance(this.entryManagerProvider.get());
    }

    public static LegacyNotificationPresenterExtensions_Factory create(Provider<NotificationEntryManager> provider) {
        return new LegacyNotificationPresenterExtensions_Factory(provider);
    }

    public static LegacyNotificationPresenterExtensions newInstance(NotificationEntryManager notificationEntryManager) {
        return new LegacyNotificationPresenterExtensions(notificationEntryManager);
    }
}
