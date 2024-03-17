package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipParamsChangedForwarder;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class TvPipModule_ProvidePipParamsChangedForwarderFactory implements Factory<PipParamsChangedForwarder> {

    public static final class InstanceHolder {
        public static final TvPipModule_ProvidePipParamsChangedForwarderFactory INSTANCE = new TvPipModule_ProvidePipParamsChangedForwarderFactory();
    }

    public PipParamsChangedForwarder get() {
        return providePipParamsChangedForwarder();
    }

    public static TvPipModule_ProvidePipParamsChangedForwarderFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipParamsChangedForwarder providePipParamsChangedForwarder() {
        return (PipParamsChangedForwarder) Preconditions.checkNotNullFromProvides(TvPipModule.providePipParamsChangedForwarder());
    }
}
