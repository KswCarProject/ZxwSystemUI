package com.android.systemui.dagger;

import android.content.Context;
import android.telecom.TelecomManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvideOptionalTelecomManagerFactory implements Factory<Optional<TelecomManager>> {
    public final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideOptionalTelecomManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public Optional<TelecomManager> get() {
        return provideOptionalTelecomManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideOptionalTelecomManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideOptionalTelecomManagerFactory(provider);
    }

    public static Optional<TelecomManager> provideOptionalTelecomManager(Context context) {
        return (Optional) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideOptionalTelecomManager(context));
    }
}
