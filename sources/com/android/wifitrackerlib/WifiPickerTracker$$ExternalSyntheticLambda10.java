package com.android.wifitrackerlib;

import android.net.wifi.hotspot2.PasspointConfiguration;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda10 implements Function {
    public final Object apply(Object obj) {
        return PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(((PasspointConfiguration) obj).getUniqueId());
    }
}
