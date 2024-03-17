package com.android.wm.shell.dagger;

import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewFactoryController;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideTaskViewFactoryControllerFactory implements Factory<TaskViewFactoryController> {
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<SyncTransactionQueue> syncQueueProvider;
    public final Provider<TaskViewTransitions> taskViewTransitionsProvider;

    public WMShellBaseModule_ProvideTaskViewFactoryControllerFactory(Provider<ShellTaskOrganizer> provider, Provider<ShellExecutor> provider2, Provider<SyncTransactionQueue> provider3, Provider<TaskViewTransitions> provider4) {
        this.shellTaskOrganizerProvider = provider;
        this.mainExecutorProvider = provider2;
        this.syncQueueProvider = provider3;
        this.taskViewTransitionsProvider = provider4;
    }

    public TaskViewFactoryController get() {
        return provideTaskViewFactoryController(this.shellTaskOrganizerProvider.get(), this.mainExecutorProvider.get(), this.syncQueueProvider.get(), this.taskViewTransitionsProvider.get());
    }

    public static WMShellBaseModule_ProvideTaskViewFactoryControllerFactory create(Provider<ShellTaskOrganizer> provider, Provider<ShellExecutor> provider2, Provider<SyncTransactionQueue> provider3, Provider<TaskViewTransitions> provider4) {
        return new WMShellBaseModule_ProvideTaskViewFactoryControllerFactory(provider, provider2, provider3, provider4);
    }

    public static TaskViewFactoryController provideTaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor, SyncTransactionQueue syncTransactionQueue, TaskViewTransitions taskViewTransitions) {
        return (TaskViewFactoryController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTaskViewFactoryController(shellTaskOrganizer, shellExecutor, syncTransactionQueue, taskViewTransitions));
    }
}
