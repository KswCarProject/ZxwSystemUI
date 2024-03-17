package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory implements Factory<MessageRouter> {
    public final Provider<DelayableExecutor> executorProvider;

    public SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory(Provider<DelayableExecutor> provider) {
        this.executorProvider = provider;
    }

    public MessageRouter get() {
        return providesBackgroundMessageRouter(this.executorProvider.get());
    }

    public static SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory create(Provider<DelayableExecutor> provider) {
        return new SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory(provider);
    }

    public static MessageRouter providesBackgroundMessageRouter(DelayableExecutor delayableExecutor) {
        return (MessageRouter) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.providesBackgroundMessageRouter(delayableExecutor));
    }
}
