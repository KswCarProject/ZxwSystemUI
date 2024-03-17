package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationHostViewModule_ProvidesComplicationsRestoreTimeoutFactory implements Factory<Integer> {
    public final Provider<Resources> resourcesProvider;

    public ComplicationHostViewModule_ProvidesComplicationsRestoreTimeoutFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesComplicationsRestoreTimeout(this.resourcesProvider.get()));
    }

    public static ComplicationHostViewModule_ProvidesComplicationsRestoreTimeoutFactory create(Provider<Resources> provider) {
        return new ComplicationHostViewModule_ProvidesComplicationsRestoreTimeoutFactory(provider);
    }

    public static int providesComplicationsRestoreTimeout(Resources resources) {
        return ComplicationHostViewModule.providesComplicationsRestoreTimeout(resources);
    }
}
