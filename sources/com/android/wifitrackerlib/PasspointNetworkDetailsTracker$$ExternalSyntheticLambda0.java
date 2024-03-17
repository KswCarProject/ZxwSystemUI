package com.android.wifitrackerlib;

import android.net.wifi.hotspot2.PasspointConfiguration;
import android.text.TextUtils;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PasspointNetworkDetailsTracker$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ PasspointNetworkDetailsTracker$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return TextUtils.equals(this.f$0, PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(((PasspointConfiguration) obj).getUniqueId()));
    }
}
