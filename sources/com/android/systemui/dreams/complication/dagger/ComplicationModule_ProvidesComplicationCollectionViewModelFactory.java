package com.android.systemui.dreams.complication.dagger;

import androidx.lifecycle.ViewModelStore;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ComplicationModule_ProvidesComplicationCollectionViewModelFactory implements Factory<ComplicationCollectionViewModel> {
    public final Provider<ViewModelStore> storeProvider;
    public final Provider<ComplicationCollectionViewModel> viewModelProvider;

    public ComplicationModule_ProvidesComplicationCollectionViewModelFactory(Provider<ViewModelStore> provider, Provider<ComplicationCollectionViewModel> provider2) {
        this.storeProvider = provider;
        this.viewModelProvider = provider2;
    }

    public ComplicationCollectionViewModel get() {
        return providesComplicationCollectionViewModel(this.storeProvider.get(), this.viewModelProvider.get());
    }

    public static ComplicationModule_ProvidesComplicationCollectionViewModelFactory create(Provider<ViewModelStore> provider, Provider<ComplicationCollectionViewModel> provider2) {
        return new ComplicationModule_ProvidesComplicationCollectionViewModelFactory(provider, provider2);
    }

    public static ComplicationCollectionViewModel providesComplicationCollectionViewModel(ViewModelStore viewModelStore, ComplicationCollectionViewModel complicationCollectionViewModel) {
        return (ComplicationCollectionViewModel) Preconditions.checkNotNullFromProvides(ComplicationModule.providesComplicationCollectionViewModel(viewModelStore, complicationCollectionViewModel));
    }
}
