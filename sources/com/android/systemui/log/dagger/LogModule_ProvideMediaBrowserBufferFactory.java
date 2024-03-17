package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideMediaBrowserBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideMediaBrowserBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideMediaBrowserBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideMediaBrowserBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideMediaBrowserBufferFactory(provider);
    }

    public static LogBuffer provideMediaBrowserBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideMediaBrowserBuffer(logBufferFactory));
    }
}
