package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_ProvidesStatusBarWindowViewFactory implements Factory<NotificationShelfController> {
    public final Provider<NotificationShelfComponent.Builder> notificationShelfComponentBuilderProvider;
    public final Provider<NotificationShelf> notificationShelfProvider;

    public StatusBarViewModule_ProvidesStatusBarWindowViewFactory(Provider<NotificationShelfComponent.Builder> provider, Provider<NotificationShelf> provider2) {
        this.notificationShelfComponentBuilderProvider = provider;
        this.notificationShelfProvider = provider2;
    }

    public NotificationShelfController get() {
        return providesStatusBarWindowView(this.notificationShelfComponentBuilderProvider.get(), this.notificationShelfProvider.get());
    }

    public static StatusBarViewModule_ProvidesStatusBarWindowViewFactory create(Provider<NotificationShelfComponent.Builder> provider, Provider<NotificationShelf> provider2) {
        return new StatusBarViewModule_ProvidesStatusBarWindowViewFactory(provider, provider2);
    }

    public static NotificationShelfController providesStatusBarWindowView(NotificationShelfComponent.Builder builder, NotificationShelf notificationShelf) {
        return (NotificationShelfController) Preconditions.checkNotNullFromProvides(StatusBarViewModule.providesStatusBarWindowView(builder, notificationShelf));
    }
}
