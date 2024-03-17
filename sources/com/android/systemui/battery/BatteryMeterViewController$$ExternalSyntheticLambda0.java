package com.android.systemui.battery;

import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.statusbar.policy.BatteryController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BatteryMeterViewController$$ExternalSyntheticLambda0 implements BatteryMeterView.BatteryEstimateFetcher {
    public final /* synthetic */ BatteryController f$0;

    public /* synthetic */ BatteryMeterViewController$$ExternalSyntheticLambda0(BatteryController batteryController) {
        this.f$0 = batteryController;
    }

    public final void fetchBatteryTimeRemainingEstimate(BatteryController.EstimateFetchCompletion estimateFetchCompletion) {
        this.f$0.getEstimatedTimeRemainingString(estimateFetchCompletion);
    }
}
