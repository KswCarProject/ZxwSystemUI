package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipTransitionState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class WMShellModule_ProvidePipTransitionStateFactory implements Factory<PipTransitionState> {

    public static final class InstanceHolder {
        public static final WMShellModule_ProvidePipTransitionStateFactory INSTANCE = new WMShellModule_ProvidePipTransitionStateFactory();
    }

    public PipTransitionState get() {
        return providePipTransitionState();
    }

    public static WMShellModule_ProvidePipTransitionStateFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipTransitionState providePipTransitionState() {
        return (PipTransitionState) Preconditions.checkNotNullFromProvides(WMShellModule.providePipTransitionState());
    }
}
