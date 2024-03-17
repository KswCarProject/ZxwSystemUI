package com.android.systemui.statusbar.connectivity;

import android.telephony.ServiceState;
import android.telephony.SignalStrength;

/* compiled from: MobileState.kt */
public final class MobileStateKt {
    public static final String minLog(ServiceState serviceState) {
        return "serviceState={state=" + serviceState.getState() + ",isEmergencyOnly=" + serviceState.isEmergencyOnly() + ",roaming=" + serviceState.getRoaming() + ",operatorNameAlphaShort=" + serviceState.getOperatorAlphaShort() + '}';
    }

    public static final String minLog(SignalStrength signalStrength) {
        return "signalStrength={isGsm=" + signalStrength.isGsm() + ",level=" + signalStrength.getLevel() + '}';
    }
}
