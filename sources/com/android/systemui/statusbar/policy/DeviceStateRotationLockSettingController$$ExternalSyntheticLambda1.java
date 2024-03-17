package com.android.systemui.statusbar.policy;

import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DeviceStateRotationLockSettingController$$ExternalSyntheticLambda1 implements DeviceStateRotationLockSettingsManager.DeviceStateRotationLockSettingsListener {
    public final /* synthetic */ DeviceStateRotationLockSettingController f$0;

    public /* synthetic */ DeviceStateRotationLockSettingController$$ExternalSyntheticLambda1(DeviceStateRotationLockSettingController deviceStateRotationLockSettingController) {
        this.f$0 = deviceStateRotationLockSettingController;
    }

    public final void onSettingsChanged() {
        this.f$0.lambda$setListening$0();
    }
}
