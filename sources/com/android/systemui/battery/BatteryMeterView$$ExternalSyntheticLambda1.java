package com.android.systemui.battery;

import com.android.systemui.statusbar.policy.BatteryController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BatteryMeterView$$ExternalSyntheticLambda1 implements BatteryController.EstimateFetchCompletion {
    public final /* synthetic */ BatteryMeterView f$0;

    public /* synthetic */ BatteryMeterView$$ExternalSyntheticLambda1(BatteryMeterView batteryMeterView) {
        this.f$0 = batteryMeterView;
    }

    public final void onBatteryRemainingEstimateRetrieved(String str) {
        this.f$0.lambda$updatePercentText$0(str);
    }
}
