package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvidesMediaTimeoutListenerLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvidesMediaTimeoutListenerLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return providesMediaTimeoutListenerLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvidesMediaTimeoutListenerLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvidesMediaTimeoutListenerLogBufferFactory(provider);
    }

    public static LogBuffer providesMediaTimeoutListenerLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.providesMediaTimeoutListenerLogBuffer(logBufferFactory));
    }
}
