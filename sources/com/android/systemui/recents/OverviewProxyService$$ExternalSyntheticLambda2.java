package com.android.systemui.recents;

import com.android.systemui.statusbar.phone.StatusBarWindowCallback;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$$ExternalSyntheticLambda2 implements StatusBarWindowCallback {
    public final /* synthetic */ OverviewProxyService f$0;

    public /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda2(OverviewProxyService overviewProxyService) {
        this.f$0 = overviewProxyService;
    }

    public final void onStateChanged(boolean z, boolean z2, boolean z3, boolean z4) {
        this.f$0.onStatusBarStateChanged(z, z2, z3, z4);
    }
}
