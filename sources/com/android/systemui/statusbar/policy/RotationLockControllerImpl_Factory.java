package com.android.systemui.statusbar.policy;

import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RotationLockControllerImpl_Factory implements Factory<RotationLockControllerImpl> {
    public final Provider<String[]> deviceStateRotationLockDefaultsProvider;
    public final Provider<DeviceStateRotationLockSettingController> deviceStateRotationLockSettingControllerProvider;
    public final Provider<RotationPolicyWrapper> rotationPolicyWrapperProvider;

    public RotationLockControllerImpl_Factory(Provider<RotationPolicyWrapper> provider, Provider<DeviceStateRotationLockSettingController> provider2, Provider<String[]> provider3) {
        this.rotationPolicyWrapperProvider = provider;
        this.deviceStateRotationLockSettingControllerProvider = provider2;
        this.deviceStateRotationLockDefaultsProvider = provider3;
    }

    public RotationLockControllerImpl get() {
        return newInstance(this.rotationPolicyWrapperProvider.get(), this.deviceStateRotationLockSettingControllerProvider.get(), this.deviceStateRotationLockDefaultsProvider.get());
    }

    public static RotationLockControllerImpl_Factory create(Provider<RotationPolicyWrapper> provider, Provider<DeviceStateRotationLockSettingController> provider2, Provider<String[]> provider3) {
        return new RotationLockControllerImpl_Factory(provider, provider2, provider3);
    }

    public static RotationLockControllerImpl newInstance(RotationPolicyWrapper rotationPolicyWrapper, DeviceStateRotationLockSettingController deviceStateRotationLockSettingController, String[] strArr) {
        return new RotationLockControllerImpl(rotationPolicyWrapper, deviceStateRotationLockSettingController, strArr);
    }
}
