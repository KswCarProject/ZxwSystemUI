package com.android.wm.shell.dagger;

import android.animation.AnimationHandler;
import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellModule_ProvideLegacySplitScreenFactory implements Factory<LegacySplitScreenController> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<DisplayImeController> displayImeControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<AnimationHandler> sfVsyncAnimationHandlerProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<SyncTransactionQueue> syncQueueProvider;
    public final Provider<SystemWindows> systemWindowsProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    public final Provider<TransactionPool> transactionPoolProvider;
    public final Provider<Transitions> transitionsProvider;

    public WMShellModule_ProvideLegacySplitScreenFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<SystemWindows> provider3, Provider<DisplayImeController> provider4, Provider<TransactionPool> provider5, Provider<ShellTaskOrganizer> provider6, Provider<SyncTransactionQueue> provider7, Provider<TaskStackListenerImpl> provider8, Provider<Transitions> provider9, Provider<ShellExecutor> provider10, Provider<AnimationHandler> provider11) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.systemWindowsProvider = provider3;
        this.displayImeControllerProvider = provider4;
        this.transactionPoolProvider = provider5;
        this.shellTaskOrganizerProvider = provider6;
        this.syncQueueProvider = provider7;
        this.taskStackListenerProvider = provider8;
        this.transitionsProvider = provider9;
        this.mainExecutorProvider = provider10;
        this.sfVsyncAnimationHandlerProvider = provider11;
    }

    public LegacySplitScreenController get() {
        return provideLegacySplitScreen(this.contextProvider.get(), this.displayControllerProvider.get(), this.systemWindowsProvider.get(), this.displayImeControllerProvider.get(), this.transactionPoolProvider.get(), this.shellTaskOrganizerProvider.get(), this.syncQueueProvider.get(), this.taskStackListenerProvider.get(), this.transitionsProvider.get(), this.mainExecutorProvider.get(), this.sfVsyncAnimationHandlerProvider.get());
    }

    public static WMShellModule_ProvideLegacySplitScreenFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<SystemWindows> provider3, Provider<DisplayImeController> provider4, Provider<TransactionPool> provider5, Provider<ShellTaskOrganizer> provider6, Provider<SyncTransactionQueue> provider7, Provider<TaskStackListenerImpl> provider8, Provider<Transitions> provider9, Provider<ShellExecutor> provider10, Provider<AnimationHandler> provider11) {
        return new WMShellModule_ProvideLegacySplitScreenFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static LegacySplitScreenController provideLegacySplitScreen(Context context, DisplayController displayController, SystemWindows systemWindows, DisplayImeController displayImeController, TransactionPool transactionPool, ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, TaskStackListenerImpl taskStackListenerImpl, Transitions transitions, ShellExecutor shellExecutor, AnimationHandler animationHandler) {
        return (LegacySplitScreenController) Preconditions.checkNotNullFromProvides(WMShellModule.provideLegacySplitScreen(context, displayController, systemWindows, displayImeController, transactionPool, shellTaskOrganizer, syncTransactionQueue, taskStackListenerImpl, transitions, shellExecutor, animationHandler));
    }
}
