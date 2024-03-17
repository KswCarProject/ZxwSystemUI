package com.android.wifitrackerlib;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda25 implements Consumer {
    public final /* synthetic */ Set f$0;
    public final /* synthetic */ Map f$1;
    public final /* synthetic */ Set f$2;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda25(Set set, Map map, Set set2) {
        this.f$0 = set;
        this.f$1 = map;
        this.f$2 = set2;
    }

    public final void accept(Object obj) {
        WifiPickerTracker.lambda$updateSuggestedWifiEntryScans$13(this.f$0, this.f$1, this.f$2, (StandardWifiEntry) obj);
    }
}
