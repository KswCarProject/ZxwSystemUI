package com.android.systemui.statusbar.window;

import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarWindowStateController_Factory implements Factory<StatusBarWindowStateController> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Integer> thisDisplayIdProvider;

    public StatusBarWindowStateController_Factory(Provider<Integer> provider, Provider<CommandQueue> provider2) {
        this.thisDisplayIdProvider = provider;
        this.commandQueueProvider = provider2;
    }

    public StatusBarWindowStateController get() {
        return newInstance(this.thisDisplayIdProvider.get().intValue(), this.commandQueueProvider.get());
    }

    public static StatusBarWindowStateController_Factory create(Provider<Integer> provider, Provider<CommandQueue> provider2) {
        return new StatusBarWindowStateController_Factory(provider, provider2);
    }

    public static StatusBarWindowStateController newInstance(int i, CommandQueue commandQueue) {
        return new StatusBarWindowStateController(i, commandQueue);
    }
}
