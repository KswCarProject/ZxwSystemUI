package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideTransitionsFactory implements Factory<Transitions> {
    public final Provider<ShellExecutor> animExecutorProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<ShellTaskOrganizer> organizerProvider;
    public final Provider<TransactionPool> poolProvider;

    public WMShellBaseModule_ProvideTransitionsFactory(Provider<ShellTaskOrganizer> provider, Provider<TransactionPool> provider2, Provider<DisplayController> provider3, Provider<Context> provider4, Provider<ShellExecutor> provider5, Provider<Handler> provider6, Provider<ShellExecutor> provider7) {
        this.organizerProvider = provider;
        this.poolProvider = provider2;
        this.displayControllerProvider = provider3;
        this.contextProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.animExecutorProvider = provider7;
    }

    public Transitions get() {
        return provideTransitions(this.organizerProvider.get(), this.poolProvider.get(), this.displayControllerProvider.get(), this.contextProvider.get(), this.mainExecutorProvider.get(), this.mainHandlerProvider.get(), this.animExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideTransitionsFactory create(Provider<ShellTaskOrganizer> provider, Provider<TransactionPool> provider2, Provider<DisplayController> provider3, Provider<Context> provider4, Provider<ShellExecutor> provider5, Provider<Handler> provider6, Provider<ShellExecutor> provider7) {
        return new WMShellBaseModule_ProvideTransitionsFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static Transitions provideTransitions(ShellTaskOrganizer shellTaskOrganizer, TransactionPool transactionPool, DisplayController displayController, Context context, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2) {
        return (Transitions) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTransitions(shellTaskOrganizer, transactionPool, displayController, context, shellExecutor, handler, shellExecutor2));
    }
}
