package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.OverlayPlugin;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$3$Callback$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ CentralSurfacesImpl$3$Callback$$ExternalSyntheticLambda0(boolean z) {
        this.f$0 = z;
    }

    public final void accept(Object obj) {
        ((OverlayPlugin) obj).setCollapseDesired(this.f$0);
    }
}
