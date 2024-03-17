package com.android.systemui.statusbar.policy;

import android.hardware.SensorPrivacyManager;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import java.util.ArrayList;
import java.util.List;

public class SensorPrivacyControllerImpl implements SensorPrivacyController, SensorPrivacyManager.OnAllSensorPrivacyChangedListener {
    public final List<SensorPrivacyController.OnSensorPrivacyChangedListener> mListeners = new ArrayList(1);
    public Object mLock = new Object();
    public boolean mSensorPrivacyEnabled;
    public SensorPrivacyManager mSensorPrivacyManager;

    public SensorPrivacyControllerImpl(SensorPrivacyManager sensorPrivacyManager) {
        this.mSensorPrivacyManager = sensorPrivacyManager;
    }

    public void init() {
        this.mSensorPrivacyEnabled = this.mSensorPrivacyManager.isAllSensorPrivacyEnabled();
        this.mSensorPrivacyManager.addAllSensorPrivacyListener(this);
    }

    public boolean isSensorPrivacyEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSensorPrivacyEnabled;
        }
        return z;
    }

    public void addCallback(SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener) {
        synchronized (this.mLock) {
            this.mListeners.add(onSensorPrivacyChangedListener);
            notifyListenerLocked(onSensorPrivacyChangedListener);
        }
    }

    public void removeCallback(SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener) {
        synchronized (this.mLock) {
            this.mListeners.remove(onSensorPrivacyChangedListener);
        }
    }

    public void onAllSensorPrivacyChanged(boolean z) {
        synchronized (this.mLock) {
            this.mSensorPrivacyEnabled = z;
            for (SensorPrivacyController.OnSensorPrivacyChangedListener notifyListenerLocked : this.mListeners) {
                notifyListenerLocked(notifyListenerLocked);
            }
        }
    }

    public final void notifyListenerLocked(SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener) {
        onSensorPrivacyChangedListener.onSensorPrivacyChanged(this.mSensorPrivacyEnabled);
    }
}
