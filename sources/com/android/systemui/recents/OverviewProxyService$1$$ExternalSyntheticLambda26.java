package com.android.systemui.recents;

import android.view.MotionEvent;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.phone.CentralSurfaces;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda26 implements Runnable {
    public final /* synthetic */ OverviewProxyService.AnonymousClass1 f$0;
    public final /* synthetic */ MotionEvent f$1;
    public final /* synthetic */ CentralSurfaces f$2;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda26(OverviewProxyService.AnonymousClass1 r1, MotionEvent motionEvent, CentralSurfaces centralSurfaces) {
        this.f$0 = r1;
        this.f$1 = motionEvent;
        this.f$2 = centralSurfaces;
    }

    public final void run() {
        this.f$0.lambda$onStatusBarMotionEvent$3(this.f$1, this.f$2);
    }
}
