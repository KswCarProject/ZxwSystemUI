package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideLSShadeTransitionControllerBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideLSShadeTransitionControllerBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideLSShadeTransitionControllerBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideLSShadeTransitionControllerBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideLSShadeTransitionControllerBufferFactory(provider);
    }

    public static LogBuffer provideLSShadeTransitionControllerBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideLSShadeTransitionControllerBuffer(logBufferFactory));
    }
}
