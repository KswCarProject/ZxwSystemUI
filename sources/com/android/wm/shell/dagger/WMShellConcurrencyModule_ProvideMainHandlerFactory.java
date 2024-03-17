package com.android.wm.shell.dagger;

import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class WMShellConcurrencyModule_ProvideMainHandlerFactory implements Factory<Handler> {

    public static final class InstanceHolder {
        public static final WMShellConcurrencyModule_ProvideMainHandlerFactory INSTANCE = new WMShellConcurrencyModule_ProvideMainHandlerFactory();
    }

    public Handler get() {
        return provideMainHandler();
    }

    public static WMShellConcurrencyModule_ProvideMainHandlerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Handler provideMainHandler() {
        return (Handler) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideMainHandler());
    }
}
