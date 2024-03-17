package com.android.systemui.statusbar.phone;

import com.android.internal.logging.MetricsLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LockscreenGestureLogger_Factory implements Factory<LockscreenGestureLogger> {
    public final Provider<MetricsLogger> metricsLoggerProvider;

    public LockscreenGestureLogger_Factory(Provider<MetricsLogger> provider) {
        this.metricsLoggerProvider = provider;
    }

    public LockscreenGestureLogger get() {
        return newInstance(this.metricsLoggerProvider.get());
    }

    public static LockscreenGestureLogger_Factory create(Provider<MetricsLogger> provider) {
        return new LockscreenGestureLogger_Factory(provider);
    }

    public static LockscreenGestureLogger newInstance(MetricsLogger metricsLogger) {
        return new LockscreenGestureLogger(metricsLogger);
    }
}
