package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.recents.RecentTasksController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideRecentTasksControllerFactory implements Factory<Optional<RecentTasksController>> {
    public final Provider<Context> contextProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;

    public WMShellBaseModule_ProvideRecentTasksControllerFactory(Provider<Context> provider, Provider<TaskStackListenerImpl> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.taskStackListenerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    public Optional<RecentTasksController> get() {
        return provideRecentTasksController(this.contextProvider.get(), this.taskStackListenerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideRecentTasksControllerFactory create(Provider<Context> provider, Provider<TaskStackListenerImpl> provider2, Provider<ShellExecutor> provider3) {
        return new WMShellBaseModule_ProvideRecentTasksControllerFactory(provider, provider2, provider3);
    }

    public static Optional<RecentTasksController> provideRecentTasksController(Context context, TaskStackListenerImpl taskStackListenerImpl, ShellExecutor shellExecutor) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideRecentTasksController(context, taskStackListenerImpl, shellExecutor));
    }
}
