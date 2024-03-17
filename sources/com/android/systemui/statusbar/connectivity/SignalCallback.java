package com.android.systemui.statusbar.connectivity;

import android.telephony.SubscriptionInfo;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: SignalCallback.kt */
public interface SignalCallback {
    void setCallIndicator(@NotNull IconState iconState, int i) {
    }

    void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
    }

    void setEthernetIndicators(@NotNull IconState iconState) {
    }

    void setIsAirplaneMode(@NotNull IconState iconState) {
    }

    void setMobileDataEnabled(boolean z) {
    }

    void setMobileDataIndicators(@NotNull MobileDataIndicators mobileDataIndicators) {
    }

    void setNoSims(boolean z, boolean z2) {
    }

    void setSubs(@NotNull List<SubscriptionInfo> list) {
    }

    void setWifiIndicators(@NotNull WifiIndicators wifiIndicators) {
    }
}
