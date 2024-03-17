package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.util.concurrency.MessageRouter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$$ExternalSyntheticLambda16 implements MessageRouter.DataMessageListener {
    public final /* synthetic */ CentralSurfacesImpl f$0;

    public /* synthetic */ CentralSurfacesImpl$$ExternalSyntheticLambda16(CentralSurfacesImpl centralSurfacesImpl) {
        this.f$0 = centralSurfacesImpl;
    }

    public final void onMessage(Object obj) {
        this.f$0.lambda$new$4((CentralSurfaces.KeyboardShortcutsMessage) obj);
    }
}
