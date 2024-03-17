package com.android.systemui.statusbar.policy.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory implements Factory<String[]> {
    public final Provider<Resources> resourcesProvider;

    public StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public String[] get() {
        return providesDeviceStateRotationLockDefaults(this.resourcesProvider.get());
    }

    public static StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory create(Provider<Resources> provider) {
        return new StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory(provider);
    }

    public static String[] providesDeviceStateRotationLockDefaults(Resources resources) {
        return (String[]) Preconditions.checkNotNullFromProvides(StatusBarPolicyModule.providesDeviceStateRotationLockDefaults(resources));
    }
}
