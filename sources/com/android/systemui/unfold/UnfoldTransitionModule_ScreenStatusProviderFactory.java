package com.android.systemui.unfold;

import com.android.systemui.keyguard.LifecycleScreenStatusProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UnfoldTransitionModule_ScreenStatusProviderFactory implements Factory<ScreenStatusProvider> {
    public final Provider<LifecycleScreenStatusProvider> implProvider;
    public final UnfoldTransitionModule module;

    public UnfoldTransitionModule_ScreenStatusProviderFactory(UnfoldTransitionModule unfoldTransitionModule, Provider<LifecycleScreenStatusProvider> provider) {
        this.module = unfoldTransitionModule;
        this.implProvider = provider;
    }

    public ScreenStatusProvider get() {
        return screenStatusProvider(this.module, this.implProvider.get());
    }

    public static UnfoldTransitionModule_ScreenStatusProviderFactory create(UnfoldTransitionModule unfoldTransitionModule, Provider<LifecycleScreenStatusProvider> provider) {
        return new UnfoldTransitionModule_ScreenStatusProviderFactory(unfoldTransitionModule, provider);
    }

    public static ScreenStatusProvider screenStatusProvider(UnfoldTransitionModule unfoldTransitionModule, LifecycleScreenStatusProvider lifecycleScreenStatusProvider) {
        return (ScreenStatusProvider) Preconditions.checkNotNullFromProvides(unfoldTransitionModule.screenStatusProvider(lifecycleScreenStatusProvider));
    }
}
