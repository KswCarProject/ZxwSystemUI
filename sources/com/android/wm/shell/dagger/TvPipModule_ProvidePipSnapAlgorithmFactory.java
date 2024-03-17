package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class TvPipModule_ProvidePipSnapAlgorithmFactory implements Factory<PipSnapAlgorithm> {

    public static final class InstanceHolder {
        public static final TvPipModule_ProvidePipSnapAlgorithmFactory INSTANCE = new TvPipModule_ProvidePipSnapAlgorithmFactory();
    }

    public PipSnapAlgorithm get() {
        return providePipSnapAlgorithm();
    }

    public static TvPipModule_ProvidePipSnapAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSnapAlgorithm providePipSnapAlgorithm() {
        return (PipSnapAlgorithm) Preconditions.checkNotNullFromProvides(TvPipModule.providePipSnapAlgorithm());
    }
}
