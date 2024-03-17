package com.android.systemui.recents;

import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda19 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda19(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).showScreenPinningRequest(this.f$0, false);
    }
}
