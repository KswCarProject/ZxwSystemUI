package com.android.systemui.util.sensors;

import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PostureDependentProximitySensor_Factory implements Factory<PostureDependentProximitySensor> {
    public final Provider<DelayableExecutor> delayableExecutorProvider;
    public final Provider<DevicePostureController> devicePostureControllerProvider;
    public final Provider<Execution> executionProvider;
    public final Provider<ThresholdSensor[]> postureToPrimaryProxSensorMapProvider;
    public final Provider<ThresholdSensor[]> postureToSecondaryProxSensorMapProvider;

    public PostureDependentProximitySensor_Factory(Provider<ThresholdSensor[]> provider, Provider<ThresholdSensor[]> provider2, Provider<DelayableExecutor> provider3, Provider<Execution> provider4, Provider<DevicePostureController> provider5) {
        this.postureToPrimaryProxSensorMapProvider = provider;
        this.postureToSecondaryProxSensorMapProvider = provider2;
        this.delayableExecutorProvider = provider3;
        this.executionProvider = provider4;
        this.devicePostureControllerProvider = provider5;
    }

    public PostureDependentProximitySensor get() {
        return newInstance(this.postureToPrimaryProxSensorMapProvider.get(), this.postureToSecondaryProxSensorMapProvider.get(), this.delayableExecutorProvider.get(), this.executionProvider.get(), this.devicePostureControllerProvider.get());
    }

    public static PostureDependentProximitySensor_Factory create(Provider<ThresholdSensor[]> provider, Provider<ThresholdSensor[]> provider2, Provider<DelayableExecutor> provider3, Provider<Execution> provider4, Provider<DevicePostureController> provider5) {
        return new PostureDependentProximitySensor_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static PostureDependentProximitySensor newInstance(ThresholdSensor[] thresholdSensorArr, ThresholdSensor[] thresholdSensorArr2, DelayableExecutor delayableExecutor, Execution execution, DevicePostureController devicePostureController) {
        return new PostureDependentProximitySensor(thresholdSensorArr, thresholdSensorArr2, delayableExecutor, execution, devicePostureController);
    }
}
