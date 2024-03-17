package com.android.systemui.statusbar.phone.dagger;

import android.view.View;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_GetLargeScreenShadeHeaderBarViewFactory implements Factory<View> {
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<NotificationShadeWindowView> notificationShadeWindowViewProvider;

    public StatusBarViewModule_GetLargeScreenShadeHeaderBarViewFactory(Provider<NotificationShadeWindowView> provider, Provider<FeatureFlags> provider2) {
        this.notificationShadeWindowViewProvider = provider;
        this.featureFlagsProvider = provider2;
    }

    public View get() {
        return getLargeScreenShadeHeaderBarView(this.notificationShadeWindowViewProvider.get(), this.featureFlagsProvider.get());
    }

    public static StatusBarViewModule_GetLargeScreenShadeHeaderBarViewFactory create(Provider<NotificationShadeWindowView> provider, Provider<FeatureFlags> provider2) {
        return new StatusBarViewModule_GetLargeScreenShadeHeaderBarViewFactory(provider, provider2);
    }

    public static View getLargeScreenShadeHeaderBarView(NotificationShadeWindowView notificationShadeWindowView, FeatureFlags featureFlags) {
        return (View) Preconditions.checkNotNullFromProvides(StatusBarViewModule.getLargeScreenShadeHeaderBarView(notificationShadeWindowView, featureFlags));
    }
}
