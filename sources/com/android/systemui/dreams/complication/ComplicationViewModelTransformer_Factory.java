package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.dagger.ComplicationViewModelComponent;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationViewModelTransformer_Factory implements Factory<ComplicationViewModelTransformer> {
    public final Provider<ComplicationViewModelComponent.Factory> viewModelComponentFactoryProvider;

    public ComplicationViewModelTransformer_Factory(Provider<ComplicationViewModelComponent.Factory> provider) {
        this.viewModelComponentFactoryProvider = provider;
    }

    public ComplicationViewModelTransformer get() {
        return newInstance(this.viewModelComponentFactoryProvider.get());
    }

    public static ComplicationViewModelTransformer_Factory create(Provider<ComplicationViewModelComponent.Factory> provider) {
        return new ComplicationViewModelTransformer_Factory(provider);
    }

    public static ComplicationViewModelTransformer newInstance(ComplicationViewModelComponent.Factory factory) {
        return new ComplicationViewModelTransformer(factory);
    }
}
