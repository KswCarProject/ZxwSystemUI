package com.android.systemui.broadcast;

import android.content.Context;
import android.os.Looper;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class BroadcastDispatcher_Factory implements Factory<BroadcastDispatcher> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Looper> bgLooperProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<BroadcastDispatcherLogger> loggerProvider;
    public final Provider<PendingRemovalStore> removalPendingStoreProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public BroadcastDispatcher_Factory(Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<DumpManager> provider4, Provider<BroadcastDispatcherLogger> provider5, Provider<UserTracker> provider6, Provider<PendingRemovalStore> provider7) {
        this.contextProvider = provider;
        this.bgLooperProvider = provider2;
        this.bgExecutorProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.loggerProvider = provider5;
        this.userTrackerProvider = provider6;
        this.removalPendingStoreProvider = provider7;
    }

    public BroadcastDispatcher get() {
        return newInstance(this.contextProvider.get(), this.bgLooperProvider.get(), this.bgExecutorProvider.get(), this.dumpManagerProvider.get(), this.loggerProvider.get(), this.userTrackerProvider.get(), this.removalPendingStoreProvider.get());
    }

    public static BroadcastDispatcher_Factory create(Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<DumpManager> provider4, Provider<BroadcastDispatcherLogger> provider5, Provider<UserTracker> provider6, Provider<PendingRemovalStore> provider7) {
        return new BroadcastDispatcher_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static BroadcastDispatcher newInstance(Context context, Looper looper, Executor executor, DumpManager dumpManager, BroadcastDispatcherLogger broadcastDispatcherLogger, UserTracker userTracker, PendingRemovalStore pendingRemovalStore) {
        return new BroadcastDispatcher(context, looper, executor, dumpManager, broadcastDispatcherLogger, userTracker, pendingRemovalStore);
    }
}
