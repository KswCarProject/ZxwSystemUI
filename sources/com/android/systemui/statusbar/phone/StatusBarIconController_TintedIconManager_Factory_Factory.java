package com.android.systemui.statusbar.phone;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarIconController_TintedIconManager_Factory_Factory implements Factory<StatusBarIconController.TintedIconManager.Factory> {
    public final Provider<FeatureFlags> featureFlagsProvider;

    public StatusBarIconController_TintedIconManager_Factory_Factory(Provider<FeatureFlags> provider) {
        this.featureFlagsProvider = provider;
    }

    public StatusBarIconController.TintedIconManager.Factory get() {
        return newInstance(this.featureFlagsProvider.get());
    }

    public static StatusBarIconController_TintedIconManager_Factory_Factory create(Provider<FeatureFlags> provider) {
        return new StatusBarIconController_TintedIconManager_Factory_Factory(provider);
    }

    public static StatusBarIconController.TintedIconManager.Factory newInstance(FeatureFlags featureFlags) {
        return new StatusBarIconController.TintedIconManager.Factory(featureFlags);
    }
}
