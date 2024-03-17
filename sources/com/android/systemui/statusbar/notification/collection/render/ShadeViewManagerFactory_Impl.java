package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class ShadeViewManagerFactory_Impl implements ShadeViewManagerFactory {
    public final ShadeViewManager_Factory delegateFactory;

    public ShadeViewManagerFactory_Impl(ShadeViewManager_Factory shadeViewManager_Factory) {
        this.delegateFactory = shadeViewManager_Factory;
    }

    public ShadeViewManager create(NotificationListContainer notificationListContainer, NotifStackController notifStackController) {
        return this.delegateFactory.get(notificationListContainer, notifStackController);
    }

    public static Provider<ShadeViewManagerFactory> create(ShadeViewManager_Factory shadeViewManager_Factory) {
        return InstanceFactory.create(new ShadeViewManagerFactory_Impl(shadeViewManager_Factory));
    }
}
