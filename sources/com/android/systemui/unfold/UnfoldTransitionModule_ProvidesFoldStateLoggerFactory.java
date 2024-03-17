package com.android.systemui.unfold;

import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class UnfoldTransitionModule_ProvidesFoldStateLoggerFactory implements Factory<Optional<FoldStateLogger>> {
    public final UnfoldTransitionModule module;
    public final Provider<Optional<FoldStateLoggingProvider>> optionalFoldStateLoggingProvider;

    public UnfoldTransitionModule_ProvidesFoldStateLoggerFactory(UnfoldTransitionModule unfoldTransitionModule, Provider<Optional<FoldStateLoggingProvider>> provider) {
        this.module = unfoldTransitionModule;
        this.optionalFoldStateLoggingProvider = provider;
    }

    public Optional<FoldStateLogger> get() {
        return providesFoldStateLogger(this.module, this.optionalFoldStateLoggingProvider.get());
    }

    public static UnfoldTransitionModule_ProvidesFoldStateLoggerFactory create(UnfoldTransitionModule unfoldTransitionModule, Provider<Optional<FoldStateLoggingProvider>> provider) {
        return new UnfoldTransitionModule_ProvidesFoldStateLoggerFactory(unfoldTransitionModule, provider);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.systemui.unfold.FoldStateLoggingProvider>, java.util.Optional] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.unfold.FoldStateLogger> providesFoldStateLogger(com.android.systemui.unfold.UnfoldTransitionModule r0, java.util.Optional<com.android.systemui.unfold.FoldStateLoggingProvider> r1) {
        /*
            java.util.Optional r0 = r0.providesFoldStateLogger(r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.unfold.UnfoldTransitionModule_ProvidesFoldStateLoggerFactory.providesFoldStateLogger(com.android.systemui.unfold.UnfoldTransitionModule, java.util.Optional):java.util.Optional");
    }
}
