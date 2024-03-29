package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideNotificationSectionLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideNotificationSectionLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideNotificationSectionLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideNotificationSectionLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideNotificationSectionLogBufferFactory(provider);
    }

    public static LogBuffer provideNotificationSectionLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideNotificationSectionLogBuffer(logBufferFactory));
    }
}
