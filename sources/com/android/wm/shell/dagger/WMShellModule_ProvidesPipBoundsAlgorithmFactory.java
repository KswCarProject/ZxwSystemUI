package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellModule_ProvidesPipBoundsAlgorithmFactory implements Factory<PipBoundsAlgorithm> {
    public final Provider<Context> contextProvider;
    public final Provider<PipBoundsState> pipBoundsStateProvider;
    public final Provider<PipSnapAlgorithm> pipSnapAlgorithmProvider;

    public WMShellModule_ProvidesPipBoundsAlgorithmFactory(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipSnapAlgorithm> provider3) {
        this.contextProvider = provider;
        this.pipBoundsStateProvider = provider2;
        this.pipSnapAlgorithmProvider = provider3;
    }

    public PipBoundsAlgorithm get() {
        return providesPipBoundsAlgorithm(this.contextProvider.get(), this.pipBoundsStateProvider.get(), this.pipSnapAlgorithmProvider.get());
    }

    public static WMShellModule_ProvidesPipBoundsAlgorithmFactory create(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipSnapAlgorithm> provider3) {
        return new WMShellModule_ProvidesPipBoundsAlgorithmFactory(provider, provider2, provider3);
    }

    public static PipBoundsAlgorithm providesPipBoundsAlgorithm(Context context, PipBoundsState pipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        return (PipBoundsAlgorithm) Preconditions.checkNotNullFromProvides(WMShellModule.providesPipBoundsAlgorithm(context, pipBoundsState, pipSnapAlgorithm));
    }
}
