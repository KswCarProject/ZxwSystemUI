package com.android.systemui.dagger;

import android.content.Context;
import android.safetycenter.SafetyCenterManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvideSafetyCenterManagerFactory implements Factory<SafetyCenterManager> {
    public final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideSafetyCenterManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public SafetyCenterManager get() {
        return provideSafetyCenterManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideSafetyCenterManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideSafetyCenterManagerFactory(provider);
    }

    public static SafetyCenterManager provideSafetyCenterManager(Context context) {
        return (SafetyCenterManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideSafetyCenterManager(context));
    }
}
