package com.android.systemui.statusbar.charging;

import com.android.systemui.statusbar.policy.BatteryController;

/* compiled from: WiredChargingRippleController.kt */
public final class WiredChargingRippleController$registerCallbacks$batteryStateChangeCallback$1 implements BatteryController.BatteryStateChangeCallback {
    public final /* synthetic */ WiredChargingRippleController this$0;

    public WiredChargingRippleController$registerCallbacks$batteryStateChangeCallback$1(WiredChargingRippleController wiredChargingRippleController) {
        this.this$0 = wiredChargingRippleController;
    }

    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        if (!this.this$0.batteryController.isPluggedInWireless()) {
            Boolean access$getPluggedIn$p = this.this$0.pluggedIn;
            this.this$0.pluggedIn = Boolean.valueOf(z);
            if ((access$getPluggedIn$p == null || !access$getPluggedIn$p.booleanValue()) && z) {
                this.this$0.startRippleWithDebounce$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
            }
        }
    }
}
