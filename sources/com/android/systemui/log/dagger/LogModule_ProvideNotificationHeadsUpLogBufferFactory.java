package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideNotificationHeadsUpLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideNotificationHeadsUpLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideNotificationHeadsUpLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideNotificationHeadsUpLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideNotificationHeadsUpLogBufferFactory(provider);
    }

    public static LogBuffer provideNotificationHeadsUpLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideNotificationHeadsUpLogBuffer(logBufferFactory));
    }
}
