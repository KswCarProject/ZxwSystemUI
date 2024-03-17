package com.android.systemui.dreams.complication.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine;

public interface ComplicationModule {
    static /* synthetic */ ViewModel lambda$providesComplicationCollectionViewModel$0(ComplicationCollectionViewModel complicationCollectionViewModel) {
        return complicationCollectionViewModel;
    }

    static Complication.VisibilityController providesVisibilityController(ComplicationLayoutEngine complicationLayoutEngine) {
        return complicationLayoutEngine;
    }

    static ComplicationCollectionViewModel providesComplicationCollectionViewModel(ViewModelStore viewModelStore, ComplicationCollectionViewModel complicationCollectionViewModel) {
        return (ComplicationCollectionViewModel) new ViewModelProvider(viewModelStore, new DaggerViewModelProviderFactory(new ComplicationModule$$ExternalSyntheticLambda0(complicationCollectionViewModel))).get(ComplicationCollectionViewModel.class);
    }
}
