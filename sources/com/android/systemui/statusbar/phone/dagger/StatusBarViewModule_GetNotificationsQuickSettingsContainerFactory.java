package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_GetNotificationsQuickSettingsContainerFactory implements Factory<NotificationsQuickSettingsContainer> {
    public final Provider<NotificationShadeWindowView> notificationShadeWindowViewProvider;

    public StatusBarViewModule_GetNotificationsQuickSettingsContainerFactory(Provider<NotificationShadeWindowView> provider) {
        this.notificationShadeWindowViewProvider = provider;
    }

    public NotificationsQuickSettingsContainer get() {
        return getNotificationsQuickSettingsContainer(this.notificationShadeWindowViewProvider.get());
    }

    public static StatusBarViewModule_GetNotificationsQuickSettingsContainerFactory create(Provider<NotificationShadeWindowView> provider) {
        return new StatusBarViewModule_GetNotificationsQuickSettingsContainerFactory(provider);
    }

    public static NotificationsQuickSettingsContainer getNotificationsQuickSettingsContainer(NotificationShadeWindowView notificationShadeWindowView) {
        return (NotificationsQuickSettingsContainer) Preconditions.checkNotNullFromProvides(StatusBarViewModule.getNotificationsQuickSettingsContainer(notificationShadeWindowView));
    }
}
