package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvideAmbientDisplayConfigurationFactory implements Factory<AmbientDisplayConfiguration> {
    public final Provider<Context> contextProvider;
    public final FrameworkServicesModule module;

    public FrameworkServicesModule_ProvideAmbientDisplayConfigurationFactory(FrameworkServicesModule frameworkServicesModule, Provider<Context> provider) {
        this.module = frameworkServicesModule;
        this.contextProvider = provider;
    }

    public AmbientDisplayConfiguration get() {
        return provideAmbientDisplayConfiguration(this.module, this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideAmbientDisplayConfigurationFactory create(FrameworkServicesModule frameworkServicesModule, Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideAmbientDisplayConfigurationFactory(frameworkServicesModule, provider);
    }

    public static AmbientDisplayConfiguration provideAmbientDisplayConfiguration(FrameworkServicesModule frameworkServicesModule, Context context) {
        return (AmbientDisplayConfiguration) Preconditions.checkNotNullFromProvides(frameworkServicesModule.provideAmbientDisplayConfiguration(context));
    }
}
