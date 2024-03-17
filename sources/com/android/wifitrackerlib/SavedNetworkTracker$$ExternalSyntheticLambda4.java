package com.android.wifitrackerlib;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda4(Map map) {
        this.f$0 = map;
    }

    public final boolean test(Object obj) {
        return ((StandardWifiEntry) obj).updateConfig((List) this.f$0.remove(((StandardWifiEntry) obj).getStandardWifiEntryKey()));
    }
}
