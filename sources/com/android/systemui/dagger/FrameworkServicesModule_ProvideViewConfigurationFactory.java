package com.android.systemui.dagger;

import android.content.Context;
import android.view.ViewConfiguration;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvideViewConfigurationFactory implements Factory<ViewConfiguration> {
    public final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideViewConfigurationFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public ViewConfiguration get() {
        return provideViewConfiguration(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideViewConfigurationFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideViewConfigurationFactory(provider);
    }

    public static ViewConfiguration provideViewConfiguration(Context context) {
        return (ViewConfiguration) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideViewConfiguration(context));
    }
}
