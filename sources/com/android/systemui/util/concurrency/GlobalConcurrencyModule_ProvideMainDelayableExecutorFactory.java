package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory implements Factory<DelayableExecutor> {
    public final Provider<Looper> looperProvider;

    public GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory(Provider<Looper> provider) {
        this.looperProvider = provider;
    }

    public DelayableExecutor get() {
        return provideMainDelayableExecutor(this.looperProvider.get());
    }

    public static GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory create(Provider<Looper> provider) {
        return new GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory(provider);
    }

    public static DelayableExecutor provideMainDelayableExecutor(Looper looper) {
        return (DelayableExecutor) Preconditions.checkNotNullFromProvides(GlobalConcurrencyModule.provideMainDelayableExecutor(looper));
    }
}
