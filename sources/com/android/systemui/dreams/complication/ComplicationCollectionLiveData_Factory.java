package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.DreamOverlayStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationCollectionLiveData_Factory implements Factory<ComplicationCollectionLiveData> {
    public final Provider<DreamOverlayStateController> stateControllerProvider;

    public ComplicationCollectionLiveData_Factory(Provider<DreamOverlayStateController> provider) {
        this.stateControllerProvider = provider;
    }

    public ComplicationCollectionLiveData get() {
        return newInstance(this.stateControllerProvider.get());
    }

    public static ComplicationCollectionLiveData_Factory create(Provider<DreamOverlayStateController> provider) {
        return new ComplicationCollectionLiveData_Factory(provider);
    }

    public static ComplicationCollectionLiveData newInstance(DreamOverlayStateController dreamOverlayStateController) {
        return new ComplicationCollectionLiveData(dreamOverlayStateController);
    }
}
