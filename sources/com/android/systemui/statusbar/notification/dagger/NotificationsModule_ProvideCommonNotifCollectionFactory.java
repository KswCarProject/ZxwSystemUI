package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationsModule_ProvideCommonNotifCollectionFactory implements Factory<CommonNotifCollection> {
    public final Provider<NotificationEntryManager> entryManagerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> pipelineProvider;

    public NotificationsModule_ProvideCommonNotifCollectionFactory(Provider<NotifPipelineFlags> provider, Provider<NotifPipeline> provider2, Provider<NotificationEntryManager> provider3) {
        this.notifPipelineFlagsProvider = provider;
        this.pipelineProvider = provider2;
        this.entryManagerProvider = provider3;
    }

    public CommonNotifCollection get() {
        return provideCommonNotifCollection(this.notifPipelineFlagsProvider.get(), DoubleCheck.lazy(this.pipelineProvider), this.entryManagerProvider.get());
    }

    public static NotificationsModule_ProvideCommonNotifCollectionFactory create(Provider<NotifPipelineFlags> provider, Provider<NotifPipeline> provider2, Provider<NotificationEntryManager> provider3) {
        return new NotificationsModule_ProvideCommonNotifCollectionFactory(provider, provider2, provider3);
    }

    public static CommonNotifCollection provideCommonNotifCollection(NotifPipelineFlags notifPipelineFlags, Lazy<NotifPipeline> lazy, NotificationEntryManager notificationEntryManager) {
        return (CommonNotifCollection) Preconditions.checkNotNullFromProvides(NotificationsModule.provideCommonNotifCollection(notifPipelineFlags, lazy, notificationEntryManager));
    }
}
