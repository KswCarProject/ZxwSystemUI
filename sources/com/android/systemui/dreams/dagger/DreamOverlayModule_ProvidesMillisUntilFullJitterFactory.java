package com.android.systemui.dreams.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesMillisUntilFullJitterFactory implements Factory<Long> {
    public final Provider<Resources> resourcesProvider;

    public DreamOverlayModule_ProvidesMillisUntilFullJitterFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Long get() {
        return Long.valueOf(providesMillisUntilFullJitter(this.resourcesProvider.get()));
    }

    public static DreamOverlayModule_ProvidesMillisUntilFullJitterFactory create(Provider<Resources> provider) {
        return new DreamOverlayModule_ProvidesMillisUntilFullJitterFactory(provider);
    }

    public static long providesMillisUntilFullJitter(Resources resources) {
        return DreamOverlayModule.providesMillisUntilFullJitter(resources);
    }
}
