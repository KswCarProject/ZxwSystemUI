package com.android.systemui.util.sensors;

import android.hardware.Sensor;
import android.hardware.TriggerEventListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AsyncSensorManager$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ AsyncSensorManager f$0;
    public final /* synthetic */ Sensor f$1;
    public final /* synthetic */ TriggerEventListener f$2;

    public /* synthetic */ AsyncSensorManager$$ExternalSyntheticLambda5(AsyncSensorManager asyncSensorManager, Sensor sensor, TriggerEventListener triggerEventListener) {
        this.f$0 = asyncSensorManager;
        this.f$1 = sensor;
        this.f$2 = triggerEventListener;
    }

    public final void run() {
        this.f$0.lambda$requestTriggerSensorImpl$3(this.f$1, this.f$2);
    }
}
