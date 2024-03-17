package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import android.view.ViewConfiguration;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationSwipeHelper_Builder_Factory implements Factory<NotificationSwipeHelper.Builder> {
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ViewConfiguration> viewConfigurationProvider;

    public NotificationSwipeHelper_Builder_Factory(Provider<Resources> provider, Provider<ViewConfiguration> provider2, Provider<FalsingManager> provider3, Provider<FeatureFlags> provider4) {
        this.resourcesProvider = provider;
        this.viewConfigurationProvider = provider2;
        this.falsingManagerProvider = provider3;
        this.featureFlagsProvider = provider4;
    }

    public NotificationSwipeHelper.Builder get() {
        return newInstance(this.resourcesProvider.get(), this.viewConfigurationProvider.get(), this.falsingManagerProvider.get(), this.featureFlagsProvider.get());
    }

    public static NotificationSwipeHelper_Builder_Factory create(Provider<Resources> provider, Provider<ViewConfiguration> provider2, Provider<FalsingManager> provider3, Provider<FeatureFlags> provider4) {
        return new NotificationSwipeHelper_Builder_Factory(provider, provider2, provider3, provider4);
    }

    public static NotificationSwipeHelper.Builder newInstance(Resources resources, ViewConfiguration viewConfiguration, FalsingManager falsingManager, FeatureFlags featureFlags) {
        return new NotificationSwipeHelper.Builder(resources, viewConfiguration, falsingManager, featureFlags);
    }
}
