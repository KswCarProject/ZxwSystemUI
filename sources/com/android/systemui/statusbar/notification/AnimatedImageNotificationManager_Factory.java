package com.android.systemui.statusbar.notification;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AnimatedImageNotificationManager_Factory implements Factory<AnimatedImageNotificationManager> {
    public final Provider<BindEventManager> bindEventManagerProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<CommonNotifCollection> notifCollectionProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public AnimatedImageNotificationManager_Factory(Provider<CommonNotifCollection> provider, Provider<BindEventManager> provider2, Provider<HeadsUpManager> provider3, Provider<StatusBarStateController> provider4) {
        this.notifCollectionProvider = provider;
        this.bindEventManagerProvider = provider2;
        this.headsUpManagerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
    }

    public AnimatedImageNotificationManager get() {
        return newInstance(this.notifCollectionProvider.get(), this.bindEventManagerProvider.get(), this.headsUpManagerProvider.get(), this.statusBarStateControllerProvider.get());
    }

    public static AnimatedImageNotificationManager_Factory create(Provider<CommonNotifCollection> provider, Provider<BindEventManager> provider2, Provider<HeadsUpManager> provider3, Provider<StatusBarStateController> provider4) {
        return new AnimatedImageNotificationManager_Factory(provider, provider2, provider3, provider4);
    }

    public static AnimatedImageNotificationManager newInstance(CommonNotifCollection commonNotifCollection, BindEventManager bindEventManager, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController) {
        return new AnimatedImageNotificationManager(commonNotifCollection, bindEventManager, headsUpManager, statusBarStateController);
    }
}
