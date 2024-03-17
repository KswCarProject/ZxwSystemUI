package com.android.systemui.recents;

import com.android.systemui.recents.OverviewProxyService;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ OverviewProxyService.AnonymousClass1 f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda0(OverviewProxyService.AnonymousClass1 r1, boolean z, boolean z2) {
        this.f$0 = r1;
        this.f$1 = z;
        this.f$2 = z2;
    }

    public final void run() {
        this.f$0.lambda$notifyTaskbarStatus$9(this.f$1, this.f$2);
    }
}
