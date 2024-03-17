package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationsModule_ProvideGroupExpansionManagerFactory implements Factory<GroupExpansionManager> {
    public final Provider<NotificationGroupManagerLegacy> groupManagerLegacyProvider;
    public final Provider<GroupMembershipManager> groupMembershipManagerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;

    public NotificationsModule_ProvideGroupExpansionManagerFactory(Provider<NotifPipelineFlags> provider, Provider<GroupMembershipManager> provider2, Provider<NotificationGroupManagerLegacy> provider3) {
        this.notifPipelineFlagsProvider = provider;
        this.groupMembershipManagerProvider = provider2;
        this.groupManagerLegacyProvider = provider3;
    }

    public GroupExpansionManager get() {
        return provideGroupExpansionManager(this.notifPipelineFlagsProvider.get(), DoubleCheck.lazy(this.groupMembershipManagerProvider), DoubleCheck.lazy(this.groupManagerLegacyProvider));
    }

    public static NotificationsModule_ProvideGroupExpansionManagerFactory create(Provider<NotifPipelineFlags> provider, Provider<GroupMembershipManager> provider2, Provider<NotificationGroupManagerLegacy> provider3) {
        return new NotificationsModule_ProvideGroupExpansionManagerFactory(provider, provider2, provider3);
    }

    public static GroupExpansionManager provideGroupExpansionManager(NotifPipelineFlags notifPipelineFlags, Lazy<GroupMembershipManager> lazy, Lazy<NotificationGroupManagerLegacy> lazy2) {
        return (GroupExpansionManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideGroupExpansionManager(notifPipelineFlags, lazy, lazy2));
    }
}
