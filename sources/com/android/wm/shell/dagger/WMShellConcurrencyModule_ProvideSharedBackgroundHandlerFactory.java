package com.android.wm.shell.dagger;

import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory implements Factory<Handler> {

    public static final class InstanceHolder {
        public static final WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory INSTANCE = new WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory();
    }

    public Handler get() {
        return provideSharedBackgroundHandler();
    }

    public static WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Handler provideSharedBackgroundHandler() {
        return (Handler) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideSharedBackgroundHandler());
    }
}
