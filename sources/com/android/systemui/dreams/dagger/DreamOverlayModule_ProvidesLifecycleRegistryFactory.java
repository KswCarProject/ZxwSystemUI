package com.android.systemui.dreams.dagger;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesLifecycleRegistryFactory implements Factory<LifecycleRegistry> {
    public final Provider<LifecycleOwner> lifecycleOwnerProvider;

    public DreamOverlayModule_ProvidesLifecycleRegistryFactory(Provider<LifecycleOwner> provider) {
        this.lifecycleOwnerProvider = provider;
    }

    public LifecycleRegistry get() {
        return providesLifecycleRegistry(this.lifecycleOwnerProvider.get());
    }

    public static DreamOverlayModule_ProvidesLifecycleRegistryFactory create(Provider<LifecycleOwner> provider) {
        return new DreamOverlayModule_ProvidesLifecycleRegistryFactory(provider);
    }

    public static LifecycleRegistry providesLifecycleRegistry(LifecycleOwner lifecycleOwner) {
        return (LifecycleRegistry) Preconditions.checkNotNullFromProvides(DreamOverlayModule.providesLifecycleRegistry(lifecycleOwner));
    }
}
