package com.android.systemui.sensorprivacy;

/* compiled from: SensorUseStartedActivity.kt */
public final class SensorUseStartedActivity$onStop$1 implements Runnable {
    public final /* synthetic */ SensorUseStartedActivity this$0;

    public SensorUseStartedActivity$onStop$1(SensorUseStartedActivity sensorUseStartedActivity) {
        this.this$0 = sensorUseStartedActivity;
    }

    public final void run() {
        this.this$0.setSuppressed(false);
    }
}
