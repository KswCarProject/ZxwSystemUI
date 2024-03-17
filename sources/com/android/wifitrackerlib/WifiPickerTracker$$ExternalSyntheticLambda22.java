package com.android.wifitrackerlib;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda22 implements Consumer {
    public final /* synthetic */ Set f$0;
    public final /* synthetic */ Map f$1;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda22(Set set, Map map) {
        this.f$0 = set;
        this.f$1 = map;
    }

    public final void accept(Object obj) {
        WifiPickerTracker.lambda$updateStandardWifiEntryScans$10(this.f$0, this.f$1, (StandardWifiEntry) obj);
    }
}
