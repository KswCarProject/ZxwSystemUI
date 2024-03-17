package com.android.systemui.unfold.updates.hinge;

import android.os.Trace;

/* compiled from: HingeSensorAngleProvider.kt */
public final class HingeSensorAngleProvider$start$1 implements Runnable {
    public final /* synthetic */ HingeSensorAngleProvider this$0;

    public HingeSensorAngleProvider$start$1(HingeSensorAngleProvider hingeSensorAngleProvider) {
        this.this$0 = hingeSensorAngleProvider;
    }

    public final void run() {
        Trace.beginSection("HingeSensorAngleProvider#start");
        this.this$0.sensorManager.registerListener(this.this$0.sensorListener, this.this$0.sensorManager.getDefaultSensor(36), 0);
        Trace.endSection();
    }
}
