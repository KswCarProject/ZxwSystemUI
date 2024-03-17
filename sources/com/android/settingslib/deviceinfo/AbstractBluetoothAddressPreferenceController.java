package com.android.settingslib.deviceinfo;

public abstract class AbstractBluetoothAddressPreferenceController extends AbstractConnectivityPreferenceController {
    public static final String[] CONNECTIVITY_INTENTS = {"android.bluetooth.adapter.action.STATE_CHANGED"};
    public static final String KEY_BT_ADDRESS = "bt_address";

    public String[] getConnectivityIntents() {
        return CONNECTIVITY_INTENTS;
    }
}
