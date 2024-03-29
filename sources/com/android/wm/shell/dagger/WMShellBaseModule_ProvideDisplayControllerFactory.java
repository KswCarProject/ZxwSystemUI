package com.android.wm.shell.dagger;

import android.content.Context;
import android.view.IWindowManager;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideDisplayControllerFactory implements Factory<DisplayController> {
    public final Provider<Context> contextProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<IWindowManager> wmServiceProvider;

    public WMShellBaseModule_ProvideDisplayControllerFactory(Provider<Context> provider, Provider<IWindowManager> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.wmServiceProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    public DisplayController get() {
        return provideDisplayController(this.contextProvider.get(), this.wmServiceProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideDisplayControllerFactory create(Provider<Context> provider, Provider<IWindowManager> provider2, Provider<ShellExecutor> provider3) {
        return new WMShellBaseModule_ProvideDisplayControllerFactory(provider, provider2, provider3);
    }

    public static DisplayController provideDisplayController(Context context, IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        return (DisplayController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDisplayController(context, iWindowManager, shellExecutor));
    }
}
