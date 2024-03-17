package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideMediaCarouselControllerBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideMediaCarouselControllerBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideMediaCarouselControllerBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideMediaCarouselControllerBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideMediaCarouselControllerBufferFactory(provider);
    }

    public static LogBuffer provideMediaCarouselControllerBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideMediaCarouselControllerBuffer(logBufferFactory));
    }
}
