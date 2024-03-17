package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.statusbar.notification.icon.IconManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaCoordinator_Factory implements Factory<MediaCoordinator> {
    public final Provider<MediaFeatureFlag> featureFlagProvider;
    public final Provider<IconManager> iconManagerProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;

    public MediaCoordinator_Factory(Provider<MediaFeatureFlag> provider, Provider<IStatusBarService> provider2, Provider<IconManager> provider3) {
        this.featureFlagProvider = provider;
        this.statusBarServiceProvider = provider2;
        this.iconManagerProvider = provider3;
    }

    public MediaCoordinator get() {
        return newInstance(this.featureFlagProvider.get(), this.statusBarServiceProvider.get(), this.iconManagerProvider.get());
    }

    public static MediaCoordinator_Factory create(Provider<MediaFeatureFlag> provider, Provider<IStatusBarService> provider2, Provider<IconManager> provider3) {
        return new MediaCoordinator_Factory(provider, provider2, provider3);
    }

    public static MediaCoordinator newInstance(MediaFeatureFlag mediaFeatureFlag, IStatusBarService iStatusBarService, IconManager iconManager) {
        return new MediaCoordinator(mediaFeatureFlag, iStatusBarService, iconManager);
    }
}
