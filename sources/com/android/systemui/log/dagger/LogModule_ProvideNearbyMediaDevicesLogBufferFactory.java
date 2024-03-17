package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideNearbyMediaDevicesLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideNearbyMediaDevicesLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideNearbyMediaDevicesLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideNearbyMediaDevicesLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideNearbyMediaDevicesLogBufferFactory(provider);
    }

    public static LogBuffer provideNearbyMediaDevicesLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideNearbyMediaDevicesLogBuffer(logBufferFactory));
    }
}
