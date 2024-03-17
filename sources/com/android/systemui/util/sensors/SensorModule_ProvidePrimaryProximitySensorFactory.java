package com.android.systemui.util.sensors;

import android.hardware.SensorManager;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SensorModule_ProvidePrimaryProximitySensorFactory implements Factory<ThresholdSensor> {
    public final Provider<SensorManager> sensorManagerProvider;
    public final Provider<ThresholdSensorImpl.Builder> thresholdSensorBuilderProvider;

    public SensorModule_ProvidePrimaryProximitySensorFactory(Provider<SensorManager> provider, Provider<ThresholdSensorImpl.Builder> provider2) {
        this.sensorManagerProvider = provider;
        this.thresholdSensorBuilderProvider = provider2;
    }

    public ThresholdSensor get() {
        return providePrimaryProximitySensor(this.sensorManagerProvider.get(), this.thresholdSensorBuilderProvider.get());
    }

    public static SensorModule_ProvidePrimaryProximitySensorFactory create(Provider<SensorManager> provider, Provider<ThresholdSensorImpl.Builder> provider2) {
        return new SensorModule_ProvidePrimaryProximitySensorFactory(provider, provider2);
    }

    public static ThresholdSensor providePrimaryProximitySensor(SensorManager sensorManager, ThresholdSensorImpl.Builder builder) {
        return (ThresholdSensor) Preconditions.checkNotNullFromProvides(SensorModule.providePrimaryProximitySensor(sensorManager, builder));
    }
}
