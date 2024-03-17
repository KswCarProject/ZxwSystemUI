package com.android.systemui.unfold;

import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.updates.FoldStateProvider;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory implements Factory<Optional<FoldStateLoggingProvider>> {
    public final Provider<UnfoldTransitionConfig> configProvider;
    public final Provider<FoldStateProvider> foldStateProvider;
    public final UnfoldTransitionModule module;

    public UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory(UnfoldTransitionModule unfoldTransitionModule, Provider<UnfoldTransitionConfig> provider, Provider<FoldStateProvider> provider2) {
        this.module = unfoldTransitionModule;
        this.configProvider = provider;
        this.foldStateProvider = provider2;
    }

    public Optional<FoldStateLoggingProvider> get() {
        return providesFoldStateLoggingProvider(this.module, this.configProvider.get(), DoubleCheck.lazy(this.foldStateProvider));
    }

    public static UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory create(UnfoldTransitionModule unfoldTransitionModule, Provider<UnfoldTransitionConfig> provider, Provider<FoldStateProvider> provider2) {
        return new UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory(unfoldTransitionModule, provider, provider2);
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [dagger.Lazy<com.android.systemui.unfold.updates.FoldStateProvider>, dagger.Lazy] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.unfold.FoldStateLoggingProvider> providesFoldStateLoggingProvider(com.android.systemui.unfold.UnfoldTransitionModule r0, com.android.systemui.unfold.config.UnfoldTransitionConfig r1, dagger.Lazy<com.android.systemui.unfold.updates.FoldStateProvider> r2) {
        /*
            java.util.Optional r0 = r0.providesFoldStateLoggingProvider(r1, r2)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.unfold.UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory.providesFoldStateLoggingProvider(com.android.systemui.unfold.UnfoldTransitionModule, com.android.systemui.unfold.config.UnfoldTransitionConfig, dagger.Lazy):java.util.Optional");
    }
}
