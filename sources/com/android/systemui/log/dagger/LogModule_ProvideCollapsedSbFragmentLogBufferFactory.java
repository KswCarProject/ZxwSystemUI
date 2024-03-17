package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideCollapsedSbFragmentLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideCollapsedSbFragmentLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideCollapsedSbFragmentLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideCollapsedSbFragmentLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideCollapsedSbFragmentLogBufferFactory(provider);
    }

    public static LogBuffer provideCollapsedSbFragmentLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideCollapsedSbFragmentLogBuffer(logBufferFactory));
    }
}
