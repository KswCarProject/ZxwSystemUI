package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class SecurityControllerImpl_Factory implements Factory<SecurityControllerImpl> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;

    public SecurityControllerImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<BroadcastDispatcher> provider3, Provider<Executor> provider4, Provider<DumpManager> provider5) {
        this.contextProvider = provider;
        this.bgHandlerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.bgExecutorProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    public SecurityControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.bgHandlerProvider.get(), this.broadcastDispatcherProvider.get(), this.bgExecutorProvider.get(), this.dumpManagerProvider.get());
    }

    public static SecurityControllerImpl_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<BroadcastDispatcher> provider3, Provider<Executor> provider4, Provider<DumpManager> provider5) {
        return new SecurityControllerImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static SecurityControllerImpl newInstance(Context context, Handler handler, BroadcastDispatcher broadcastDispatcher, Executor executor, DumpManager dumpManager) {
        return new SecurityControllerImpl(context, handler, broadcastDispatcher, executor, dumpManager);
    }
}
