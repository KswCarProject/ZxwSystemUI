package com.android.systemui.statusbar.notification.collection.provider;

import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class HighPriorityProvider_Factory implements Factory<HighPriorityProvider> {
    public final Provider<GroupMembershipManager> groupManagerProvider;
    public final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;

    public HighPriorityProvider_Factory(Provider<PeopleNotificationIdentifier> provider, Provider<GroupMembershipManager> provider2) {
        this.peopleNotificationIdentifierProvider = provider;
        this.groupManagerProvider = provider2;
    }

    public HighPriorityProvider get() {
        return newInstance(this.peopleNotificationIdentifierProvider.get(), this.groupManagerProvider.get());
    }

    public static HighPriorityProvider_Factory create(Provider<PeopleNotificationIdentifier> provider, Provider<GroupMembershipManager> provider2) {
        return new HighPriorityProvider_Factory(provider, provider2);
    }

    public static HighPriorityProvider newInstance(PeopleNotificationIdentifier peopleNotificationIdentifier, GroupMembershipManager groupMembershipManager) {
        return new HighPriorityProvider(peopleNotificationIdentifier, groupMembershipManager);
    }
}
