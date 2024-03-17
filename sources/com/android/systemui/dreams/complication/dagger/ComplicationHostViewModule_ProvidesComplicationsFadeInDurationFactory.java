package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationHostViewModule_ProvidesComplicationsFadeInDurationFactory implements Factory<Integer> {
    public final Provider<Resources> resourcesProvider;

    public ComplicationHostViewModule_ProvidesComplicationsFadeInDurationFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesComplicationsFadeInDuration(this.resourcesProvider.get()));
    }

    public static ComplicationHostViewModule_ProvidesComplicationsFadeInDurationFactory create(Provider<Resources> provider) {
        return new ComplicationHostViewModule_ProvidesComplicationsFadeInDurationFactory(provider);
    }

    public static int providesComplicationsFadeInDuration(Resources resources) {
        return ComplicationHostViewModule.providesComplicationsFadeInDuration(resources);
    }
}
