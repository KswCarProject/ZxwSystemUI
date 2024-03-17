package com.android.wifitrackerlib;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda7(Map map) {
        this.f$0 = map;
    }

    public final void accept(Object obj) {
        ((StandardWifiEntry) obj).updateScanResultInfo((List) this.f$0.get(((StandardWifiEntry) obj).getStandardWifiEntryKey().getScanResultKey()));
    }
}
