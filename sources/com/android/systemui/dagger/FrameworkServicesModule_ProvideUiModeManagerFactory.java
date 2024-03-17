package com.android.systemui.dagger;

import android.app.UiModeManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvideUiModeManagerFactory implements Factory<UiModeManager> {
    public final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideUiModeManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public UiModeManager get() {
        return provideUiModeManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideUiModeManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideUiModeManagerFactory(provider);
    }

    public static UiModeManager provideUiModeManager(Context context) {
        return (UiModeManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideUiModeManager(context));
    }
}
