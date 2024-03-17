package com.android.systemui.statusbar.connectivity;

import com.android.settingslib.net.DataUsageController;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.DataSaverController;

public interface NetworkController extends CallbackController<SignalCallback>, DemoMode {

    public interface EmergencyListener {
        void setEmergencyCallsOnly(boolean z);
    }

    DataSaverController getDataSaverController();

    DataUsageController getMobileDataController();

    String getMobileDataNetworkName();

    int getNumberSubscriptions();

    boolean hasEmergencyCryptKeeperText();

    boolean hasMobileDataFeature();

    boolean hasVoiceCallingFeature();

    boolean isMobileDataNetworkInService();

    boolean isRadioOn();

    void setWifiEnabled(boolean z);
}
