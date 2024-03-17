package com.android.systemui.statusbar.phone;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarHideIconsForBouncerManager_Factory implements Factory<StatusBarHideIconsForBouncerManager> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<StatusBarWindowStateController> statusBarWindowStateControllerProvider;

    public StatusBarHideIconsForBouncerManager_Factory(Provider<CommandQueue> provider, Provider<DelayableExecutor> provider2, Provider<StatusBarWindowStateController> provider3, Provider<DumpManager> provider4) {
        this.commandQueueProvider = provider;
        this.mainExecutorProvider = provider2;
        this.statusBarWindowStateControllerProvider = provider3;
        this.dumpManagerProvider = provider4;
    }

    public StatusBarHideIconsForBouncerManager get() {
        return newInstance(this.commandQueueProvider.get(), this.mainExecutorProvider.get(), this.statusBarWindowStateControllerProvider.get(), this.dumpManagerProvider.get());
    }

    public static StatusBarHideIconsForBouncerManager_Factory create(Provider<CommandQueue> provider, Provider<DelayableExecutor> provider2, Provider<StatusBarWindowStateController> provider3, Provider<DumpManager> provider4) {
        return new StatusBarHideIconsForBouncerManager_Factory(provider, provider2, provider3, provider4);
    }

    public static StatusBarHideIconsForBouncerManager newInstance(CommandQueue commandQueue, DelayableExecutor delayableExecutor, StatusBarWindowStateController statusBarWindowStateController, DumpManager dumpManager) {
        return new StatusBarHideIconsForBouncerManager(commandQueue, delayableExecutor, statusBarWindowStateController, dumpManager);
    }
}
