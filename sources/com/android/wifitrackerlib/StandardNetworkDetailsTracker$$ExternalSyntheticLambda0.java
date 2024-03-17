package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StandardNetworkDetailsTracker$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ StandardNetworkDetailsTracker f$0;

    public /* synthetic */ StandardNetworkDetailsTracker$$ExternalSyntheticLambda0(StandardNetworkDetailsTracker standardNetworkDetailsTracker) {
        this.f$0 = standardNetworkDetailsTracker;
    }

    public final boolean test(Object obj) {
        return this.f$0.configMatches((WifiConfiguration) obj);
    }
}
