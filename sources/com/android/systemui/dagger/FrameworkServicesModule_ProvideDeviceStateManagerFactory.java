package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvideDeviceStateManagerFactory implements Factory<DeviceStateManager> {
    public final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideDeviceStateManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public DeviceStateManager get() {
        return provideDeviceStateManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideDeviceStateManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideDeviceStateManagerFactory(provider);
    }

    public static DeviceStateManager provideDeviceStateManager(Context context) {
        return (DeviceStateManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideDeviceStateManager(context));
    }
}
