package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ManagedProfileControllerImpl_Factory implements Factory<ManagedProfileControllerImpl> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;

    public ManagedProfileControllerImpl_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    public ManagedProfileControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static ManagedProfileControllerImpl_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        return new ManagedProfileControllerImpl_Factory(provider, provider2);
    }

    public static ManagedProfileControllerImpl newInstance(Context context, BroadcastDispatcher broadcastDispatcher) {
        return new ManagedProfileControllerImpl(context, broadcastDispatcher);
    }
}
