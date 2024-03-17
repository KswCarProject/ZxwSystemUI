package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationsModule_ProvideGroupMembershipManagerFactory implements Factory<GroupMembershipManager> {
    public final Provider<NotificationGroupManagerLegacy> groupManagerLegacyProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;

    public NotificationsModule_ProvideGroupMembershipManagerFactory(Provider<NotifPipelineFlags> provider, Provider<NotificationGroupManagerLegacy> provider2) {
        this.notifPipelineFlagsProvider = provider;
        this.groupManagerLegacyProvider = provider2;
    }

    public GroupMembershipManager get() {
        return provideGroupMembershipManager(this.notifPipelineFlagsProvider.get(), DoubleCheck.lazy(this.groupManagerLegacyProvider));
    }

    public static NotificationsModule_ProvideGroupMembershipManagerFactory create(Provider<NotifPipelineFlags> provider, Provider<NotificationGroupManagerLegacy> provider2) {
        return new NotificationsModule_ProvideGroupMembershipManagerFactory(provider, provider2);
    }

    public static GroupMembershipManager provideGroupMembershipManager(NotifPipelineFlags notifPipelineFlags, Lazy<NotificationGroupManagerLegacy> lazy) {
        return (GroupMembershipManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideGroupMembershipManager(notifPipelineFlags, lazy));
    }
}
