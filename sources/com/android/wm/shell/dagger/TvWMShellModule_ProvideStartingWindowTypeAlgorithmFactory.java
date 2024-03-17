package com.android.wm.shell.dagger;

import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory implements Factory<StartingWindowTypeAlgorithm> {

    public static final class InstanceHolder {
        public static final TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory INSTANCE = new TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory();
    }

    public StartingWindowTypeAlgorithm get() {
        return provideStartingWindowTypeAlgorithm();
    }

    public static TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm() {
        return (StartingWindowTypeAlgorithm) Preconditions.checkNotNullFromProvides(TvWMShellModule.provideStartingWindowTypeAlgorithm());
    }
}
