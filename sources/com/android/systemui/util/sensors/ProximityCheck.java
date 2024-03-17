package com.android.systemui.util.sensors;

import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ThresholdSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class ProximityCheck implements Runnable {
    public List<Consumer<Boolean>> mCallbacks = new ArrayList();
    public final DelayableExecutor mDelayableExecutor;
    public final ThresholdSensor.Listener mListener;
    public final AtomicBoolean mRegistered = new AtomicBoolean();
    public final ProximitySensor mSensor;

    public ProximityCheck(ProximitySensor proximitySensor, DelayableExecutor delayableExecutor) {
        this.mSensor = proximitySensor;
        proximitySensor.setTag("prox_check");
        this.mDelayableExecutor = delayableExecutor;
        this.mListener = new ProximityCheck$$ExternalSyntheticLambda0(this);
    }

    public void run() {
        unregister();
        onProximityEvent((ThresholdSensorEvent) null);
    }

    public void check(long j, Consumer<Boolean> consumer) {
        if (!this.mSensor.isLoaded()) {
            consumer.accept((Object) null);
            return;
        }
        this.mCallbacks.add(consumer);
        if (!this.mRegistered.getAndSet(true)) {
            this.mSensor.register(this.mListener);
            this.mDelayableExecutor.executeDelayed(this, j);
        }
    }

    public void destroy() {
        this.mSensor.destroy();
    }

    public final void unregister() {
        this.mSensor.unregister(this.mListener);
        this.mRegistered.set(false);
    }

    public final void onProximityEvent(ThresholdSensorEvent thresholdSensorEvent) {
        this.mCallbacks.forEach(new ProximityCheck$$ExternalSyntheticLambda1(thresholdSensorEvent));
        this.mCallbacks.clear();
        unregister();
        this.mRegistered.set(false);
    }

    public static /* synthetic */ void lambda$onProximityEvent$0(ThresholdSensorEvent thresholdSensorEvent, Consumer consumer) {
        consumer.accept(thresholdSensorEvent == null ? null : Boolean.valueOf(thresholdSensorEvent.getBelow()));
    }
}
