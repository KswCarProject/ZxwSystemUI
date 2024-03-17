package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class InstantAppNotifier_Factory implements Factory<InstantAppNotifier> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Executor> uiBgExecutorProvider;

    public InstantAppNotifier_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<Executor> provider3) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.uiBgExecutorProvider = provider3;
    }

    public InstantAppNotifier get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), this.uiBgExecutorProvider.get());
    }

    public static InstantAppNotifier_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<Executor> provider3) {
        return new InstantAppNotifier_Factory(provider, provider2, provider3);
    }

    public static InstantAppNotifier newInstance(Context context, CommandQueue commandQueue, Executor executor) {
        return new InstantAppNotifier(context, commandQueue, executor);
    }
}
