package com.android.systemui.people.widget;

import android.os.UserManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.wmshell.BubblesManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class LaunchConversationActivity_Factory implements Factory<LaunchConversationActivity> {
    public final Provider<Optional<BubblesManager>> bubblesManagerOptionalProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<CommonNotifCollection> commonNotifCollectionProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;

    public LaunchConversationActivity_Factory(Provider<NotificationVisibilityProvider> provider, Provider<CommonNotifCollection> provider2, Provider<Optional<BubblesManager>> provider3, Provider<UserManager> provider4, Provider<CommandQueue> provider5) {
        this.visibilityProvider = provider;
        this.commonNotifCollectionProvider = provider2;
        this.bubblesManagerOptionalProvider = provider3;
        this.userManagerProvider = provider4;
        this.commandQueueProvider = provider5;
    }

    public LaunchConversationActivity get() {
        return newInstance(this.visibilityProvider.get(), this.commonNotifCollectionProvider.get(), this.bubblesManagerOptionalProvider.get(), this.userManagerProvider.get(), this.commandQueueProvider.get());
    }

    public static LaunchConversationActivity_Factory create(Provider<NotificationVisibilityProvider> provider, Provider<CommonNotifCollection> provider2, Provider<Optional<BubblesManager>> provider3, Provider<UserManager> provider4, Provider<CommandQueue> provider5) {
        return new LaunchConversationActivity_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static LaunchConversationActivity newInstance(NotificationVisibilityProvider notificationVisibilityProvider, CommonNotifCollection commonNotifCollection, Optional<BubblesManager> optional, UserManager userManager, CommandQueue commandQueue) {
        return new LaunchConversationActivity(notificationVisibilityProvider, commonNotifCollection, optional, userManager, commandQueue);
    }
}
