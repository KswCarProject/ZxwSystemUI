package com.android.systemui.statusbar.phone.dagger;

import android.view.LayoutInflater;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_ProvidesNotificationShelfFactory implements Factory<NotificationShelf> {
    public final Provider<LayoutInflater> layoutInflaterProvider;
    public final Provider<NotificationStackScrollLayout> notificationStackScrollLayoutProvider;

    public StatusBarViewModule_ProvidesNotificationShelfFactory(Provider<LayoutInflater> provider, Provider<NotificationStackScrollLayout> provider2) {
        this.layoutInflaterProvider = provider;
        this.notificationStackScrollLayoutProvider = provider2;
    }

    public NotificationShelf get() {
        return providesNotificationShelf(this.layoutInflaterProvider.get(), this.notificationStackScrollLayoutProvider.get());
    }

    public static StatusBarViewModule_ProvidesNotificationShelfFactory create(Provider<LayoutInflater> provider, Provider<NotificationStackScrollLayout> provider2) {
        return new StatusBarViewModule_ProvidesNotificationShelfFactory(provider, provider2);
    }

    public static NotificationShelf providesNotificationShelf(LayoutInflater layoutInflater, NotificationStackScrollLayout notificationStackScrollLayout) {
        return (NotificationShelf) Preconditions.checkNotNullFromProvides(StatusBarViewModule.providesNotificationShelf(layoutInflater, notificationStackScrollLayout));
    }
}
