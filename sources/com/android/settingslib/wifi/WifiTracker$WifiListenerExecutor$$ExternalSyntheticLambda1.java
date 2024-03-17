package com.android.settingslib.wifi;

import com.android.settingslib.wifi.WifiTracker;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WifiTracker.WifiListenerExecutor f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda1(WifiTracker.WifiListenerExecutor wifiListenerExecutor, int i) {
        this.f$0 = wifiListenerExecutor;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$onWifiStateChanged$0(this.f$1);
    }
}
