package com.android.settingslib.wifi;

import androidx.annotation.Keep;

public class WifiTrackerFactory {
    public static WifiTracker sTestingWifiTracker;

    @Keep
    public static void setTestingWifiTracker(WifiTracker wifiTracker) {
        sTestingWifiTracker = wifiTracker;
    }
}
