package com.android.systemui.statusbar.phone.userswitcher;

import com.android.systemui.flags.FeatureFlags;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarUserSwitcherFeatureController_Factory implements Factory<StatusBarUserSwitcherFeatureController> {
    public final Provider<FeatureFlags> flagsProvider;

    public StatusBarUserSwitcherFeatureController_Factory(Provider<FeatureFlags> provider) {
        this.flagsProvider = provider;
    }

    public StatusBarUserSwitcherFeatureController get() {
        return newInstance(this.flagsProvider.get());
    }

    public static StatusBarUserSwitcherFeatureController_Factory create(Provider<FeatureFlags> provider) {
        return new StatusBarUserSwitcherFeatureController_Factory(provider);
    }

    public static StatusBarUserSwitcherFeatureController newInstance(FeatureFlags featureFlags) {
        return new StatusBarUserSwitcherFeatureController(featureFlags);
    }
}
