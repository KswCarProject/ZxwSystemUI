package com.android.systemui.dreams.complication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import java.util.Collection;
import java.util.stream.Collectors;

public class ComplicationCollectionViewModel extends ViewModel {
    public final LiveData<Collection<ComplicationViewModel>> mComplications;
    public final ComplicationViewModelTransformer mTransformer;

    public ComplicationCollectionViewModel(ComplicationCollectionLiveData complicationCollectionLiveData, ComplicationViewModelTransformer complicationViewModelTransformer) {
        this.mComplications = Transformations.map(complicationCollectionLiveData, new ComplicationCollectionViewModel$$ExternalSyntheticLambda0(this));
        this.mTransformer = complicationViewModelTransformer;
    }

    /* renamed from: convert */
    public final Collection<ComplicationViewModel> lambda$new$0(Collection<Complication> collection) {
        return (Collection) collection.stream().map(new ComplicationCollectionViewModel$$ExternalSyntheticLambda1(this)).collect(Collectors.toSet());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ ComplicationViewModel lambda$convert$1(Complication complication) {
        return this.mTransformer.getViewModel(complication);
    }

    public LiveData<Collection<ComplicationViewModel>> getComplications() {
        return this.mComplications;
    }
}
