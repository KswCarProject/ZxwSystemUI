package com.android.systemui.dreams.complication.dagger;

import androidx.lifecycle.ViewModel;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel;
import com.android.systemui.dreams.complication.dagger.DaggerViewModelProviderFactory;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ComplicationModule$$ExternalSyntheticLambda0 implements DaggerViewModelProviderFactory.ViewModelCreator {
    public final /* synthetic */ ComplicationCollectionViewModel f$0;

    public /* synthetic */ ComplicationModule$$ExternalSyntheticLambda0(ComplicationCollectionViewModel complicationCollectionViewModel) {
        this.f$0 = complicationCollectionViewModel;
    }

    public final ViewModel create() {
        return ComplicationModule.lambda$providesComplicationCollectionViewModel$0(this.f$0);
    }
}
