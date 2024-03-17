package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.compatui.CompatUIController;
import com.android.wm.shell.transition.Transitions;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideCompatUIControllerFactory implements Factory<CompatUIController> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    public final Provider<DisplayImeController> imeControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<SyncTransactionQueue> syncQueueProvider;
    public final Provider<Transitions> transitionsLazyProvider;

    public WMShellBaseModule_ProvideCompatUIControllerFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<DisplayInsetsController> provider3, Provider<DisplayImeController> provider4, Provider<SyncTransactionQueue> provider5, Provider<ShellExecutor> provider6, Provider<Transitions> provider7) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.displayInsetsControllerProvider = provider3;
        this.imeControllerProvider = provider4;
        this.syncQueueProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.transitionsLazyProvider = provider7;
    }

    public CompatUIController get() {
        return provideCompatUIController(this.contextProvider.get(), this.displayControllerProvider.get(), this.displayInsetsControllerProvider.get(), this.imeControllerProvider.get(), this.syncQueueProvider.get(), this.mainExecutorProvider.get(), DoubleCheck.lazy(this.transitionsLazyProvider));
    }

    public static WMShellBaseModule_ProvideCompatUIControllerFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<DisplayInsetsController> provider3, Provider<DisplayImeController> provider4, Provider<SyncTransactionQueue> provider5, Provider<ShellExecutor> provider6, Provider<Transitions> provider7) {
        return new WMShellBaseModule_ProvideCompatUIControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static CompatUIController provideCompatUIController(Context context, DisplayController displayController, DisplayInsetsController displayInsetsController, DisplayImeController displayImeController, SyncTransactionQueue syncTransactionQueue, ShellExecutor shellExecutor, Lazy<Transitions> lazy) {
        return (CompatUIController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideCompatUIController(context, displayController, displayInsetsController, displayImeController, syncTransactionQueue, shellExecutor, lazy));
    }
}
