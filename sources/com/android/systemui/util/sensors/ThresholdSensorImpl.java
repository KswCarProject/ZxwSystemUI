package com.android.systemui.util.sensors;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ThresholdSensor;
import java.util.ArrayList;
import java.util.List;

public class ThresholdSensorImpl implements ThresholdSensor {
    public static final boolean DEBUG = Log.isLoggable("ThresholdSensor", 3);
    public final Execution mExecution;
    public Boolean mLastBelow;
    public List<ThresholdSensor.Listener> mListeners;
    public boolean mPaused;
    public boolean mRegistered;
    public final Sensor mSensor;
    public int mSensorDelay;
    public SensorEventListener mSensorEventListener;
    public final AsyncSensorManager mSensorManager;
    public String mTag;
    public final float mThreshold;
    public final float mThresholdLatch;

    public ThresholdSensorImpl(AsyncSensorManager asyncSensorManager, Sensor sensor, Execution execution, float f, float f2, int i) {
        this.mListeners = new ArrayList();
        this.mSensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                boolean z = true;
                boolean z2 = sensorEvent.values[0] < ThresholdSensorImpl.this.mThreshold;
                if (sensorEvent.values[0] < ThresholdSensorImpl.this.mThresholdLatch) {
                    z = false;
                }
                ThresholdSensorImpl.this.logDebug("Sensor value: " + sensorEvent.values[0]);
                ThresholdSensorImpl.this.onSensorEvent(z2, z, sensorEvent.timestamp);
            }
        };
        this.mSensorManager = asyncSensorManager;
        this.mExecution = execution;
        this.mSensor = sensor;
        this.mThreshold = f;
        this.mThresholdLatch = f2;
        this.mSensorDelay = i;
    }

    public void setTag(String str) {
        this.mTag = str;
    }

    public void setDelay(int i) {
        if (i != this.mSensorDelay) {
            this.mSensorDelay = i;
            if (isLoaded()) {
                unregisterInternal();
                registerInternal();
            }
        }
    }

    public boolean isLoaded() {
        return this.mSensor != null;
    }

    @VisibleForTesting
    public boolean isRegistered() {
        return this.mRegistered;
    }

    public void register(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
        registerInternal();
    }

    public void unregister(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        this.mListeners.remove(listener);
        unregisterInternal();
    }

    public void pause() {
        this.mExecution.assertIsMainThread();
        this.mPaused = true;
        unregisterInternal();
    }

    public void resume() {
        this.mExecution.assertIsMainThread();
        this.mPaused = false;
        registerInternal();
    }

    public final void alertListenersInternal(boolean z, long j) {
        new ArrayList(this.mListeners).forEach(new ThresholdSensorImpl$$ExternalSyntheticLambda0(z, j));
    }

    public final void registerInternal() {
        this.mExecution.assertIsMainThread();
        if (!this.mRegistered && !this.mPaused && !this.mListeners.isEmpty()) {
            logDebug("Registering sensor listener");
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor, this.mSensorDelay);
            this.mRegistered = true;
        }
    }

    public final void unregisterInternal() {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered) {
            logDebug("Unregister sensor listener");
            this.mSensorManager.unregisterListener(this.mSensorEventListener);
            this.mRegistered = false;
            this.mLastBelow = null;
        }
    }

    public final void onSensorEvent(boolean z, boolean z2, long j) {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered) {
            Boolean bool = this.mLastBelow;
            if (bool != null) {
                if (bool.booleanValue() && !z2) {
                    return;
                }
                if (!this.mLastBelow.booleanValue() && !z) {
                    return;
                }
            }
            this.mLastBelow = Boolean.valueOf(z);
            logDebug("Alerting below: " + z);
            alertListenersInternal(z, j);
        }
    }

    public String toString() {
        return String.format("{isLoaded=%s, registered=%s, paused=%s, threshold=%s, sensor=%s}", new Object[]{Boolean.valueOf(isLoaded()), Boolean.valueOf(this.mRegistered), Boolean.valueOf(this.mPaused), Float.valueOf(this.mThreshold), this.mSensor});
    }

    public final void logDebug(String str) {
        String str2;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            if (this.mTag != null) {
                str2 = "[" + this.mTag + "] ";
            } else {
                str2 = "";
            }
            sb.append(str2);
            sb.append(str);
            Log.d("ThresholdSensor", sb.toString());
        }
    }

    public static class Builder {
        public final Execution mExecution;
        public final Resources mResources;
        public Sensor mSensor;
        public int mSensorDelay = 3;
        public final AsyncSensorManager mSensorManager;
        public boolean mSensorSet;
        public float mThresholdLatchValue;
        public boolean mThresholdLatchValueSet;
        public boolean mThresholdSet;
        public float mThresholdValue;

        public Builder(Resources resources, AsyncSensorManager asyncSensorManager, Execution execution) {
            this.mResources = resources;
            this.mSensorManager = asyncSensorManager;
            this.mExecution = execution;
        }

        public Builder setSensorDelay(int i) {
            this.mSensorDelay = i;
            return this;
        }

        public Builder setSensorResourceId(int i, boolean z) {
            setSensorType(this.mResources.getString(i), z);
            return this;
        }

        public Builder setThresholdResourceId(int i) {
            try {
                setThresholdValue(this.mResources.getFloat(i));
            } catch (Resources.NotFoundException unused) {
            }
            return this;
        }

        public Builder setThresholdLatchResourceId(int i) {
            try {
                setThresholdLatchValue(this.mResources.getFloat(i));
            } catch (Resources.NotFoundException unused) {
            }
            return this;
        }

        public Builder setSensorType(String str, boolean z) {
            Sensor findSensorByType = findSensorByType(str, z);
            if (findSensorByType != null) {
                setSensor(findSensorByType);
            }
            return this;
        }

        public Builder setThresholdValue(float f) {
            this.mThresholdValue = f;
            this.mThresholdSet = true;
            if (!this.mThresholdLatchValueSet) {
                this.mThresholdLatchValue = f;
            }
            return this;
        }

        public Builder setThresholdLatchValue(float f) {
            this.mThresholdLatchValue = f;
            this.mThresholdLatchValueSet = true;
            return this;
        }

        public Builder setSensor(Sensor sensor) {
            this.mSensor = sensor;
            this.mSensorSet = true;
            return this;
        }

        public ThresholdSensor build() {
            if (!this.mSensorSet) {
                throw new IllegalStateException("A sensor was not successfully set.");
            } else if (!this.mThresholdSet) {
                throw new IllegalStateException("A threshold was not successfully set.");
            } else if (this.mThresholdValue <= this.mThresholdLatchValue) {
                return new ThresholdSensorImpl(this.mSensorManager, this.mSensor, this.mExecution, this.mThresholdValue, this.mThresholdLatchValue, this.mSensorDelay);
            } else {
                throw new IllegalStateException("Threshold must be less than or equal to Threshold Latch");
            }
        }

        @VisibleForTesting
        public Sensor findSensorByType(String str, boolean z) {
            Sensor sensor = null;
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            for (Sensor next : this.mSensorManager.getSensorList(-1)) {
                if (str.equals(next.getStringType())) {
                    if (!z || next.isWakeUpSensor()) {
                        return next;
                    }
                    sensor = next;
                }
            }
            return sensor;
        }
    }

    public static class BuilderFactory {
        public final Execution mExecution;
        public final Resources mResources;
        public final AsyncSensorManager mSensorManager;

        public BuilderFactory(Resources resources, AsyncSensorManager asyncSensorManager, Execution execution) {
            this.mResources = resources;
            this.mSensorManager = asyncSensorManager;
            this.mExecution = execution;
        }

        public Builder createBuilder() {
            return new Builder(this.mResources, this.mSensorManager, this.mExecution);
        }
    }
}
