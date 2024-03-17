package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.back.BackAnimationController;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideBackAnimationControllerFactory implements Factory<Optional<BackAnimationController>> {
    public final Provider<Handler> backgroundHandlerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ShellExecutor> shellExecutorProvider;

    public WMShellBaseModule_ProvideBackAnimationControllerFactory(Provider<Context> provider, Provider<ShellExecutor> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.shellExecutorProvider = provider2;
        this.backgroundHandlerProvider = provider3;
    }

    public Optional<BackAnimationController> get() {
        return provideBackAnimationController(this.contextProvider.get(), this.shellExecutorProvider.get(), this.backgroundHandlerProvider.get());
    }

    public static WMShellBaseModule_ProvideBackAnimationControllerFactory create(Provider<Context> provider, Provider<ShellExecutor> provider2, Provider<Handler> provider3) {
        return new WMShellBaseModule_ProvideBackAnimationControllerFactory(provider, provider2, provider3);
    }

    public static Optional<BackAnimationController> provideBackAnimationController(Context context, ShellExecutor shellExecutor, Handler handler) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideBackAnimationController(context, shellExecutor, handler));
    }
}
