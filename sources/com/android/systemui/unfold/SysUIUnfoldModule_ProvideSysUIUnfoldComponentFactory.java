package com.android.systemui.unfold;

import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory implements Factory<Optional<SysUIUnfoldComponent>> {
    public final Provider<SysUIUnfoldComponent.Factory> factoryProvider;
    public final SysUIUnfoldModule module;
    public final Provider<Optional<UnfoldTransitionProgressProvider>> providerProvider;
    public final Provider<Optional<NaturalRotationUnfoldProgressProvider>> rotationProvider;
    public final Provider<Optional<ScopedUnfoldTransitionProgressProvider>> scopedProvider;

    public SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory(SysUIUnfoldModule sysUIUnfoldModule, Provider<Optional<UnfoldTransitionProgressProvider>> provider, Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider2, Provider<Optional<ScopedUnfoldTransitionProgressProvider>> provider3, Provider<SysUIUnfoldComponent.Factory> provider4) {
        this.module = sysUIUnfoldModule;
        this.providerProvider = provider;
        this.rotationProvider = provider2;
        this.scopedProvider = provider3;
        this.factoryProvider = provider4;
    }

    public Optional<SysUIUnfoldComponent> get() {
        return provideSysUIUnfoldComponent(this.module, this.providerProvider.get(), this.rotationProvider.get(), this.scopedProvider.get(), this.factoryProvider.get());
    }

    public static SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory create(SysUIUnfoldModule sysUIUnfoldModule, Provider<Optional<UnfoldTransitionProgressProvider>> provider, Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider2, Provider<Optional<ScopedUnfoldTransitionProgressProvider>> provider3, Provider<SysUIUnfoldComponent.Factory> provider4) {
        return new SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory(sysUIUnfoldModule, provider, provider2, provider3, provider4);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.systemui.unfold.UnfoldTransitionProgressProvider>, java.util.Optional] */
    /* JADX WARNING: type inference failed for: r2v0, types: [java.util.Optional<com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider>, java.util.Optional] */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.util.Optional, java.util.Optional<com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider>] */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.unfold.SysUIUnfoldComponent> provideSysUIUnfoldComponent(com.android.systemui.unfold.SysUIUnfoldModule r0, java.util.Optional<com.android.systemui.unfold.UnfoldTransitionProgressProvider> r1, java.util.Optional<com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider> r2, java.util.Optional<com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider> r3, com.android.systemui.unfold.SysUIUnfoldComponent.Factory r4) {
        /*
            java.util.Optional r0 = r0.provideSysUIUnfoldComponent(r1, r2, r3, r4)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.unfold.SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory.provideSysUIUnfoldComponent(com.android.systemui.unfold.SysUIUnfoldModule, java.util.Optional, java.util.Optional, java.util.Optional, com.android.systemui.unfold.SysUIUnfoldComponent$Factory):java.util.Optional");
    }
}
