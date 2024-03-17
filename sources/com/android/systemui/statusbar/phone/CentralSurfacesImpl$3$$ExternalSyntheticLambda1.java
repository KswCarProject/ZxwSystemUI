package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.OverlayPlugin;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$3$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ CentralSurfacesImpl.AnonymousClass3 f$0;
    public final /* synthetic */ OverlayPlugin f$1;

    public /* synthetic */ CentralSurfacesImpl$3$$ExternalSyntheticLambda1(CentralSurfacesImpl.AnonymousClass3 r1, OverlayPlugin overlayPlugin) {
        this.f$0 = r1;
        this.f$1 = overlayPlugin;
    }

    public final void run() {
        this.f$0.lambda$onPluginConnected$0(this.f$1);
    }
}
