package com.android.systemui.dagger;

import android.content.Context;
import android.view.LayoutInflater;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProviderLayoutInflaterFactory implements Factory<LayoutInflater> {
    public final Provider<Context> contextProvider;
    public final FrameworkServicesModule module;

    public FrameworkServicesModule_ProviderLayoutInflaterFactory(FrameworkServicesModule frameworkServicesModule, Provider<Context> provider) {
        this.module = frameworkServicesModule;
        this.contextProvider = provider;
    }

    public LayoutInflater get() {
        return providerLayoutInflater(this.module, this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProviderLayoutInflaterFactory create(FrameworkServicesModule frameworkServicesModule, Provider<Context> provider) {
        return new FrameworkServicesModule_ProviderLayoutInflaterFactory(frameworkServicesModule, provider);
    }

    public static LayoutInflater providerLayoutInflater(FrameworkServicesModule frameworkServicesModule, Context context) {
        return (LayoutInflater) Preconditions.checkNotNullFromProvides(frameworkServicesModule.providerLayoutInflater(context));
    }
}
