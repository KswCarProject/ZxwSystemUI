package com.android.systemui.util.sensors;

import android.os.Build;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ThresholdSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProximitySensorImpl implements ProximitySensor {
    public static final boolean DEBUG = (Log.isLoggable("ProxSensor", 3) || Build.IS_DEBUGGABLE);
    public final AtomicBoolean mAlerting = new AtomicBoolean();
    public Runnable mCancelSecondaryRunnable;
    public final DelayableExecutor mDelayableExecutor;
    public int mDevicePosture;
    public final Execution mExecution;
    public boolean mInitializedListeners = false;
    @VisibleForTesting
    public ThresholdSensorEvent mLastEvent;
    public ThresholdSensorEvent mLastPrimaryEvent;
    public final List<ThresholdSensor.Listener> mListeners = new ArrayList();
    @VisibleForTesting
    public boolean mPaused;
    public final ThresholdSensor.Listener mPrimaryEventListener = new ProximitySensorImpl$$ExternalSyntheticLambda1(this);
    public ThresholdSensor mPrimaryThresholdSensor;
    public boolean mRegistered;
    public final ThresholdSensor.Listener mSecondaryEventListener = new ThresholdSensor.Listener() {
        public void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent) {
            if (!ProximitySensorImpl.this.mSecondarySafe && (ProximitySensorImpl.this.mLastPrimaryEvent == null || !ProximitySensorImpl.this.mLastPrimaryEvent.getBelow() || !thresholdSensorEvent.getBelow())) {
                ProximitySensorImpl.this.chooseSensor();
                if (ProximitySensorImpl.this.mLastPrimaryEvent != null && ProximitySensorImpl.this.mLastPrimaryEvent.getBelow()) {
                    ProximitySensorImpl proximitySensorImpl = ProximitySensorImpl.this;
                    proximitySensorImpl.mCancelSecondaryRunnable = proximitySensorImpl.mDelayableExecutor.executeDelayed(new ProximitySensorImpl$1$$ExternalSyntheticLambda0(this), 5000);
                } else if (ProximitySensorImpl.this.mCancelSecondaryRunnable != null) {
                    ProximitySensorImpl.this.mCancelSecondaryRunnable.run();
                    ProximitySensorImpl.this.mCancelSecondaryRunnable = null;
                    return;
                } else {
                    return;
                }
            }
            ProximitySensorImpl proximitySensorImpl2 = ProximitySensorImpl.this;
            proximitySensorImpl2.logDebug("Secondary sensor event: " + thresholdSensorEvent.getBelow() + ".");
            ProximitySensorImpl proximitySensorImpl3 = ProximitySensorImpl.this;
            if (!proximitySensorImpl3.mPaused) {
                proximitySensorImpl3.onSensorEvent(thresholdSensorEvent);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onThresholdCrossed$0() {
            ProximitySensorImpl.this.mPrimaryThresholdSensor.pause();
            ProximitySensorImpl.this.mSecondaryThresholdSensor.resume();
        }
    };
    public boolean mSecondarySafe = false;
    public ThresholdSensor mSecondaryThresholdSensor;
    public String mTag = null;

    public ProximitySensorImpl(ThresholdSensor thresholdSensor, ThresholdSensor thresholdSensor2, DelayableExecutor delayableExecutor, Execution execution) {
        this.mPrimaryThresholdSensor = thresholdSensor;
        this.mSecondaryThresholdSensor = thresholdSensor2;
        this.mDelayableExecutor = delayableExecutor;
        this.mExecution = execution;
    }

    public void setTag(String str) {
        this.mTag = str;
        ThresholdSensor thresholdSensor = this.mPrimaryThresholdSensor;
        thresholdSensor.setTag(str + ":primary");
        ThresholdSensor thresholdSensor2 = this.mSecondaryThresholdSensor;
        thresholdSensor2.setTag(str + ":secondary");
    }

    public void setDelay(int i) {
        this.mExecution.assertIsMainThread();
        this.mPrimaryThresholdSensor.setDelay(i);
        this.mSecondaryThresholdSensor.setDelay(i);
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

    public void setSecondarySafe(boolean z) {
        this.mSecondarySafe = this.mSecondaryThresholdSensor.isLoaded() && z;
        chooseSensor();
    }

    public boolean isRegistered() {
        return this.mRegistered;
    }

    public boolean isLoaded() {
        return this.mPrimaryThresholdSensor.isLoaded();
    }

    public void register(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        if (isLoaded()) {
            if (this.mListeners.contains(listener)) {
                logDebug("ProxListener registered multiple times: " + listener);
            } else {
                this.mListeners.add(listener);
            }
            registerInternal();
        }
    }

    public void registerInternal() {
        this.mExecution.assertIsMainThread();
        if (!this.mRegistered && !this.mPaused && !this.mListeners.isEmpty()) {
            if (!this.mInitializedListeners) {
                this.mPrimaryThresholdSensor.pause();
                this.mSecondaryThresholdSensor.pause();
                this.mPrimaryThresholdSensor.register(this.mPrimaryEventListener);
                this.mSecondaryThresholdSensor.register(this.mSecondaryEventListener);
                this.mInitializedListeners = true;
            }
            this.mRegistered = true;
            chooseSensor();
        }
    }

    public final void chooseSensor() {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered && !this.mPaused && !this.mListeners.isEmpty()) {
            if (this.mSecondarySafe) {
                this.mSecondaryThresholdSensor.resume();
                this.mPrimaryThresholdSensor.pause();
                return;
            }
            this.mPrimaryThresholdSensor.resume();
            this.mSecondaryThresholdSensor.pause();
        }
    }

    public void unregister(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        this.mListeners.remove(listener);
        if (this.mListeners.size() == 0) {
            unregisterInternal();
        }
    }

    public void destroy() {
        pause();
    }

    public void unregisterInternal() {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered) {
            logDebug("unregistering sensor listener");
            this.mPrimaryThresholdSensor.pause();
            this.mSecondaryThresholdSensor.pause();
            Runnable runnable = this.mCancelSecondaryRunnable;
            if (runnable != null) {
                runnable.run();
                this.mCancelSecondaryRunnable = null;
            }
            this.mLastPrimaryEvent = null;
            this.mLastEvent = null;
            this.mRegistered = false;
        }
    }

    public Boolean isNear() {
        ThresholdSensorEvent thresholdSensorEvent;
        if (!isLoaded() || (thresholdSensorEvent = this.mLastEvent) == null) {
            return null;
        }
        return Boolean.valueOf(thresholdSensorEvent.getBelow());
    }

    public void alertListeners() {
        this.mExecution.assertIsMainThread();
        if (!this.mAlerting.getAndSet(true)) {
            ThresholdSensorEvent thresholdSensorEvent = this.mLastEvent;
            if (thresholdSensorEvent != null) {
                new ArrayList(this.mListeners).forEach(new ProximitySensorImpl$$ExternalSyntheticLambda0(thresholdSensorEvent));
            }
            this.mAlerting.set(false);
        }
    }

    public final void onPrimarySensorEvent(ThresholdSensorEvent thresholdSensorEvent) {
        this.mExecution.assertIsMainThread();
        if (this.mLastPrimaryEvent == null || thresholdSensorEvent.getBelow() != this.mLastPrimaryEvent.getBelow()) {
            this.mLastPrimaryEvent = thresholdSensorEvent;
            if (this.mSecondarySafe && this.mSecondaryThresholdSensor.isLoaded()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Primary sensor reported ");
                sb.append(thresholdSensorEvent.getBelow() ? "near" : "far");
                sb.append(". Checking secondary.");
                logDebug(sb.toString());
                if (this.mCancelSecondaryRunnable == null) {
                    this.mSecondaryThresholdSensor.resume();
                }
            } else if (!this.mSecondaryThresholdSensor.isLoaded()) {
                logDebug("Primary sensor event: " + thresholdSensorEvent.getBelow() + ". No secondary.");
                onSensorEvent(thresholdSensorEvent);
            } else if (thresholdSensorEvent.getBelow()) {
                logDebug("Primary sensor event: " + thresholdSensorEvent.getBelow() + ". Checking secondary.");
                Runnable runnable = this.mCancelSecondaryRunnable;
                if (runnable != null) {
                    runnable.run();
                }
                this.mSecondaryThresholdSensor.resume();
            } else {
                onSensorEvent(thresholdSensorEvent);
            }
        }
    }

    public final void onSensorEvent(ThresholdSensorEvent thresholdSensorEvent) {
        this.mExecution.assertIsMainThread();
        if (this.mLastEvent == null || thresholdSensorEvent.getBelow() != this.mLastEvent.getBelow()) {
            if (!this.mSecondarySafe && !thresholdSensorEvent.getBelow()) {
                chooseSensor();
            }
            this.mLastEvent = thresholdSensorEvent;
            alertListeners();
        }
    }

    public String toString() {
        return String.format("{registered=%s, paused=%s, near=%s, posture=%s, primarySensor=%s, secondarySensor=%s secondarySafe=%s}", new Object[]{Boolean.valueOf(isRegistered()), Boolean.valueOf(this.mPaused), isNear(), Integer.valueOf(this.mDevicePosture), this.mPrimaryThresholdSensor, this.mSecondaryThresholdSensor, Boolean.valueOf(this.mSecondarySafe)});
    }

    public void logDebug(String str) {
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
            Log.d("ProxSensor", sb.toString());
        }
    }
}
