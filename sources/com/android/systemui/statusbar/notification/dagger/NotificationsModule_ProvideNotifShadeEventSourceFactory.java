package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationPresenterExtensions;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationsModule_ProvideNotifShadeEventSourceFactory implements Factory<NotifShadeEventSource> {
    public final Provider<LegacyNotificationPresenterExtensions> legacyNotificationPresenterExtensionsLazyProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<ShadeEventCoordinator> shadeEventCoordinatorLazyProvider;

    public NotificationsModule_ProvideNotifShadeEventSourceFactory(Provider<NotifPipelineFlags> provider, Provider<ShadeEventCoordinator> provider2, Provider<LegacyNotificationPresenterExtensions> provider3) {
        this.notifPipelineFlagsProvider = provider;
        this.shadeEventCoordinatorLazyProvider = provider2;
        this.legacyNotificationPresenterExtensionsLazyProvider = provider3;
    }

    public NotifShadeEventSource get() {
        return provideNotifShadeEventSource(this.notifPipelineFlagsProvider.get(), DoubleCheck.lazy(this.shadeEventCoordinatorLazyProvider), DoubleCheck.lazy(this.legacyNotificationPresenterExtensionsLazyProvider));
    }

    public static NotificationsModule_ProvideNotifShadeEventSourceFactory create(Provider<NotifPipelineFlags> provider, Provider<ShadeEventCoordinator> provider2, Provider<LegacyNotificationPresenterExtensions> provider3) {
        return new NotificationsModule_ProvideNotifShadeEventSourceFactory(provider, provider2, provider3);
    }

    public static NotifShadeEventSource provideNotifShadeEventSource(NotifPipelineFlags notifPipelineFlags, Lazy<ShadeEventCoordinator> lazy, Lazy<LegacyNotificationPresenterExtensions> lazy2) {
        return (NotifShadeEventSource) Preconditions.checkNotNullFromProvides(NotificationsModule.provideNotifShadeEventSource(notifPipelineFlags, lazy, lazy2));
    }
}
