package com.android.systemui;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SliceBroadcastRelayHandler_Factory implements Factory<SliceBroadcastRelayHandler> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;

    public SliceBroadcastRelayHandler_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    public SliceBroadcastRelayHandler get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static SliceBroadcastRelayHandler_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        return new SliceBroadcastRelayHandler_Factory(provider, provider2);
    }

    public static SliceBroadcastRelayHandler newInstance(Context context, BroadcastDispatcher broadcastDispatcher) {
        return new SliceBroadcastRelayHandler(context, broadcastDispatcher);
    }
}
