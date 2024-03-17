package com.android.systemui.dagger;

import android.content.Context;
import android.view.accessibility.CaptioningManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FrameworkServicesModule_ProvideCaptioningManagerFactory implements Factory<CaptioningManager> {
    public final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideCaptioningManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public CaptioningManager get() {
        return provideCaptioningManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideCaptioningManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideCaptioningManagerFactory(provider);
    }

    public static CaptioningManager provideCaptioningManager(Context context) {
        return (CaptioningManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideCaptioningManager(context));
    }
}
