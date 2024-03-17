package com.android.wm.shell.dagger;

import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.recents.RecentTasksController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideRecentTasksFactory implements Factory<Optional<RecentTasks>> {
    public final Provider<Optional<RecentTasksController>> recentTasksControllerProvider;

    public WMShellBaseModule_ProvideRecentTasksFactory(Provider<Optional<RecentTasksController>> provider) {
        this.recentTasksControllerProvider = provider;
    }

    public Optional<RecentTasks> get() {
        return provideRecentTasks(this.recentTasksControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideRecentTasksFactory create(Provider<Optional<RecentTasksController>> provider) {
        return new WMShellBaseModule_ProvideRecentTasksFactory(provider);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.util.Optional<com.android.wm.shell.recents.RecentTasksController>, java.util.Optional] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.wm.shell.recents.RecentTasks> provideRecentTasks(java.util.Optional<com.android.wm.shell.recents.RecentTasksController> r0) {
        /*
            java.util.Optional r0 = com.android.wm.shell.dagger.WMShellBaseModule.provideRecentTasks(r0)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.dagger.WMShellBaseModule_ProvideRecentTasksFactory.provideRecentTasks(java.util.Optional):java.util.Optional");
    }
}
