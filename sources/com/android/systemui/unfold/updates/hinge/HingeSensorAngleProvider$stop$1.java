package com.android.systemui.unfold.updates.hinge;

/* compiled from: HingeSensorAngleProvider.kt */
public final class HingeSensorAngleProvider$stop$1 implements Runnable {
    public final /* synthetic */ HingeSensorAngleProvider this$0;

    public HingeSensorAngleProvider$stop$1(HingeSensorAngleProvider hingeSensorAngleProvider) {
        this.this$0 = hingeSensorAngleProvider;
    }

    public final void run() {
        this.this$0.sensorManager.unregisterListener(this.this$0.sensorListener);
    }
}
