package com.android.systemui.keyguard;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class LifecycleScreenStatusProvider_Factory implements Factory<LifecycleScreenStatusProvider> {
    public final Provider<ScreenLifecycle> screenLifecycleProvider;

    public LifecycleScreenStatusProvider_Factory(Provider<ScreenLifecycle> provider) {
        this.screenLifecycleProvider = provider;
    }

    public LifecycleScreenStatusProvider get() {
        return newInstance(this.screenLifecycleProvider.get());
    }

    public static LifecycleScreenStatusProvider_Factory create(Provider<ScreenLifecycle> provider) {
        return new LifecycleScreenStatusProvider_Factory(provider);
    }

    public static LifecycleScreenStatusProvider newInstance(ScreenLifecycle screenLifecycle) {
        return new LifecycleScreenStatusProvider(screenLifecycle);
    }
}
