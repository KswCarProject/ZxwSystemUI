package com.android.systemui.util.sensors;

import android.content.res.Resources;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SensorModule_ProvidePostureToProximitySensorMappingFactory implements Factory<ThresholdSensor[]> {
    public final Provider<Resources> resourcesProvider;
    public final Provider<ThresholdSensorImpl.BuilderFactory> thresholdSensorImplBuilderFactoryProvider;

    public SensorModule_ProvidePostureToProximitySensorMappingFactory(Provider<ThresholdSensorImpl.BuilderFactory> provider, Provider<Resources> provider2) {
        this.thresholdSensorImplBuilderFactoryProvider = provider;
        this.resourcesProvider = provider2;
    }

    public ThresholdSensor[] get() {
        return providePostureToProximitySensorMapping(this.thresholdSensorImplBuilderFactoryProvider.get(), this.resourcesProvider.get());
    }

    public static SensorModule_ProvidePostureToProximitySensorMappingFactory create(Provider<ThresholdSensorImpl.BuilderFactory> provider, Provider<Resources> provider2) {
        return new SensorModule_ProvidePostureToProximitySensorMappingFactory(provider, provider2);
    }

    public static ThresholdSensor[] providePostureToProximitySensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, Resources resources) {
        return (ThresholdSensor[]) Preconditions.checkNotNullFromProvides(SensorModule.providePostureToProximitySensorMapping(builderFactory, resources));
    }
}
