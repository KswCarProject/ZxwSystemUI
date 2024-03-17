package com.android.systemui.util.sensors;

import android.content.res.Resources;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory implements Factory<ThresholdSensor[]> {
    public final Provider<Resources> resourcesProvider;
    public final Provider<ThresholdSensorImpl.BuilderFactory> thresholdSensorImplBuilderFactoryProvider;

    public SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory(Provider<ThresholdSensorImpl.BuilderFactory> provider, Provider<Resources> provider2) {
        this.thresholdSensorImplBuilderFactoryProvider = provider;
        this.resourcesProvider = provider2;
    }

    public ThresholdSensor[] get() {
        return providePostureToSecondaryProximitySensorMapping(this.thresholdSensorImplBuilderFactoryProvider.get(), this.resourcesProvider.get());
    }

    public static SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory create(Provider<ThresholdSensorImpl.BuilderFactory> provider, Provider<Resources> provider2) {
        return new SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory(provider, provider2);
    }

    public static ThresholdSensor[] providePostureToSecondaryProximitySensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, Resources resources) {
        return (ThresholdSensor[]) Preconditions.checkNotNullFromProvides(SensorModule.providePostureToSecondaryProximitySensorMapping(builderFactory, resources));
    }
}
