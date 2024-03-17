package com.android.systemui.dagger;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ReferenceSystemUIModule_BindDeviceProvisionedControllerFactory implements Factory<DeviceProvisionedController> {
    public final Provider<DeviceProvisionedControllerImpl> deviceProvisionedControllerProvider;

    public ReferenceSystemUIModule_BindDeviceProvisionedControllerFactory(Provider<DeviceProvisionedControllerImpl> provider) {
        this.deviceProvisionedControllerProvider = provider;
    }

    public DeviceProvisionedController get() {
        return bindDeviceProvisionedController(this.deviceProvisionedControllerProvider.get());
    }

    public static ReferenceSystemUIModule_BindDeviceProvisionedControllerFactory create(Provider<DeviceProvisionedControllerImpl> provider) {
        return new ReferenceSystemUIModule_BindDeviceProvisionedControllerFactory(provider);
    }

    public static DeviceProvisionedController bindDeviceProvisionedController(DeviceProvisionedControllerImpl deviceProvisionedControllerImpl) {
        return (DeviceProvisionedController) Preconditions.checkNotNullFromProvides(ReferenceSystemUIModule.bindDeviceProvisionedController(deviceProvisionedControllerImpl));
    }
}
