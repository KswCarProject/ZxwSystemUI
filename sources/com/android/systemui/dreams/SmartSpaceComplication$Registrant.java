package com.android.systemui.dreams;

import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.smartspace.DreamsSmartspaceController;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;

public class SmartSpaceComplication$Registrant extends CoreStartable {
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final BcSmartspaceDataPlugin.SmartspaceTargetListener mSmartspaceListener;

    public void start() {
        this.mDreamOverlayStateController.addCallback((DreamOverlayStateController.Callback) new DreamOverlayStateController.Callback() {
            public void onStateChanged() {
                if (SmartSpaceComplication$Registrant.this.mDreamOverlayStateController.isOverlayActive()) {
                    DreamsSmartspaceController unused = SmartSpaceComplication$Registrant.this.getClass();
                    BcSmartspaceDataPlugin.SmartspaceTargetListener unused2 = SmartSpaceComplication$Registrant.this.mSmartspaceListener;
                    throw null;
                }
                DreamsSmartspaceController unused3 = SmartSpaceComplication$Registrant.this.getClass();
                BcSmartspaceDataPlugin.SmartspaceTargetListener unused4 = SmartSpaceComplication$Registrant.this.mSmartspaceListener;
                throw null;
            }
        });
    }
}
