package com.android.systemui.doze;

import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorEvent;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DozeSensors$$ExternalSyntheticLambda1 implements ThresholdSensor.Listener {
    public final /* synthetic */ DozeSensors f$0;

    public /* synthetic */ DozeSensors$$ExternalSyntheticLambda1(DozeSensors dozeSensors) {
        this.f$0 = dozeSensors;
    }

    public final void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent) {
        this.f$0.lambda$new$0(thresholdSensorEvent);
    }
}
