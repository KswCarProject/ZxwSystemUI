package com.android.systemui.util.sensors;

import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SensorModule_ProvideSecondaryProximitySensorFactory implements Factory<ThresholdSensor> {
    public final Provider<ThresholdSensorImpl.Builder> thresholdSensorBuilderProvider;

    public SensorModule_ProvideSecondaryProximitySensorFactory(Provider<ThresholdSensorImpl.Builder> provider) {
        this.thresholdSensorBuilderProvider = provider;
    }

    public ThresholdSensor get() {
        return provideSecondaryProximitySensor(this.thresholdSensorBuilderProvider.get());
    }

    public static SensorModule_ProvideSecondaryProximitySensorFactory create(Provider<ThresholdSensorImpl.Builder> provider) {
        return new SensorModule_ProvideSecondaryProximitySensorFactory(provider);
    }

    public static ThresholdSensor provideSecondaryProximitySensor(ThresholdSensorImpl.Builder builder) {
        return (ThresholdSensor) Preconditions.checkNotNullFromProvides(SensorModule.provideSecondaryProximitySensor(builder));
    }
}
