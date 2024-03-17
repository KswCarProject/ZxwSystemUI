package com.android.systemui.statusbar.policy;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BatteryControllerImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ BatteryControllerImpl f$0;

    public /* synthetic */ BatteryControllerImpl$$ExternalSyntheticLambda2(BatteryControllerImpl batteryControllerImpl) {
        this.f$0 = batteryControllerImpl;
    }

    public final void run() {
        this.f$0.notifyEstimateFetchCallbacks();
    }
}
