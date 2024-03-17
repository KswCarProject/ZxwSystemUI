package com.android.settingslib.deviceinfo;

public abstract class AbstractImsStatusPreferenceController extends AbstractConnectivityPreferenceController {
    public static final String[] CONNECTIVITY_INTENTS = {"android.bluetooth.adapter.action.STATE_CHANGED", "android.net.conn.CONNECTIVITY_CHANGE", "android.net.wifi.LINK_CONFIGURATION_CHANGED", "android.net.wifi.STATE_CHANGE"};
    public static final String KEY_IMS_REGISTRATION_STATE = "ims_reg_state";

    public String[] getConnectivityIntents() {
        return CONNECTIVITY_INTENTS;
    }
}
