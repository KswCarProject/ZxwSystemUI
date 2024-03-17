package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardEnvironmentImpl_Factory implements Factory<KeyguardEnvironmentImpl> {
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;

    public KeyguardEnvironmentImpl_Factory(Provider<NotificationLockscreenUserManager> provider, Provider<DeviceProvisionedController> provider2) {
        this.notificationLockscreenUserManagerProvider = provider;
        this.deviceProvisionedControllerProvider = provider2;
    }

    public KeyguardEnvironmentImpl get() {
        return newInstance(this.notificationLockscreenUserManagerProvider.get(), this.deviceProvisionedControllerProvider.get());
    }

    public static KeyguardEnvironmentImpl_Factory create(Provider<NotificationLockscreenUserManager> provider, Provider<DeviceProvisionedController> provider2) {
        return new KeyguardEnvironmentImpl_Factory(provider, provider2);
    }

    public static KeyguardEnvironmentImpl newInstance(NotificationLockscreenUserManager notificationLockscreenUserManager, DeviceProvisionedController deviceProvisionedController) {
        return new KeyguardEnvironmentImpl(notificationLockscreenUserManager, deviceProvisionedController);
    }
}
