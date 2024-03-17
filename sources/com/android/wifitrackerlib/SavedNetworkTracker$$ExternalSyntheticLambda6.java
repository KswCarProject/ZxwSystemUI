package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda6 implements Function {
    public final Object apply(Object obj) {
        return new StandardWifiEntry.ScanResultKey((ScanResult) obj);
    }
}
