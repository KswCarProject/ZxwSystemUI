package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideMediaTttReceiverLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideMediaTttReceiverLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideMediaTttReceiverLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideMediaTttReceiverLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideMediaTttReceiverLogBufferFactory(provider);
    }

    public static LogBuffer provideMediaTttReceiverLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideMediaTttReceiverLogBuffer(logBufferFactory));
    }
}
