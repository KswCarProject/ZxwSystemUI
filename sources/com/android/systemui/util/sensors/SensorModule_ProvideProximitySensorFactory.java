package com.android.systemui.util.sensors;

import android.content.res.Resources;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SensorModule_ProvideProximitySensorFactory implements Factory<ProximitySensor> {
    public final Provider<PostureDependentProximitySensor> postureDependentProximitySensorProvider;
    public final Provider<ProximitySensorImpl> proximitySensorProvider;
    public final Provider<Resources> resourcesProvider;

    public SensorModule_ProvideProximitySensorFactory(Provider<Resources> provider, Provider<PostureDependentProximitySensor> provider2, Provider<ProximitySensorImpl> provider3) {
        this.resourcesProvider = provider;
        this.postureDependentProximitySensorProvider = provider2;
        this.proximitySensorProvider = provider3;
    }

    public ProximitySensor get() {
        return provideProximitySensor(this.resourcesProvider.get(), DoubleCheck.lazy(this.postureDependentProximitySensorProvider), DoubleCheck.lazy(this.proximitySensorProvider));
    }

    public static SensorModule_ProvideProximitySensorFactory create(Provider<Resources> provider, Provider<PostureDependentProximitySensor> provider2, Provider<ProximitySensorImpl> provider3) {
        return new SensorModule_ProvideProximitySensorFactory(provider, provider2, provider3);
    }

    public static ProximitySensor provideProximitySensor(Resources resources, Lazy<PostureDependentProximitySensor> lazy, Lazy<ProximitySensorImpl> lazy2) {
        return (ProximitySensor) Preconditions.checkNotNullFromProvides(SensorModule.provideProximitySensor(resources, lazy, lazy2));
    }
}
