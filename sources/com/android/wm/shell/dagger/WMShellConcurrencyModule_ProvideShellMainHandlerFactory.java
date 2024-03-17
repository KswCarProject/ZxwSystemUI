package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellConcurrencyModule_ProvideShellMainHandlerFactory implements Factory<Handler> {
    public final Provider<Context> contextProvider;
    public final Provider<HandlerThread> mainThreadProvider;
    public final Provider<Handler> sysuiMainHandlerProvider;

    public WMShellConcurrencyModule_ProvideShellMainHandlerFactory(Provider<Context> provider, Provider<HandlerThread> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.mainThreadProvider = provider2;
        this.sysuiMainHandlerProvider = provider3;
    }

    public Handler get() {
        return provideShellMainHandler(this.contextProvider.get(), this.mainThreadProvider.get(), this.sysuiMainHandlerProvider.get());
    }

    public static WMShellConcurrencyModule_ProvideShellMainHandlerFactory create(Provider<Context> provider, Provider<HandlerThread> provider2, Provider<Handler> provider3) {
        return new WMShellConcurrencyModule_ProvideShellMainHandlerFactory(provider, provider2, provider3);
    }

    public static Handler provideShellMainHandler(Context context, HandlerThread handlerThread, Handler handler) {
        return (Handler) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideShellMainHandler(context, handlerThread, handler));
    }
}
