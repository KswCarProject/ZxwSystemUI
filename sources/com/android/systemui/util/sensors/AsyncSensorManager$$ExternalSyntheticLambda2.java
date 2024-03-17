package com.android.systemui.util.sensors;

import android.hardware.SensorAdditionalInfo;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AsyncSensorManager$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ AsyncSensorManager f$0;
    public final /* synthetic */ SensorAdditionalInfo f$1;

    public /* synthetic */ AsyncSensorManager$$ExternalSyntheticLambda2(AsyncSensorManager asyncSensorManager, SensorAdditionalInfo sensorAdditionalInfo) {
        this.f$0 = asyncSensorManager;
        this.f$1 = sensorAdditionalInfo;
    }

    public final void run() {
        this.f$0.lambda$setOperationParameterImpl$7(this.f$1);
    }
}
