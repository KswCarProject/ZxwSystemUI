package com.android.systemui.dreams.dagger;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesLifecycleFactory implements Factory<Lifecycle> {
    public final Provider<LifecycleOwner> lifecycleOwnerProvider;

    public DreamOverlayModule_ProvidesLifecycleFactory(Provider<LifecycleOwner> provider) {
        this.lifecycleOwnerProvider = provider;
    }

    public Lifecycle get() {
        return providesLifecycle(this.lifecycleOwnerProvider.get());
    }

    public static DreamOverlayModule_ProvidesLifecycleFactory create(Provider<LifecycleOwner> provider) {
        return new DreamOverlayModule_ProvidesLifecycleFactory(provider);
    }

    public static Lifecycle providesLifecycle(LifecycleOwner lifecycleOwner) {
        return (Lifecycle) Preconditions.checkNotNullFromProvides(DreamOverlayModule.providesLifecycle(lifecycleOwner));
    }
}
