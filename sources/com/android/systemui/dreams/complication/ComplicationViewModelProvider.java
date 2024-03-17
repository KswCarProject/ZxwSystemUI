package com.android.systemui.dreams.complication;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import com.android.systemui.dreams.complication.dagger.DaggerViewModelProviderFactory;

public class ComplicationViewModelProvider extends ViewModelProvider {
    public static /* synthetic */ ViewModel lambda$new$0(ComplicationViewModel complicationViewModel) {
        return complicationViewModel;
    }

    public ComplicationViewModelProvider(ViewModelStore viewModelStore, ComplicationViewModel complicationViewModel) {
        super(viewModelStore, new DaggerViewModelProviderFactory(new ComplicationViewModelProvider$$ExternalSyntheticLambda0(complicationViewModel)));
    }
}
