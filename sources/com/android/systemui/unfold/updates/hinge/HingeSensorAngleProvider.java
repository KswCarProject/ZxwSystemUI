package com.android.systemui.unfold.updates.hinge;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.core.util.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HingeSensorAngleProvider.kt */
public final class HingeSensorAngleProvider implements HingeAngleProvider {
    @NotNull
    public final Executor executor;
    @NotNull
    public final List<Consumer<Float>> listeners = new ArrayList();
    @NotNull
    public final HingeAngleSensorListener sensorListener = new HingeAngleSensorListener();
    @NotNull
    public final SensorManager sensorManager;

    public HingeSensorAngleProvider(@NotNull SensorManager sensorManager2, @NotNull Executor executor2) {
        this.sensorManager = sensorManager2;
        this.executor = executor2;
    }

    public void start() {
        this.executor.execute(new HingeSensorAngleProvider$start$1(this));
    }

    public void stop() {
        this.executor.execute(new HingeSensorAngleProvider$stop$1(this));
    }

    public void removeCallback(@NotNull Consumer<Float> consumer) {
        this.listeners.remove(consumer);
    }

    public void addCallback(@NotNull Consumer<Float> consumer) {
        this.listeners.add(consumer);
    }

    /* compiled from: HingeSensorAngleProvider.kt */
    public final class HingeAngleSensorListener implements SensorEventListener {
        public void onAccuracyChanged(@Nullable Sensor sensor, int i) {
        }

        public HingeAngleSensorListener() {
        }

        public void onSensorChanged(@NotNull SensorEvent sensorEvent) {
            for (Consumer accept : HingeSensorAngleProvider.this.listeners) {
                accept.accept(Float.valueOf(sensorEvent.values[0]));
            }
        }
    }
}
