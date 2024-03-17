package com.android.systemui.dreams.complication.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DaggerViewModelProviderFactory implements ViewModelProvider.Factory {
    public final ViewModelCreator mCreator;

    public interface ViewModelCreator {
        ViewModel create();
    }

    public DaggerViewModelProviderFactory(ViewModelCreator viewModelCreator) {
        this.mCreator = viewModelCreator;
    }

    public <T extends ViewModel> T create(Class<T> cls) {
        return this.mCreator.create();
    }
}
