package com.android.systemui.dreams.complication;

import androidx.lifecycle.LiveData;
import com.android.systemui.dreams.DreamOverlayStateController;
import java.util.Collection;

public class ComplicationCollectionLiveData extends LiveData<Collection<Complication>> {
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final DreamOverlayStateController.Callback mStateControllerCallback = new DreamOverlayStateController.Callback() {
        public void onComplicationsChanged() {
            ComplicationCollectionLiveData complicationCollectionLiveData = ComplicationCollectionLiveData.this;
            complicationCollectionLiveData.setValue(complicationCollectionLiveData.mDreamOverlayStateController.getComplications());
        }

        public void onAvailableComplicationTypesChanged() {
            ComplicationCollectionLiveData complicationCollectionLiveData = ComplicationCollectionLiveData.this;
            complicationCollectionLiveData.setValue(complicationCollectionLiveData.mDreamOverlayStateController.getComplications());
        }
    };

    public ComplicationCollectionLiveData(DreamOverlayStateController dreamOverlayStateController) {
        this.mDreamOverlayStateController = dreamOverlayStateController;
    }

    public void onActive() {
        super.onActive();
        this.mDreamOverlayStateController.addCallback(this.mStateControllerCallback);
        setValue(this.mDreamOverlayStateController.getComplications());
    }

    public void onInactive() {
        this.mDreamOverlayStateController.removeCallback(this.mStateControllerCallback);
        super.onInactive();
    }
}
