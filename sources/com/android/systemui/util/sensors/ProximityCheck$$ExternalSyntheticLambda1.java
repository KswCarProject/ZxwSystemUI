package com.android.systemui.util.sensors;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ProximityCheck$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ ThresholdSensorEvent f$0;

    public /* synthetic */ ProximityCheck$$ExternalSyntheticLambda1(ThresholdSensorEvent thresholdSensorEvent) {
        this.f$0 = thresholdSensorEvent;
    }

    public final void accept(Object obj) {
        ProximityCheck.lambda$onProximityEvent$0(this.f$0, (Consumer) obj);
    }
}
