package com.android.systemui.statusbar.policy;

import android.hardware.devicestate.DeviceStateManager;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DeviceStateRotationLockSettingController_Factory implements Factory<DeviceStateRotationLockSettingController> {
    public final Provider<DeviceStateManager> deviceStateManagerProvider;
    public final Provider<DeviceStateRotationLockSettingsManager> deviceStateRotationLockSettingsManagerProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<RotationPolicyWrapper> rotationPolicyWrapperProvider;

    public DeviceStateRotationLockSettingController_Factory(Provider<RotationPolicyWrapper> provider, Provider<DeviceStateManager> provider2, Provider<Executor> provider3, Provider<DeviceStateRotationLockSettingsManager> provider4) {
        this.rotationPolicyWrapperProvider = provider;
        this.deviceStateManagerProvider = provider2;
        this.executorProvider = provider3;
        this.deviceStateRotationLockSettingsManagerProvider = provider4;
    }

    public DeviceStateRotationLockSettingController get() {
        return newInstance(this.rotationPolicyWrapperProvider.get(), this.deviceStateManagerProvider.get(), this.executorProvider.get(), this.deviceStateRotationLockSettingsManagerProvider.get());
    }

    public static DeviceStateRotationLockSettingController_Factory create(Provider<RotationPolicyWrapper> provider, Provider<DeviceStateManager> provider2, Provider<Executor> provider3, Provider<DeviceStateRotationLockSettingsManager> provider4) {
        return new DeviceStateRotationLockSettingController_Factory(provider, provider2, provider3, provider4);
    }

    public static DeviceStateRotationLockSettingController newInstance(RotationPolicyWrapper rotationPolicyWrapper, DeviceStateManager deviceStateManager, Executor executor, DeviceStateRotationLockSettingsManager deviceStateRotationLockSettingsManager) {
        return new DeviceStateRotationLockSettingController(rotationPolicyWrapper, deviceStateManager, executor, deviceStateRotationLockSettingsManager);
    }
}
