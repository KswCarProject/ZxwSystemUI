package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.pip.PipMediaController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvidePipMediaControllerFactory implements Factory<PipMediaController> {
    public final Provider<Context> contextProvider;
    public final Provider<Handler> mainHandlerProvider;

    public WMShellBaseModule_ProvidePipMediaControllerFactory(Provider<Context> provider, Provider<Handler> provider2) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
    }

    public PipMediaController get() {
        return providePipMediaController(this.contextProvider.get(), this.mainHandlerProvider.get());
    }

    public static WMShellBaseModule_ProvidePipMediaControllerFactory create(Provider<Context> provider, Provider<Handler> provider2) {
        return new WMShellBaseModule_ProvidePipMediaControllerFactory(provider, provider2);
    }

    public static PipMediaController providePipMediaController(Context context, Handler handler) {
        return (PipMediaController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.providePipMediaController(context, handler));
    }
}
