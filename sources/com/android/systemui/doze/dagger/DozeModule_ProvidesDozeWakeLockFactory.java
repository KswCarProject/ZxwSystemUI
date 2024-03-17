package com.android.systemui.doze.dagger;

import android.os.Handler;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DozeModule_ProvidesDozeWakeLockFactory implements Factory<WakeLock> {
    public final Provider<DelayedWakeLock.Builder> delayedWakeLockBuilderProvider;
    public final Provider<Handler> handlerProvider;

    public DozeModule_ProvidesDozeWakeLockFactory(Provider<DelayedWakeLock.Builder> provider, Provider<Handler> provider2) {
        this.delayedWakeLockBuilderProvider = provider;
        this.handlerProvider = provider2;
    }

    public WakeLock get() {
        return providesDozeWakeLock(this.delayedWakeLockBuilderProvider.get(), this.handlerProvider.get());
    }

    public static DozeModule_ProvidesDozeWakeLockFactory create(Provider<DelayedWakeLock.Builder> provider, Provider<Handler> provider2) {
        return new DozeModule_ProvidesDozeWakeLockFactory(provider, provider2);
    }

    public static WakeLock providesDozeWakeLock(DelayedWakeLock.Builder builder, Handler handler) {
        return (WakeLock) Preconditions.checkNotNullFromProvides(DozeModule.providesDozeWakeLock(builder, handler));
    }
}
