package com.android.systemui.dreams.complication;

import androidx.lifecycle.ViewModel;
import com.android.systemui.dreams.complication.dagger.DaggerViewModelProviderFactory;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ComplicationViewModelProvider$$ExternalSyntheticLambda0 implements DaggerViewModelProviderFactory.ViewModelCreator {
    public final /* synthetic */ ComplicationViewModel f$0;

    public /* synthetic */ ComplicationViewModelProvider$$ExternalSyntheticLambda0(ComplicationViewModel complicationViewModel) {
        this.f$0 = complicationViewModel;
    }

    public final ViewModel create() {
        return ComplicationViewModelProvider.lambda$new$0(this.f$0);
    }
}
