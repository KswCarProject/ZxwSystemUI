package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;

public final class GlobalConcurrencyModule_ProvideUiBackgroundExecutorFactory implements Factory<Executor> {

    public static final class InstanceHolder {
        public static final GlobalConcurrencyModule_ProvideUiBackgroundExecutorFactory INSTANCE = new GlobalConcurrencyModule_ProvideUiBackgroundExecutorFactory();
    }

    public Executor get() {
        return provideUiBackgroundExecutor();
    }

    public static GlobalConcurrencyModule_ProvideUiBackgroundExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Executor provideUiBackgroundExecutor() {
        return (Executor) Preconditions.checkNotNullFromProvides(GlobalConcurrencyModule.provideUiBackgroundExecutor());
    }
}
