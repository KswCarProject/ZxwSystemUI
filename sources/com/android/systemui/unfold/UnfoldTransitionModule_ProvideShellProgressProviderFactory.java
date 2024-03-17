package com.android.systemui.unfold;

import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class UnfoldTransitionModule_ProvideShellProgressProviderFactory implements Factory<ShellUnfoldProgressProvider> {
    public final Provider<UnfoldTransitionConfig> configProvider;
    public final UnfoldTransitionModule module;
    public final Provider<Optional<UnfoldTransitionProgressProvider>> providerProvider;

    public UnfoldTransitionModule_ProvideShellProgressProviderFactory(UnfoldTransitionModule unfoldTransitionModule, Provider<UnfoldTransitionConfig> provider, Provider<Optional<UnfoldTransitionProgressProvider>> provider2) {
        this.module = unfoldTransitionModule;
        this.configProvider = provider;
        this.providerProvider = provider2;
    }

    public ShellUnfoldProgressProvider get() {
        return provideShellProgressProvider(this.module, this.configProvider.get(), this.providerProvider.get());
    }

    public static UnfoldTransitionModule_ProvideShellProgressProviderFactory create(UnfoldTransitionModule unfoldTransitionModule, Provider<UnfoldTransitionConfig> provider, Provider<Optional<UnfoldTransitionProgressProvider>> provider2) {
        return new UnfoldTransitionModule_ProvideShellProgressProviderFactory(unfoldTransitionModule, provider, provider2);
    }

    public static ShellUnfoldProgressProvider provideShellProgressProvider(UnfoldTransitionModule unfoldTransitionModule, UnfoldTransitionConfig unfoldTransitionConfig, Optional<UnfoldTransitionProgressProvider> optional) {
        return (ShellUnfoldProgressProvider) Preconditions.checkNotNullFromProvides(unfoldTransitionModule.provideShellProgressProvider(unfoldTransitionConfig, optional));
    }
}
