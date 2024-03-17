package com.android.settingslib.deviceinfo;

public abstract class AbstractIpAddressPreferenceController extends AbstractConnectivityPreferenceController {
    public static final String[] CONNECTIVITY_INTENTS = {"android.net.conn.CONNECTIVITY_CHANGE", "android.net.wifi.LINK_CONFIGURATION_CHANGED", "android.net.wifi.STATE_CHANGE"};
    public static final String KEY_IP_ADDRESS = "wifi_ip_address";

    public String[] getConnectivityIntents() {
        return CONNECTIVITY_INTENTS;
    }
}
