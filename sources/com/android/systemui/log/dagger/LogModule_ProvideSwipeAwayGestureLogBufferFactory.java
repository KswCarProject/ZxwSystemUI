package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LogModule_ProvideSwipeAwayGestureLogBufferFactory implements Factory<LogBuffer> {
    public final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideSwipeAwayGestureLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    public LogBuffer get() {
        return provideSwipeAwayGestureLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideSwipeAwayGestureLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideSwipeAwayGestureLogBufferFactory(provider);
    }

    public static LogBuffer provideSwipeAwayGestureLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideSwipeAwayGestureLogBuffer(logBufferFactory));
    }
}
