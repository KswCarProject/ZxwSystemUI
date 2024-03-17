package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationHostViewModule_ProvidesComplicationPaddingFactory implements Factory<Integer> {
    public final Provider<Resources> resourcesProvider;

    public ComplicationHostViewModule_ProvidesComplicationPaddingFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesComplicationPadding(this.resourcesProvider.get()));
    }

    public static ComplicationHostViewModule_ProvidesComplicationPaddingFactory create(Provider<Resources> provider) {
        return new ComplicationHostViewModule_ProvidesComplicationPaddingFactory(provider);
    }

    public static int providesComplicationPadding(Resources resources) {
        return ComplicationHostViewModule.providesComplicationPadding(resources);
    }
}
