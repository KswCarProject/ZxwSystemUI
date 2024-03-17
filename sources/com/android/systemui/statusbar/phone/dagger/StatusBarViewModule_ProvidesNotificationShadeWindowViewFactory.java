package com.android.systemui.statusbar.phone.dagger;

import android.view.LayoutInflater;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_ProvidesNotificationShadeWindowViewFactory implements Factory<NotificationShadeWindowView> {
    public final Provider<LayoutInflater> layoutInflaterProvider;

    public StatusBarViewModule_ProvidesNotificationShadeWindowViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }

    public NotificationShadeWindowView get() {
        return providesNotificationShadeWindowView(this.layoutInflaterProvider.get());
    }

    public static StatusBarViewModule_ProvidesNotificationShadeWindowViewFactory create(Provider<LayoutInflater> provider) {
        return new StatusBarViewModule_ProvidesNotificationShadeWindowViewFactory(provider);
    }

    public static NotificationShadeWindowView providesNotificationShadeWindowView(LayoutInflater layoutInflater) {
        return (NotificationShadeWindowView) Preconditions.checkNotNullFromProvides(StatusBarViewModule.providesNotificationShadeWindowView(layoutInflater));
    }
}
