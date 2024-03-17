package com.android.systemui.statusbar.policy;

import android.hardware.devicestate.DeviceStateManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DeviceStateRotationLockSettingController$$ExternalSyntheticLambda0 implements DeviceStateManager.DeviceStateCallback {
    public final /* synthetic */ DeviceStateRotationLockSettingController f$0;

    public /* synthetic */ DeviceStateRotationLockSettingController$$ExternalSyntheticLambda0(DeviceStateRotationLockSettingController deviceStateRotationLockSettingController) {
        this.f$0 = deviceStateRotationLockSettingController;
    }

    public final void onStateChanged(int i) {
        this.f$0.updateDeviceState(i);
    }
}
