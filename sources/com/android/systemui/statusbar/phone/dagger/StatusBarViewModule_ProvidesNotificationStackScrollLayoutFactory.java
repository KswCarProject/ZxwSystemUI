package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_ProvidesNotificationStackScrollLayoutFactory implements Factory<NotificationStackScrollLayout> {
    public final Provider<NotificationShadeWindowView> notificationShadeWindowViewProvider;

    public StatusBarViewModule_ProvidesNotificationStackScrollLayoutFactory(Provider<NotificationShadeWindowView> provider) {
        this.notificationShadeWindowViewProvider = provider;
    }

    public NotificationStackScrollLayout get() {
        return providesNotificationStackScrollLayout(this.notificationShadeWindowViewProvider.get());
    }

    public static StatusBarViewModule_ProvidesNotificationStackScrollLayoutFactory create(Provider<NotificationShadeWindowView> provider) {
        return new StatusBarViewModule_ProvidesNotificationStackScrollLayoutFactory(provider);
    }

    public static NotificationStackScrollLayout providesNotificationStackScrollLayout(NotificationShadeWindowView notificationShadeWindowView) {
        return (NotificationStackScrollLayout) Preconditions.checkNotNullFromProvides(StatusBarViewModule.providesNotificationStackScrollLayout(notificationShadeWindowView));
    }
}
