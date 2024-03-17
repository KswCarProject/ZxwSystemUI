package com.android.settingslib.wifi;

import android.net.wifi.WifiConfiguration;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiTracker$$ExternalSyntheticLambda1 implements Predicate {
    public final boolean test(Object obj) {
        return WifiTracker.isSaeOrOwe((WifiConfiguration) obj);
    }
}
