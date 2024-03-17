package com.android.systemui.dreams.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory implements Factory<Long> {
    public final Provider<Resources> resourcesProvider;

    public DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Long get() {
        return Long.valueOf(providesBurnInProtectionUpdateInterval(this.resourcesProvider.get()));
    }

    public static DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory create(Provider<Resources> provider) {
        return new DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory(provider);
    }

    public static long providesBurnInProtectionUpdateInterval(Resources resources) {
        return DreamOverlayModule.providesBurnInProtectionUpdateInterval(resources);
    }
}
