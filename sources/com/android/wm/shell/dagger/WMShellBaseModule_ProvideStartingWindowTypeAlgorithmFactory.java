package com.android.wm.shell.dagger;

import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory implements Factory<StartingWindowTypeAlgorithm> {
    public final Provider<Optional<StartingWindowTypeAlgorithm>> startingWindowTypeAlgorithmProvider;

    public WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory(Provider<Optional<StartingWindowTypeAlgorithm>> provider) {
        this.startingWindowTypeAlgorithmProvider = provider;
    }

    public StartingWindowTypeAlgorithm get() {
        return provideStartingWindowTypeAlgorithm(this.startingWindowTypeAlgorithmProvider.get());
    }

    public static WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory create(Provider<Optional<StartingWindowTypeAlgorithm>> provider) {
        return new WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory(provider);
    }

    public static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm(Optional<StartingWindowTypeAlgorithm> optional) {
        return (StartingWindowTypeAlgorithm) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideStartingWindowTypeAlgorithm(optional));
    }
}
