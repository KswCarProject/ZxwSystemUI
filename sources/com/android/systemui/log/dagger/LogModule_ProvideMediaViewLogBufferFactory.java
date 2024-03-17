package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideMediaViewLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideMediaViewLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideMediaViewLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideMediaViewLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideMediaViewLogBufferFactory(provider);
    }

    public static LogBuffer provideMediaViewLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideMediaViewLogBuffer(logBufferFactory));
    }
}
