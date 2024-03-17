package com.android.systemui.dagger;

import android.content.Context;
import android.telephony.CarrierConfigManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvideCarrierConfigManagerFactory implements Factory<CarrierConfigManager> {
    public final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideCarrierConfigManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public CarrierConfigManager get() {
        return provideCarrierConfigManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideCarrierConfigManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideCarrierConfigManagerFactory(provider);
    }

    public static CarrierConfigManager provideCarrierConfigManager(Context context) {
        return (CarrierConfigManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideCarrierConfigManager(context));
    }
}
