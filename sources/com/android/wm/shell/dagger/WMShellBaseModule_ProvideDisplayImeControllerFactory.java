package com.android.wm.shell.dagger;

import android.view.IWindowManager;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideDisplayImeControllerFactory implements Factory<DisplayImeController> {
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<DisplayImeController>> overrideDisplayImeControllerProvider;
    public final Provider<TransactionPool> transactionPoolProvider;
    public final Provider<IWindowManager> wmServiceProvider;

    public WMShellBaseModule_ProvideDisplayImeControllerFactory(Provider<Optional<DisplayImeController>> provider, Provider<IWindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayInsetsController> provider4, Provider<ShellExecutor> provider5, Provider<TransactionPool> provider6) {
        this.overrideDisplayImeControllerProvider = provider;
        this.wmServiceProvider = provider2;
        this.displayControllerProvider = provider3;
        this.displayInsetsControllerProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.transactionPoolProvider = provider6;
    }

    public DisplayImeController get() {
        return provideDisplayImeController(this.overrideDisplayImeControllerProvider.get(), this.wmServiceProvider.get(), this.displayControllerProvider.get(), this.displayInsetsControllerProvider.get(), this.mainExecutorProvider.get(), this.transactionPoolProvider.get());
    }

    public static WMShellBaseModule_ProvideDisplayImeControllerFactory create(Provider<Optional<DisplayImeController>> provider, Provider<IWindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayInsetsController> provider4, Provider<ShellExecutor> provider5, Provider<TransactionPool> provider6) {
        return new WMShellBaseModule_ProvideDisplayImeControllerFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static DisplayImeController provideDisplayImeController(Optional<DisplayImeController> optional, IWindowManager iWindowManager, DisplayController displayController, DisplayInsetsController displayInsetsController, ShellExecutor shellExecutor, TransactionPool transactionPool) {
        return (DisplayImeController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDisplayImeController(optional, iWindowManager, displayController, displayInsetsController, shellExecutor, transactionPool));
    }
}
