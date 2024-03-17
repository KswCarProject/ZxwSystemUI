package com.android.systemui.broadcast;

import android.content.Context;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class BroadcastSender_Factory implements Factory<BroadcastSender> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Context> contextProvider;
    public final Provider<WakeLock.Builder> wakeLockBuilderProvider;

    public BroadcastSender_Factory(Provider<Context> provider, Provider<WakeLock.Builder> provider2, Provider<Executor> provider3) {
        this.contextProvider = provider;
        this.wakeLockBuilderProvider = provider2;
        this.bgExecutorProvider = provider3;
    }

    public BroadcastSender get() {
        return newInstance(this.contextProvider.get(), this.wakeLockBuilderProvider.get(), this.bgExecutorProvider.get());
    }

    public static BroadcastSender_Factory create(Provider<Context> provider, Provider<WakeLock.Builder> provider2, Provider<Executor> provider3) {
        return new BroadcastSender_Factory(provider, provider2, provider3);
    }

    public static BroadcastSender newInstance(Context context, WakeLock.Builder builder, Executor executor) {
        return new BroadcastSender(context, builder, executor);
    }
}
