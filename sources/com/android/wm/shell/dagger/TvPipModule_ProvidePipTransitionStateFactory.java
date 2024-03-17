package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipTransitionState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class TvPipModule_ProvidePipTransitionStateFactory implements Factory<PipTransitionState> {

    public static final class InstanceHolder {
        public static final TvPipModule_ProvidePipTransitionStateFactory INSTANCE = new TvPipModule_ProvidePipTransitionStateFactory();
    }

    public PipTransitionState get() {
        return providePipTransitionState();
    }

    public static TvPipModule_ProvidePipTransitionStateFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipTransitionState providePipTransitionState() {
        return (PipTransitionState) Preconditions.checkNotNullFromProvides(TvPipModule.providePipTransitionState());
    }
}
