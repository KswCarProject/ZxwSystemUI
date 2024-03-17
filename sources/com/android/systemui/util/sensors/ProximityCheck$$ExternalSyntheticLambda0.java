package com.android.systemui.util.sensors;

import com.android.systemui.util.sensors.ThresholdSensor;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ProximityCheck$$ExternalSyntheticLambda0 implements ThresholdSensor.Listener {
    public final /* synthetic */ ProximityCheck f$0;

    public /* synthetic */ ProximityCheck$$ExternalSyntheticLambda0(ProximityCheck proximityCheck) {
        this.f$0 = proximityCheck;
    }

    public final void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent) {
        this.f$0.onProximityEvent(thresholdSensorEvent);
    }
}
