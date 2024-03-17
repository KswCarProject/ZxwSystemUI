package com.android.systemui.dreams.dagger;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesLifecycleOwnerFactory implements Factory<LifecycleOwner> {
    public final Provider<LifecycleRegistry> lifecycleRegistryLazyProvider;

    public DreamOverlayModule_ProvidesLifecycleOwnerFactory(Provider<LifecycleRegistry> provider) {
        this.lifecycleRegistryLazyProvider = provider;
    }

    public LifecycleOwner get() {
        return providesLifecycleOwner(DoubleCheck.lazy(this.lifecycleRegistryLazyProvider));
    }

    public static DreamOverlayModule_ProvidesLifecycleOwnerFactory create(Provider<LifecycleRegistry> provider) {
        return new DreamOverlayModule_ProvidesLifecycleOwnerFactory(provider);
    }

    public static LifecycleOwner providesLifecycleOwner(Lazy<LifecycleRegistry> lazy) {
        return (LifecycleOwner) Preconditions.checkNotNullFromProvides(DreamOverlayModule.providesLifecycleOwner(lazy));
    }
}
