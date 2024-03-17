package com.android.wifitrackerlib;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.android.wifitrackerlib.StandardWifiEntry;
import com.android.wifitrackerlib.WifiEntry;

public class NetworkRequestEntry extends StandardWifiEntry {
    public boolean canConnect() {
        return false;
    }

    public boolean canSetAutoJoinEnabled() {
        return false;
    }

    public boolean canSetMeteredChoice() {
        return false;
    }

    public void connect(WifiEntry.ConnectCallback connectCallback) {
    }

    public int getMeteredChoice() {
        return 0;
    }

    public WifiConfiguration getWifiConfiguration() {
        return null;
    }

    public boolean isAutoJoinEnabled() {
        return true;
    }

    public boolean isMetered() {
        return false;
    }

    public boolean isSaved() {
        return false;
    }

    public boolean isSubscription() {
        return false;
    }

    public boolean isSuggestion() {
        return false;
    }

    public NetworkRequestEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        super(wifiTrackerInjector, context, handler, standardWifiEntryKey, wifiManager, z);
    }
}
