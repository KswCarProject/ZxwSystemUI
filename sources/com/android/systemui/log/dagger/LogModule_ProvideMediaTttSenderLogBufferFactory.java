package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideMediaTttSenderLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideMediaTttSenderLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideMediaTttSenderLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideMediaTttSenderLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideMediaTttSenderLogBufferFactory(provider);
    }

    public static LogBuffer provideMediaTttSenderLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideMediaTttSenderLogBuffer(logBufferFactory));
    }
}
