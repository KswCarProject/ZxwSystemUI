package com.android.systemui.util.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AsyncSensorManager$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AsyncSensorManager f$0;
    public final /* synthetic */ Sensor f$1;
    public final /* synthetic */ SensorEventListener f$2;

    public /* synthetic */ AsyncSensorManager$$ExternalSyntheticLambda1(AsyncSensorManager asyncSensorManager, Sensor sensor, SensorEventListener sensorEventListener) {
        this.f$0 = asyncSensorManager;
        this.f$1 = sensor;
        this.f$2 = sensorEventListener;
    }

    public final void run() {
        this.f$0.lambda$unregisterListenerImpl$8(this.f$1, this.f$2);
    }
}
