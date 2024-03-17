package com.android.systemui.recents;

import com.android.systemui.model.SysUiState;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$$ExternalSyntheticLambda5 implements SysUiState.SysUiStateCallback {
    public final /* synthetic */ OverviewProxyService f$0;

    public /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda5(OverviewProxyService overviewProxyService) {
        this.f$0 = overviewProxyService;
    }

    public final void onSystemUiStateChanged(int i) {
        this.f$0.notifySystemUiStateFlags(i);
    }
}
