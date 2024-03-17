package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class WMShellModule_ProvidePipSnapAlgorithmFactory implements Factory<PipSnapAlgorithm> {

    public static final class InstanceHolder {
        public static final WMShellModule_ProvidePipSnapAlgorithmFactory INSTANCE = new WMShellModule_ProvidePipSnapAlgorithmFactory();
    }

    public PipSnapAlgorithm get() {
        return providePipSnapAlgorithm();
    }

    public static WMShellModule_ProvidePipSnapAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSnapAlgorithm providePipSnapAlgorithm() {
        return (PipSnapAlgorithm) Preconditions.checkNotNullFromProvides(WMShellModule.providePipSnapAlgorithm());
    }
}
