package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory implements Factory<PipSurfaceTransactionHelper> {

    public static final class InstanceHolder {
        public static final WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory INSTANCE = new WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory();
    }

    public PipSurfaceTransactionHelper get() {
        return providePipSurfaceTransactionHelper();
    }

    public static WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSurfaceTransactionHelper providePipSurfaceTransactionHelper() {
        return (PipSurfaceTransactionHelper) Preconditions.checkNotNullFromProvides(WMShellBaseModule.providePipSurfaceTransactionHelper());
    }
}
