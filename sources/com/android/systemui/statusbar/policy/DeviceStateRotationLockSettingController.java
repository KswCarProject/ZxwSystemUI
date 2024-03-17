package com.android.systemui.statusbar.policy;

import android.hardware.devicestate.DeviceStateManager;
import android.os.Trace;
import android.util.Log;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import java.util.concurrent.Executor;

public final class DeviceStateRotationLockSettingController implements RotationLockController.RotationLockControllerCallback {
    public int mDeviceState = -1;
    public DeviceStateManager.DeviceStateCallback mDeviceStateCallback;
    public final DeviceStateManager mDeviceStateManager;
    public DeviceStateRotationLockSettingsManager.DeviceStateRotationLockSettingsListener mDeviceStateRotationLockSettingsListener;
    public final DeviceStateRotationLockSettingsManager mDeviceStateRotationLockSettingsManager;
    public final Executor mMainExecutor;
    public final RotationPolicyWrapper mRotationPolicyWrapper;

    public DeviceStateRotationLockSettingController(RotationPolicyWrapper rotationPolicyWrapper, DeviceStateManager deviceStateManager, Executor executor, DeviceStateRotationLockSettingsManager deviceStateRotationLockSettingsManager) {
        this.mRotationPolicyWrapper = rotationPolicyWrapper;
        this.mDeviceStateManager = deviceStateManager;
        this.mMainExecutor = executor;
        this.mDeviceStateRotationLockSettingsManager = deviceStateRotationLockSettingsManager;
    }

    public void setListening(boolean z) {
        if (z) {
            DeviceStateRotationLockSettingController$$ExternalSyntheticLambda0 deviceStateRotationLockSettingController$$ExternalSyntheticLambda0 = new DeviceStateRotationLockSettingController$$ExternalSyntheticLambda0(this);
            this.mDeviceStateCallback = deviceStateRotationLockSettingController$$ExternalSyntheticLambda0;
            this.mDeviceStateManager.registerCallback(this.mMainExecutor, deviceStateRotationLockSettingController$$ExternalSyntheticLambda0);
            DeviceStateRotationLockSettingController$$ExternalSyntheticLambda1 deviceStateRotationLockSettingController$$ExternalSyntheticLambda1 = new DeviceStateRotationLockSettingController$$ExternalSyntheticLambda1(this);
            this.mDeviceStateRotationLockSettingsListener = deviceStateRotationLockSettingController$$ExternalSyntheticLambda1;
            this.mDeviceStateRotationLockSettingsManager.registerListener(deviceStateRotationLockSettingController$$ExternalSyntheticLambda1);
            return;
        }
        DeviceStateManager.DeviceStateCallback deviceStateCallback = this.mDeviceStateCallback;
        if (deviceStateCallback != null) {
            this.mDeviceStateManager.unregisterCallback(deviceStateCallback);
        }
        DeviceStateRotationLockSettingsManager.DeviceStateRotationLockSettingsListener deviceStateRotationLockSettingsListener = this.mDeviceStateRotationLockSettingsListener;
        if (deviceStateRotationLockSettingsListener != null) {
            this.mDeviceStateRotationLockSettingsManager.unregisterListener(deviceStateRotationLockSettingsListener);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setListening$0() {
        readPersistedSetting(this.mDeviceState);
    }

    public void onRotationLockStateChanged(boolean z, boolean z2) {
        int i = this.mDeviceState;
        if (i == -1) {
            Log.wtf("DSRotateLockSettingCon", "Device state was not initialized.");
        } else if (z == this.mDeviceStateRotationLockSettingsManager.isRotationLocked(i)) {
            Log.v("DSRotateLockSettingCon", "Rotation lock same as the current setting, no need to update.");
        } else {
            saveNewRotationLockSetting(z);
        }
    }

    public final void saveNewRotationLockSetting(boolean z) {
        Log.v("DSRotateLockSettingCon", "saveNewRotationLockSetting [state=" + this.mDeviceState + "] [isRotationLocked=" + z + "]");
        this.mDeviceStateRotationLockSettingsManager.updateSetting(this.mDeviceState, z);
    }

    public final void updateDeviceState(int i) {
        Log.v("DSRotateLockSettingCon", "updateDeviceState [state=" + i + "]");
        Trace.beginSection("updateDeviceState [state=" + i + "]");
        try {
            if (this.mDeviceState != i) {
                readPersistedSetting(i);
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    public final void readPersistedSetting(int i) {
        int rotationLockSetting = this.mDeviceStateRotationLockSettingsManager.getRotationLockSetting(i);
        if (rotationLockSetting == 0) {
            Log.w("DSRotateLockSettingCon", "Missing fallback. Ignoring new device state: " + i);
            return;
        }
        this.mDeviceState = i;
        boolean z = true;
        if (rotationLockSetting != 1) {
            z = false;
        }
        if (z != this.mRotationPolicyWrapper.isRotationLocked()) {
            this.mRotationPolicyWrapper.setRotationLock(z);
        }
    }
}
