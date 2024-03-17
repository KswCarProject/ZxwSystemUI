package com.android.systemui.util.sensors;

import com.android.systemui.util.sensors.ThresholdSensor;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ProximitySensorImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ ThresholdSensorEvent f$0;

    public /* synthetic */ ProximitySensorImpl$$ExternalSyntheticLambda0(ThresholdSensorEvent thresholdSensorEvent) {
        this.f$0 = thresholdSensorEvent;
    }

    public final void accept(Object obj) {
        ((ThresholdSensor.Listener) obj).onThresholdCrossed(this.f$0);
    }
}
