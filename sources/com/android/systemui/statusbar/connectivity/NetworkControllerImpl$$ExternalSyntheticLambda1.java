package com.android.systemui.statusbar.connectivity;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NetworkControllerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WifiSignalController f$0;

    public /* synthetic */ NetworkControllerImpl$$ExternalSyntheticLambda1(WifiSignalController wifiSignalController) {
        this.f$0 = wifiSignalController;
    }

    public final void run() {
        this.f$0.fetchInitialState();
    }
}
