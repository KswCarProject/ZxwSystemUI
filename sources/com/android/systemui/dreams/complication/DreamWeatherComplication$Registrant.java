package com.android.systemui.dreams.complication;

import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;

public class DreamWeatherComplication$Registrant extends CoreStartable {
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final LockscreenSmartspaceController mSmartSpaceController;

    public void start() {
        if (this.mSmartSpaceController.isEnabled()) {
            this.mDreamOverlayStateController.addComplication((Complication) null);
        }
    }
}
