package com.android.systemui.statusbar.notification.collection.coordinator;

import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class ShadeEventCoordinator_Factory implements Factory<ShadeEventCoordinator> {
    public final Provider<ShadeEventCoordinatorLogger> mLoggerProvider;
    public final Provider<Executor> mMainExecutorProvider;

    public ShadeEventCoordinator_Factory(Provider<Executor> provider, Provider<ShadeEventCoordinatorLogger> provider2) {
        this.mMainExecutorProvider = provider;
        this.mLoggerProvider = provider2;
    }

    public ShadeEventCoordinator get() {
        return newInstance(this.mMainExecutorProvider.get(), this.mLoggerProvider.get());
    }

    public static ShadeEventCoordinator_Factory create(Provider<Executor> provider, Provider<ShadeEventCoordinatorLogger> provider2) {
        return new ShadeEventCoordinator_Factory(provider, provider2);
    }

    public static ShadeEventCoordinator newInstance(Executor executor, ShadeEventCoordinatorLogger shadeEventCoordinatorLogger) {
        return new ShadeEventCoordinator(executor, shadeEventCoordinatorLogger);
    }
}
