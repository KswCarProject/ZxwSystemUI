package com.android.systemui.util.concurrency;

import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class SysUIConcurrencyModule_ProvideTimeTickHandlerFactory implements Factory<Handler> {

    public static final class InstanceHolder {
        public static final SysUIConcurrencyModule_ProvideTimeTickHandlerFactory INSTANCE = new SysUIConcurrencyModule_ProvideTimeTickHandlerFactory();
    }

    public Handler get() {
        return provideTimeTickHandler();
    }

    public static SysUIConcurrencyModule_ProvideTimeTickHandlerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Handler provideTimeTickHandler() {
        return (Handler) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideTimeTickHandler());
    }
}
