package com.android.systemui.statusbar.notification.stack;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationStackScrollLayoutListContainerModule_ProvideListContainerFactory implements Factory<NotificationListContainer> {
    public final Provider<NotificationStackScrollLayoutController> nsslControllerProvider;

    public NotificationStackScrollLayoutListContainerModule_ProvideListContainerFactory(Provider<NotificationStackScrollLayoutController> provider) {
        this.nsslControllerProvider = provider;
    }

    public NotificationListContainer get() {
        return provideListContainer(this.nsslControllerProvider.get());
    }

    public static NotificationStackScrollLayoutListContainerModule_ProvideListContainerFactory create(Provider<NotificationStackScrollLayoutController> provider) {
        return new NotificationStackScrollLayoutListContainerModule_ProvideListContainerFactory(provider);
    }

    public static NotificationListContainer provideListContainer(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return (NotificationListContainer) Preconditions.checkNotNullFromProvides(NotificationStackScrollLayoutListContainerModule.provideListContainer(notificationStackScrollLayoutController));
    }
}
