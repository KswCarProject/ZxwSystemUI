package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.wm.shell.recents.RecentTasksController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory implements Factory<KidsModeTaskOrganizer> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<Optional<RecentTasksController>> recentTasksOptionalProvider;
    public final Provider<SyncTransactionQueue> syncTransactionQueueProvider;

    public WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory(Provider<ShellExecutor> provider, Provider<Handler> provider2, Provider<Context> provider3, Provider<SyncTransactionQueue> provider4, Provider<DisplayController> provider5, Provider<DisplayInsetsController> provider6, Provider<Optional<RecentTasksController>> provider7) {
        this.mainExecutorProvider = provider;
        this.mainHandlerProvider = provider2;
        this.contextProvider = provider3;
        this.syncTransactionQueueProvider = provider4;
        this.displayControllerProvider = provider5;
        this.displayInsetsControllerProvider = provider6;
        this.recentTasksOptionalProvider = provider7;
    }

    public KidsModeTaskOrganizer get() {
        return provideKidsModeTaskOrganizer(this.mainExecutorProvider.get(), this.mainHandlerProvider.get(), this.contextProvider.get(), this.syncTransactionQueueProvider.get(), this.displayControllerProvider.get(), this.displayInsetsControllerProvider.get(), this.recentTasksOptionalProvider.get());
    }

    public static WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory create(Provider<ShellExecutor> provider, Provider<Handler> provider2, Provider<Context> provider3, Provider<SyncTransactionQueue> provider4, Provider<DisplayController> provider5, Provider<DisplayInsetsController> provider6, Provider<Optional<RecentTasksController>> provider7) {
        return new WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static KidsModeTaskOrganizer provideKidsModeTaskOrganizer(ShellExecutor shellExecutor, Handler handler, Context context, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, DisplayInsetsController displayInsetsController, Optional<RecentTasksController> optional) {
        return (KidsModeTaskOrganizer) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideKidsModeTaskOrganizer(shellExecutor, handler, context, syncTransactionQueue, displayController, displayInsetsController, optional));
    }
}
