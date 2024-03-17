package com.android.systemui.statusbar.policy;

import android.hardware.SensorPrivacyManager;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import java.util.Set;

public class IndividualSensorPrivacyControllerImpl implements IndividualSensorPrivacyController {
    public static final int[] SENSORS = {2, 1};
    public final Set<IndividualSensorPrivacyController.Callback> mCallbacks = new ArraySet();
    public final SparseBooleanArray mHardwareToggleState = new SparseBooleanArray();
    public final SensorPrivacyManager mSensorPrivacyManager;
    public final SparseBooleanArray mSoftwareToggleState = new SparseBooleanArray();

    public IndividualSensorPrivacyControllerImpl(SensorPrivacyManager sensorPrivacyManager) {
        this.mSensorPrivacyManager = sensorPrivacyManager;
    }

    public void init() {
        this.mSensorPrivacyManager.addSensorPrivacyListener(new SensorPrivacyManager.OnSensorPrivacyChangedListener() {
            public void onSensorPrivacyChanged(int i, boolean z) {
            }

            public void onSensorPrivacyChanged(SensorPrivacyManager.OnSensorPrivacyChangedListener.SensorPrivacyChangedParams sensorPrivacyChangedParams) {
                IndividualSensorPrivacyControllerImpl.this.onSensorPrivacyChanged(sensorPrivacyChangedParams.getSensor(), sensorPrivacyChangedParams.getToggleType(), sensorPrivacyChangedParams.isEnabled());
            }
        });
        for (int i : SENSORS) {
            boolean isSensorPrivacyEnabled = this.mSensorPrivacyManager.isSensorPrivacyEnabled(1, i);
            boolean isSensorPrivacyEnabled2 = this.mSensorPrivacyManager.isSensorPrivacyEnabled(2, i);
            this.mSoftwareToggleState.put(i, isSensorPrivacyEnabled);
            this.mHardwareToggleState.put(i, isSensorPrivacyEnabled2);
        }
    }

    public boolean supportsSensorToggle(int i) {
        return this.mSensorPrivacyManager.supportsSensorToggle(i);
    }

    public boolean isSensorBlocked(int i) {
        return this.mSoftwareToggleState.get(i, false) || this.mHardwareToggleState.get(i, false);
    }

    public boolean isSensorBlockedByHardwareToggle(int i) {
        return this.mHardwareToggleState.get(i, false);
    }

    public void setSensorBlocked(int i, int i2, boolean z) {
        this.mSensorPrivacyManager.setSensorPrivacyForProfileGroup(i, i2, z);
    }

    public void suppressSensorPrivacyReminders(int i, boolean z) {
        this.mSensorPrivacyManager.suppressSensorPrivacyReminders(i, z);
    }

    public boolean requiresAuthentication() {
        return this.mSensorPrivacyManager.requiresAuthentication();
    }

    public void addCallback(IndividualSensorPrivacyController.Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(IndividualSensorPrivacyController.Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public final void onSensorPrivacyChanged(int i, int i2, boolean z) {
        if (i2 == 1) {
            this.mSoftwareToggleState.put(i, z);
        } else if (i2 == 2) {
            this.mHardwareToggleState.put(i, z);
        }
        for (IndividualSensorPrivacyController.Callback onSensorBlockedChanged : this.mCallbacks) {
            onSensorBlockedChanged.onSensorBlockedChanged(i, isSensorBlocked(i));
        }
    }
}
