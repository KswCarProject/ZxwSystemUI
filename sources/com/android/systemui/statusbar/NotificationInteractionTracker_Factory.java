package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.NotificationEntryManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationInteractionTracker_Factory implements Factory<NotificationInteractionTracker> {
    public final Provider<NotificationClickNotifier> clickerProvider;
    public final Provider<NotificationEntryManager> entryManagerProvider;

    public NotificationInteractionTracker_Factory(Provider<NotificationClickNotifier> provider, Provider<NotificationEntryManager> provider2) {
        this.clickerProvider = provider;
        this.entryManagerProvider = provider2;
    }

    public NotificationInteractionTracker get() {
        return newInstance(this.clickerProvider.get(), this.entryManagerProvider.get());
    }

    public static NotificationInteractionTracker_Factory create(Provider<NotificationClickNotifier> provider, Provider<NotificationEntryManager> provider2) {
        return new NotificationInteractionTracker_Factory(provider, provider2);
    }

    public static NotificationInteractionTracker newInstance(NotificationClickNotifier notificationClickNotifier, NotificationEntryManager notificationEntryManager) {
        return new NotificationInteractionTracker(notificationClickNotifier, notificationEntryManager);
    }
}
