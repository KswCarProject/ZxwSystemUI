package com.android.systemui.dreams.complication;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationHostViewController_Factory implements Factory<ComplicationHostViewController> {
    public final Provider<ComplicationLayoutEngine> layoutEngineProvider;
    public final Provider<LifecycleOwner> lifecycleOwnerProvider;
    public final Provider<ComplicationCollectionViewModel> viewModelProvider;
    public final Provider<ConstraintLayout> viewProvider;

    public ComplicationHostViewController_Factory(Provider<ConstraintLayout> provider, Provider<ComplicationLayoutEngine> provider2, Provider<LifecycleOwner> provider3, Provider<ComplicationCollectionViewModel> provider4) {
        this.viewProvider = provider;
        this.layoutEngineProvider = provider2;
        this.lifecycleOwnerProvider = provider3;
        this.viewModelProvider = provider4;
    }

    public ComplicationHostViewController get() {
        return newInstance(this.viewProvider.get(), this.layoutEngineProvider.get(), this.lifecycleOwnerProvider.get(), this.viewModelProvider.get());
    }

    public static ComplicationHostViewController_Factory create(Provider<ConstraintLayout> provider, Provider<ComplicationLayoutEngine> provider2, Provider<LifecycleOwner> provider3, Provider<ComplicationCollectionViewModel> provider4) {
        return new ComplicationHostViewController_Factory(provider, provider2, provider3, provider4);
    }

    public static ComplicationHostViewController newInstance(ConstraintLayout constraintLayout, ComplicationLayoutEngine complicationLayoutEngine, LifecycleOwner lifecycleOwner, ComplicationCollectionViewModel complicationCollectionViewModel) {
        return new ComplicationHostViewController(constraintLayout, complicationLayoutEngine, lifecycleOwner, complicationCollectionViewModel);
    }
}
