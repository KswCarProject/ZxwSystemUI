package com.android.systemui.statusbar.connectivity;

import android.telephony.TelephonyCallback;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NetworkControllerImpl$$ExternalSyntheticLambda8 implements TelephonyCallback.ActiveDataSubscriptionIdListener {
    public final /* synthetic */ NetworkControllerImpl f$0;

    public /* synthetic */ NetworkControllerImpl$$ExternalSyntheticLambda8(NetworkControllerImpl networkControllerImpl) {
        this.f$0 = networkControllerImpl;
    }

    public final void onActiveDataSubscriptionIdChanged(int i) {
        this.f$0.lambda$new$1(i);
    }
}
