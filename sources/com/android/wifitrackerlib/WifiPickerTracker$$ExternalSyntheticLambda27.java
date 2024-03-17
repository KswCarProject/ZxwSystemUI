package com.android.wifitrackerlib;

import java.util.Map;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda27 implements Consumer {
    public final /* synthetic */ WifiPickerTracker f$0;
    public final /* synthetic */ Map f$1;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda27(WifiPickerTracker wifiPickerTracker, Map map) {
        this.f$0 = wifiPickerTracker;
        this.f$1 = map;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$updateOsuWifiEntryScans$16(this.f$1, (OsuWifiEntry) obj);
    }
}
