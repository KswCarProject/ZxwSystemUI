package com.android.systemui.broadcast;

import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PendingRemovalStore_Factory implements Factory<PendingRemovalStore> {
    public final Provider<BroadcastDispatcherLogger> loggerProvider;

    public PendingRemovalStore_Factory(Provider<BroadcastDispatcherLogger> provider) {
        this.loggerProvider = provider;
    }

    public PendingRemovalStore get() {
        return newInstance(this.loggerProvider.get());
    }

    public static PendingRemovalStore_Factory create(Provider<BroadcastDispatcherLogger> provider) {
        return new PendingRemovalStore_Factory(provider);
    }

    public static PendingRemovalStore newInstance(BroadcastDispatcherLogger broadcastDispatcherLogger) {
        return new PendingRemovalStore(broadcastDispatcherLogger);
    }
}
