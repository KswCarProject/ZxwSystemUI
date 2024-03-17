package com.android.systemui.doze;

import android.hardware.Sensor;
import android.hardware.TriggerEvent;
import com.android.systemui.doze.DozeSensors;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DozeSensors$TriggerSensor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DozeSensors.TriggerSensor f$0;
    public final /* synthetic */ TriggerEvent f$1;
    public final /* synthetic */ Sensor f$2;

    public /* synthetic */ DozeSensors$TriggerSensor$$ExternalSyntheticLambda0(DozeSensors.TriggerSensor triggerSensor, TriggerEvent triggerEvent, Sensor sensor) {
        this.f$0 = triggerSensor;
        this.f$1 = triggerEvent;
        this.f$2 = sensor;
    }

    public final void run() {
        this.f$0.lambda$onTrigger$0(this.f$1, this.f$2);
    }
}
