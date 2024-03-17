package com.android.wm.shell.dagger;

import android.os.Handler;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory implements Factory<ShellExecutor> {
    public final Provider<Handler> handlerProvider;

    public WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory(Provider<Handler> provider) {
        this.handlerProvider = provider;
    }

    public ShellExecutor get() {
        return provideSharedBackgroundExecutor(this.handlerProvider.get());
    }

    public static WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory create(Provider<Handler> provider) {
        return new WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory(provider);
    }

    public static ShellExecutor provideSharedBackgroundExecutor(Handler handler) {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideSharedBackgroundExecutor(handler));
    }
}
