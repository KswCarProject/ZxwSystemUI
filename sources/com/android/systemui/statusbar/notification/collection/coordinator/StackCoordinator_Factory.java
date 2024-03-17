package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StackCoordinator_Factory implements Factory<StackCoordinator> {
    public final Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;

    public StackCoordinator_Factory(Provider<NotificationIconAreaController> provider) {
        this.notificationIconAreaControllerProvider = provider;
    }

    public StackCoordinator get() {
        return newInstance(this.notificationIconAreaControllerProvider.get());
    }

    public static StackCoordinator_Factory create(Provider<NotificationIconAreaController> provider) {
        return new StackCoordinator_Factory(provider);
    }

    public static StackCoordinator newInstance(NotificationIconAreaController notificationIconAreaController) {
        return new StackCoordinator(notificationIconAreaController);
    }
}
