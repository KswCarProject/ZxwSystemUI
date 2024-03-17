package com.android.systemui.recents;

import android.view.MotionEvent;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda23 implements Consumer {
    public final /* synthetic */ OverviewProxyService.AnonymousClass1 f$0;
    public final /* synthetic */ MotionEvent f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda23(OverviewProxyService.AnonymousClass1 r1, MotionEvent motionEvent) {
        this.f$0 = r1;
        this.f$1 = motionEvent;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onStatusBarMotionEvent$4(this.f$1, (CentralSurfaces) obj);
    }
}
