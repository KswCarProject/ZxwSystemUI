package com.android.systemui.statusbar.connectivity;

import android.content.Intent;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiSignalController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WifiSignalController f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ WifiSignalController$$ExternalSyntheticLambda0(WifiSignalController wifiSignalController, Intent intent) {
        this.f$0 = wifiSignalController;
        this.f$1 = intent;
    }

    public final void run() {
        this.f$0.lambda$handleBroadcast$1(this.f$1);
    }
}
