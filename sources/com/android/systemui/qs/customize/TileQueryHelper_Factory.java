package com.android.systemui.qs.customize;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class TileQueryHelper_Factory implements Factory<TileQueryHelper> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public TileQueryHelper_Factory(Provider<Context> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<Executor> provider4) {
        this.contextProvider = provider;
        this.userTrackerProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.bgExecutorProvider = provider4;
    }

    public TileQueryHelper get() {
        return newInstance(this.contextProvider.get(), this.userTrackerProvider.get(), this.mainExecutorProvider.get(), this.bgExecutorProvider.get());
    }

    public static TileQueryHelper_Factory create(Provider<Context> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<Executor> provider4) {
        return new TileQueryHelper_Factory(provider, provider2, provider3, provider4);
    }

    public static TileQueryHelper newInstance(Context context, UserTracker userTracker, Executor executor, Executor executor2) {
        return new TileQueryHelper(context, userTracker, executor, executor2);
    }
}
