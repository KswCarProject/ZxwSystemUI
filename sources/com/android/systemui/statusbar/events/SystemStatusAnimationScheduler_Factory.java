package com.android.systemui.statusbar.events;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SystemStatusAnimationScheduler_Factory implements Factory<SystemStatusAnimationScheduler> {
    public final Provider<SystemEventChipAnimationController> chipAnimationControllerProvider;
    public final Provider<SystemEventCoordinator> coordinatorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> executorProvider;
    public final Provider<StatusBarWindowController> statusBarWindowControllerProvider;
    public final Provider<SystemClock> systemClockProvider;

    public SystemStatusAnimationScheduler_Factory(Provider<SystemEventCoordinator> provider, Provider<SystemEventChipAnimationController> provider2, Provider<StatusBarWindowController> provider3, Provider<DumpManager> provider4, Provider<SystemClock> provider5, Provider<DelayableExecutor> provider6) {
        this.coordinatorProvider = provider;
        this.chipAnimationControllerProvider = provider2;
        this.statusBarWindowControllerProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.systemClockProvider = provider5;
        this.executorProvider = provider6;
    }

    public SystemStatusAnimationScheduler get() {
        return newInstance(this.coordinatorProvider.get(), this.chipAnimationControllerProvider.get(), this.statusBarWindowControllerProvider.get(), this.dumpManagerProvider.get(), this.systemClockProvider.get(), this.executorProvider.get());
    }

    public static SystemStatusAnimationScheduler_Factory create(Provider<SystemEventCoordinator> provider, Provider<SystemEventChipAnimationController> provider2, Provider<StatusBarWindowController> provider3, Provider<DumpManager> provider4, Provider<SystemClock> provider5, Provider<DelayableExecutor> provider6) {
        return new SystemStatusAnimationScheduler_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static SystemStatusAnimationScheduler newInstance(SystemEventCoordinator systemEventCoordinator, SystemEventChipAnimationController systemEventChipAnimationController, StatusBarWindowController statusBarWindowController, DumpManager dumpManager, SystemClock systemClock, DelayableExecutor delayableExecutor) {
        return new SystemStatusAnimationScheduler(systemEventCoordinator, systemEventChipAnimationController, statusBarWindowController, dumpManager, systemClock, delayableExecutor);
    }
}
