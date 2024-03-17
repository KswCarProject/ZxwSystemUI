package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.icon.ConversationIconManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConversationCoordinator_Factory implements Factory<ConversationCoordinator> {
    public final Provider<ConversationIconManager> conversationIconManagerProvider;
    public final Provider<NodeController> peopleHeaderControllerProvider;
    public final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;

    public ConversationCoordinator_Factory(Provider<PeopleNotificationIdentifier> provider, Provider<ConversationIconManager> provider2, Provider<NodeController> provider3) {
        this.peopleNotificationIdentifierProvider = provider;
        this.conversationIconManagerProvider = provider2;
        this.peopleHeaderControllerProvider = provider3;
    }

    public ConversationCoordinator get() {
        return newInstance(this.peopleNotificationIdentifierProvider.get(), this.conversationIconManagerProvider.get(), this.peopleHeaderControllerProvider.get());
    }

    public static ConversationCoordinator_Factory create(Provider<PeopleNotificationIdentifier> provider, Provider<ConversationIconManager> provider2, Provider<NodeController> provider3) {
        return new ConversationCoordinator_Factory(provider, provider2, provider3);
    }

    public static ConversationCoordinator newInstance(PeopleNotificationIdentifier peopleNotificationIdentifier, ConversationIconManager conversationIconManager, NodeController nodeController) {
        return new ConversationCoordinator(peopleNotificationIdentifier, conversationIconManager, nodeController);
    }
}
