package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConversationNotificationManager_Factory implements Factory<ConversationNotificationManager> {
    public final Provider<BindEventManager> bindEventManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<NotifPipelineFlags> featureFlagsProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<CommonNotifCollection> notifCollectionProvider;
    public final Provider<NotificationGroupManagerLegacy> notificationGroupManagerProvider;

    public ConversationNotificationManager_Factory(Provider<BindEventManager> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<Context> provider3, Provider<CommonNotifCollection> provider4, Provider<NotifPipelineFlags> provider5, Provider<Handler> provider6) {
        this.bindEventManagerProvider = provider;
        this.notificationGroupManagerProvider = provider2;
        this.contextProvider = provider3;
        this.notifCollectionProvider = provider4;
        this.featureFlagsProvider = provider5;
        this.mainHandlerProvider = provider6;
    }

    public ConversationNotificationManager get() {
        return newInstance(this.bindEventManagerProvider.get(), this.notificationGroupManagerProvider.get(), this.contextProvider.get(), this.notifCollectionProvider.get(), this.featureFlagsProvider.get(), this.mainHandlerProvider.get());
    }

    public static ConversationNotificationManager_Factory create(Provider<BindEventManager> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<Context> provider3, Provider<CommonNotifCollection> provider4, Provider<NotifPipelineFlags> provider5, Provider<Handler> provider6) {
        return new ConversationNotificationManager_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static ConversationNotificationManager newInstance(BindEventManager bindEventManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, Context context, CommonNotifCollection commonNotifCollection, NotifPipelineFlags notifPipelineFlags, Handler handler) {
        return new ConversationNotificationManager(bindEventManager, notificationGroupManagerLegacy, context, commonNotifCollection, notifPipelineFlags, handler);
    }
}
