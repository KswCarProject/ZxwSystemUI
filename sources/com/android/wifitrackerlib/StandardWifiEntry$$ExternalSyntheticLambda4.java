package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiEntry;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ WifiEntry.ConnectCallback f$0;

    public /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda4(WifiEntry.ConnectCallback connectCallback) {
        this.f$0 = connectCallback;
    }

    public final void run() {
        this.f$0.onConnectResult(3);
    }
}
