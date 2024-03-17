package com.android.systemui.util.sensors;

import android.content.res.Resources;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ThresholdSensorImpl_BuilderFactory_Factory implements Factory<ThresholdSensorImpl.BuilderFactory> {
    public final Provider<Execution> executionProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<AsyncSensorManager> sensorManagerProvider;

    public ThresholdSensorImpl_BuilderFactory_Factory(Provider<Resources> provider, Provider<AsyncSensorManager> provider2, Provider<Execution> provider3) {
        this.resourcesProvider = provider;
        this.sensorManagerProvider = provider2;
        this.executionProvider = provider3;
    }

    public ThresholdSensorImpl.BuilderFactory get() {
        return newInstance(this.resourcesProvider.get(), this.sensorManagerProvider.get(), this.executionProvider.get());
    }

    public static ThresholdSensorImpl_BuilderFactory_Factory create(Provider<Resources> provider, Provider<AsyncSensorManager> provider2, Provider<Execution> provider3) {
        return new ThresholdSensorImpl_BuilderFactory_Factory(provider, provider2, provider3);
    }

    public static ThresholdSensorImpl.BuilderFactory newInstance(Resources resources, AsyncSensorManager asyncSensorManager, Execution execution) {
        return new ThresholdSensorImpl.BuilderFactory(resources, asyncSensorManager, execution);
    }
}
