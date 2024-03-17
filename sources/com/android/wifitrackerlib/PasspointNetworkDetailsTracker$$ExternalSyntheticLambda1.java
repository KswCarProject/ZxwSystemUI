package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PasspointNetworkDetailsTracker$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ PasspointNetworkDetailsTracker$$ExternalSyntheticLambda1(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return PasspointNetworkDetailsTracker.lambda$new$1(this.f$0, (WifiConfiguration) obj);
    }
}
