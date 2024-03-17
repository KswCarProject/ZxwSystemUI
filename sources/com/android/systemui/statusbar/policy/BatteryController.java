package com.android.systemui.statusbar.policy;

import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.demomode.DemoMode;
import java.lang.ref.WeakReference;

public interface BatteryController extends DemoMode, Dumpable, CallbackController<BatteryStateChangeCallback> {

    public interface BatteryStateChangeCallback {
        void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        }

        void onBatteryUnknownStateChanged(boolean z) {
        }

        void onPowerSaveChanged(boolean z) {
        }

        void onWirelessChargingChanged(boolean z) {
        }
    }

    public interface EstimateFetchCompletion {
        void onBatteryRemainingEstimateRetrieved(String str);
    }

    void clearLastPowerSaverStartView() {
    }

    void getEstimatedTimeRemainingString(EstimateFetchCompletion estimateFetchCompletion) {
    }

    WeakReference<View> getLastPowerSaverStartView() {
        return null;
    }

    void init() {
    }

    boolean isAodPowerSave();

    boolean isPluggedIn();

    boolean isPluggedInWireless() {
        return false;
    }

    boolean isPowerSave();

    boolean isWirelessCharging() {
        return false;
    }

    void setPowerSaveMode(boolean z, View view);
}
