package com.android.systemui.statusbar.events;

import com.android.systemui.statusbar.policy.BatteryController;

/* compiled from: SystemEventCoordinator.kt */
public final class SystemEventCoordinator$batteryStateListener$1 implements BatteryController.BatteryStateChangeCallback {
    public boolean plugged;
    public boolean stateKnown;
    public final /* synthetic */ SystemEventCoordinator this$0;

    public SystemEventCoordinator$batteryStateListener$1(SystemEventCoordinator systemEventCoordinator) {
        this.this$0 = systemEventCoordinator;
    }

    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        if (!this.stateKnown) {
            this.stateKnown = true;
            this.plugged = z;
            notifyListeners();
        } else if (this.plugged != z) {
            this.plugged = z;
            notifyListeners();
        }
    }

    public final void notifyListeners() {
        if (this.plugged) {
            this.this$0.notifyPluggedIn();
        }
    }
}
