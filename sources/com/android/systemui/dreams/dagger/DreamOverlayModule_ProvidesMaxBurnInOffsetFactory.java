package com.android.systemui.dreams.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesMaxBurnInOffsetFactory implements Factory<Integer> {
    public final Provider<Resources> resourcesProvider;

    public DreamOverlayModule_ProvidesMaxBurnInOffsetFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesMaxBurnInOffset(this.resourcesProvider.get()));
    }

    public static DreamOverlayModule_ProvidesMaxBurnInOffsetFactory create(Provider<Resources> provider) {
        return new DreamOverlayModule_ProvidesMaxBurnInOffsetFactory(provider);
    }

    public static int providesMaxBurnInOffset(Resources resources) {
        return DreamOverlayModule.providesMaxBurnInOffset(resources);
    }
}
