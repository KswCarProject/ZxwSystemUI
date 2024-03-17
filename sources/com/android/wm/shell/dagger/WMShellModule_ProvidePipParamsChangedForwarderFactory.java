package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipParamsChangedForwarder;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class WMShellModule_ProvidePipParamsChangedForwarderFactory implements Factory<PipParamsChangedForwarder> {

    public static final class InstanceHolder {
        public static final WMShellModule_ProvidePipParamsChangedForwarderFactory INSTANCE = new WMShellModule_ProvidePipParamsChangedForwarderFactory();
    }

    public PipParamsChangedForwarder get() {
        return providePipParamsChangedForwarder();
    }

    public static WMShellModule_ProvidePipParamsChangedForwarderFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipParamsChangedForwarder providePipParamsChangedForwarder() {
        return (PipParamsChangedForwarder) Preconditions.checkNotNullFromProvides(WMShellModule.providePipParamsChangedForwarder());
    }
}
