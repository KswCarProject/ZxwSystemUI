package com.android.systemui.statusbar.notification;

import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationLaunchAnimatorControllerProvider_Factory implements Factory<NotificationLaunchAnimatorControllerProvider> {
    public final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    public final Provider<InteractionJankMonitor> jankMonitorProvider;
    public final Provider<NotificationListContainer> notificationListContainerProvider;
    public final Provider<NotificationShadeWindowViewController> notificationShadeWindowViewControllerProvider;

    public NotificationLaunchAnimatorControllerProvider_Factory(Provider<NotificationShadeWindowViewController> provider, Provider<NotificationListContainer> provider2, Provider<HeadsUpManagerPhone> provider3, Provider<InteractionJankMonitor> provider4) {
        this.notificationShadeWindowViewControllerProvider = provider;
        this.notificationListContainerProvider = provider2;
        this.headsUpManagerProvider = provider3;
        this.jankMonitorProvider = provider4;
    }

    public NotificationLaunchAnimatorControllerProvider get() {
        return newInstance(this.notificationShadeWindowViewControllerProvider.get(), this.notificationListContainerProvider.get(), this.headsUpManagerProvider.get(), this.jankMonitorProvider.get());
    }

    public static NotificationLaunchAnimatorControllerProvider_Factory create(Provider<NotificationShadeWindowViewController> provider, Provider<NotificationListContainer> provider2, Provider<HeadsUpManagerPhone> provider3, Provider<InteractionJankMonitor> provider4) {
        return new NotificationLaunchAnimatorControllerProvider_Factory(provider, provider2, provider3, provider4);
    }

    public static NotificationLaunchAnimatorControllerProvider newInstance(NotificationShadeWindowViewController notificationShadeWindowViewController, NotificationListContainer notificationListContainer, HeadsUpManagerPhone headsUpManagerPhone, InteractionJankMonitor interactionJankMonitor) {
        return new NotificationLaunchAnimatorControllerProvider(notificationShadeWindowViewController, notificationListContainer, headsUpManagerPhone, interactionJankMonitor);
    }
}
