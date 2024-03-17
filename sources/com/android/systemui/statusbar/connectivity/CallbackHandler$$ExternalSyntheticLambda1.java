package com.android.systemui.statusbar.connectivity;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CallbackHandler$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ CallbackHandler f$0;
    public final /* synthetic */ WifiIndicators f$1;

    public /* synthetic */ CallbackHandler$$ExternalSyntheticLambda1(CallbackHandler callbackHandler, WifiIndicators wifiIndicators) {
        this.f$0 = callbackHandler;
        this.f$1 = wifiIndicators;
    }

    public final void run() {
        this.f$0.lambda$setWifiIndicators$0(this.f$1);
    }
}
