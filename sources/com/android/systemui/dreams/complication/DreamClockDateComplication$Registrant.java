package com.android.systemui.dreams.complication;

import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;

public class DreamClockDateComplication$Registrant extends CoreStartable {
    public final DreamOverlayStateController mDreamOverlayStateController;

    public void start() {
        this.mDreamOverlayStateController.addComplication((Complication) null);
    }
}
