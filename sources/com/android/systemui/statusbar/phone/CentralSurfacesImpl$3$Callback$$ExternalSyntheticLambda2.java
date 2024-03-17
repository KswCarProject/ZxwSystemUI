package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$3$Callback$$ExternalSyntheticLambda2 implements NotificationShadeWindowController.OtherwisedCollapsedListener {
    public final /* synthetic */ CentralSurfacesImpl.AnonymousClass3.Callback f$0;

    public /* synthetic */ CentralSurfacesImpl$3$Callback$$ExternalSyntheticLambda2(CentralSurfacesImpl.AnonymousClass3.Callback callback) {
        this.f$0 = callback;
    }

    public final void setWouldOtherwiseCollapse(boolean z) {
        this.f$0.lambda$onHoldStatusBarOpenChange$1(z);
    }
}
