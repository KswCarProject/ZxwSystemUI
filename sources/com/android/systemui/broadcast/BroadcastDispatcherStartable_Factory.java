package com.android.systemui.broadcast;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BroadcastDispatcherStartable_Factory implements Factory<BroadcastDispatcherStartable> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;

    public BroadcastDispatcherStartable_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    public BroadcastDispatcherStartable get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static BroadcastDispatcherStartable_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        return new BroadcastDispatcherStartable_Factory(provider, provider2);
    }

    public static BroadcastDispatcherStartable newInstance(Context context, BroadcastDispatcher broadcastDispatcher) {
        return new BroadcastDispatcherStartable(context, broadcastDispatcher);
    }
}
