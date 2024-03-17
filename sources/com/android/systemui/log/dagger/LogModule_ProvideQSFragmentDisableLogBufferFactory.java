package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideQSFragmentDisableLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideQSFragmentDisableLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideQSFragmentDisableLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideQSFragmentDisableLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideQSFragmentDisableLogBufferFactory(provider);
    }

    public static LogBuffer provideQSFragmentDisableLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideQSFragmentDisableLogBuffer(logBufferFactory));
    }
}
