package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationsModule_ProvideOnUserInteractionCallbackFactory implements Factory<OnUserInteractionCallback> {
    public final Provider<NotificationEntryManager> entryManagerProvider;
    public final Provider<GroupMembershipManager> groupMembershipManagerLazyProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<NotifCollection> notifCollectionProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;
    public final Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;
    public final Provider<VisualStabilityManager> visualStabilityManagerProvider;

    public NotificationsModule_ProvideOnUserInteractionCallbackFactory(Provider<NotifPipelineFlags> provider, Provider<HeadsUpManager> provider2, Provider<StatusBarStateController> provider3, Provider<NotifCollection> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<VisualStabilityCoordinator> provider6, Provider<NotificationEntryManager> provider7, Provider<VisualStabilityManager> provider8, Provider<GroupMembershipManager> provider9) {
        this.notifPipelineFlagsProvider = provider;
        this.headsUpManagerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.notifCollectionProvider = provider4;
        this.visibilityProvider = provider5;
        this.visualStabilityCoordinatorProvider = provider6;
        this.entryManagerProvider = provider7;
        this.visualStabilityManagerProvider = provider8;
        this.groupMembershipManagerLazyProvider = provider9;
    }

    public OnUserInteractionCallback get() {
        return provideOnUserInteractionCallback(this.notifPipelineFlagsProvider.get(), this.headsUpManagerProvider.get(), this.statusBarStateControllerProvider.get(), DoubleCheck.lazy(this.notifCollectionProvider), DoubleCheck.lazy(this.visibilityProvider), DoubleCheck.lazy(this.visualStabilityCoordinatorProvider), this.entryManagerProvider.get(), this.visualStabilityManagerProvider.get(), DoubleCheck.lazy(this.groupMembershipManagerLazyProvider));
    }

    public static NotificationsModule_ProvideOnUserInteractionCallbackFactory create(Provider<NotifPipelineFlags> provider, Provider<HeadsUpManager> provider2, Provider<StatusBarStateController> provider3, Provider<NotifCollection> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<VisualStabilityCoordinator> provider6, Provider<NotificationEntryManager> provider7, Provider<VisualStabilityManager> provider8, Provider<GroupMembershipManager> provider9) {
        return new NotificationsModule_ProvideOnUserInteractionCallbackFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static OnUserInteractionCallback provideOnUserInteractionCallback(NotifPipelineFlags notifPipelineFlags, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, Lazy<NotifCollection> lazy, Lazy<NotificationVisibilityProvider> lazy2, Lazy<VisualStabilityCoordinator> lazy3, NotificationEntryManager notificationEntryManager, VisualStabilityManager visualStabilityManager, Lazy<GroupMembershipManager> lazy4) {
        return (OnUserInteractionCallback) Preconditions.checkNotNullFromProvides(NotificationsModule.provideOnUserInteractionCallback(notifPipelineFlags, headsUpManager, statusBarStateController, lazy, lazy2, lazy3, notificationEntryManager, visualStabilityManager, lazy4));
    }
}
