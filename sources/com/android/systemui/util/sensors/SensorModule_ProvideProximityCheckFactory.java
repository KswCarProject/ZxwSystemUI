package com.android.systemui.util.sensors;

import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SensorModule_ProvideProximityCheckFactory implements Factory<ProximityCheck> {
    public final Provider<DelayableExecutor> delayableExecutorProvider;
    public final Provider<ProximitySensor> proximitySensorProvider;

    public SensorModule_ProvideProximityCheckFactory(Provider<ProximitySensor> provider, Provider<DelayableExecutor> provider2) {
        this.proximitySensorProvider = provider;
        this.delayableExecutorProvider = provider2;
    }

    public ProximityCheck get() {
        return provideProximityCheck(this.proximitySensorProvider.get(), this.delayableExecutorProvider.get());
    }

    public static SensorModule_ProvideProximityCheckFactory create(Provider<ProximitySensor> provider, Provider<DelayableExecutor> provider2) {
        return new SensorModule_ProvideProximityCheckFactory(provider, provider2);
    }

    public static ProximityCheck provideProximityCheck(ProximitySensor proximitySensor, DelayableExecutor delayableExecutor) {
        return (ProximityCheck) Preconditions.checkNotNullFromProvides(SensorModule.provideProximityCheck(proximitySensor, delayableExecutor));
    }
}
