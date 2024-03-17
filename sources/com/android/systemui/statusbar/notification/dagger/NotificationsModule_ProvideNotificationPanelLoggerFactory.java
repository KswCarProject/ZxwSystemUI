package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class NotificationsModule_ProvideNotificationPanelLoggerFactory implements Factory<NotificationPanelLogger> {

    public static final class InstanceHolder {
        public static final NotificationsModule_ProvideNotificationPanelLoggerFactory INSTANCE = new NotificationsModule_ProvideNotificationPanelLoggerFactory();
    }

    public NotificationPanelLogger get() {
        return provideNotificationPanelLogger();
    }

    public static NotificationsModule_ProvideNotificationPanelLoggerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotificationPanelLogger provideNotificationPanelLogger() {
        return (NotificationPanelLogger) Preconditions.checkNotNullFromProvides(NotificationsModule.provideNotificationPanelLogger());
    }
}
