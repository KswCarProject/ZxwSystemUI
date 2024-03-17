package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideMediaMuteAwaitLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideMediaMuteAwaitLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideMediaMuteAwaitLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideMediaMuteAwaitLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideMediaMuteAwaitLogBufferFactory(provider);
    }

    public static LogBuffer provideMediaMuteAwaitLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideMediaMuteAwaitLogBuffer(logBufferFactory));
    }
}
