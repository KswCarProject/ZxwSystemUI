package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SysUIConcurrencyModule_ProvidesMainMessageRouterFactory implements Factory<MessageRouter> {
    public final Provider<DelayableExecutor> executorProvider;

    public SysUIConcurrencyModule_ProvidesMainMessageRouterFactory(Provider<DelayableExecutor> provider) {
        this.executorProvider = provider;
    }

    public MessageRouter get() {
        return providesMainMessageRouter(this.executorProvider.get());
    }

    public static SysUIConcurrencyModule_ProvidesMainMessageRouterFactory create(Provider<DelayableExecutor> provider) {
        return new SysUIConcurrencyModule_ProvidesMainMessageRouterFactory(provider);
    }

    public static MessageRouter providesMainMessageRouter(DelayableExecutor delayableExecutor) {
        return (MessageRouter) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.providesMainMessageRouter(delayableExecutor));
    }
}
