package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationHostViewModule_ProvidesComplicationsFadeOutDurationFactory implements Factory<Integer> {
    public final Provider<Resources> resourcesProvider;

    public ComplicationHostViewModule_ProvidesComplicationsFadeOutDurationFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesComplicationsFadeOutDuration(this.resourcesProvider.get()));
    }

    public static ComplicationHostViewModule_ProvidesComplicationsFadeOutDurationFactory create(Provider<Resources> provider) {
        return new ComplicationHostViewModule_ProvidesComplicationsFadeOutDurationFactory(provider);
    }

    public static int providesComplicationsFadeOutDuration(Resources resources) {
        return ComplicationHostViewModule.providesComplicationsFadeOutDuration(resources);
    }
}
