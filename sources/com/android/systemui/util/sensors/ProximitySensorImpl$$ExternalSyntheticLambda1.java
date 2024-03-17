package com.android.systemui.util.sensors;

import com.android.systemui.util.sensors.ThresholdSensor;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ProximitySensorImpl$$ExternalSyntheticLambda1 implements ThresholdSensor.Listener {
    public final /* synthetic */ ProximitySensorImpl f$0;

    public /* synthetic */ ProximitySensorImpl$$ExternalSyntheticLambda1(ProximitySensorImpl proximitySensorImpl) {
        this.f$0 = proximitySensorImpl;
    }

    public final void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent) {
        this.f$0.onPrimarySensorEvent(thresholdSensorEvent);
    }
}
