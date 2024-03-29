package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.SensorManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvidesSensorManagerFactory implements Factory<SensorManager> {
    public final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvidesSensorManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public SensorManager get() {
        return providesSensorManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvidesSensorManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvidesSensorManagerFactory(provider);
    }

    public static SensorManager providesSensorManager(Context context) {
        return (SensorManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.providesSensorManager(context));
    }
}
