package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DevicePostureControllerImpl_Factory implements Factory<DevicePostureControllerImpl> {
    public final Provider<Context> contextProvider;
    public final Provider<DeviceStateManager> deviceStateManagerProvider;
    public final Provider<Executor> executorProvider;

    public DevicePostureControllerImpl_Factory(Provider<Context> provider, Provider<DeviceStateManager> provider2, Provider<Executor> provider3) {
        this.contextProvider = provider;
        this.deviceStateManagerProvider = provider2;
        this.executorProvider = provider3;
    }

    public DevicePostureControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.deviceStateManagerProvider.get(), this.executorProvider.get());
    }

    public static DevicePostureControllerImpl_Factory create(Provider<Context> provider, Provider<DeviceStateManager> provider2, Provider<Executor> provider3) {
        return new DevicePostureControllerImpl_Factory(provider, provider2, provider3);
    }

    public static DevicePostureControllerImpl newInstance(Context context, DeviceStateManager deviceStateManager, Executor executor) {
        return new DevicePostureControllerImpl(context, deviceStateManager, executor);
    }
}
