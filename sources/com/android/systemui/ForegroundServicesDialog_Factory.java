package com.android.systemui;

import com.android.internal.logging.MetricsLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ForegroundServicesDialog_Factory implements Factory<ForegroundServicesDialog> {
    public final Provider<MetricsLogger> metricsLoggerProvider;

    public ForegroundServicesDialog_Factory(Provider<MetricsLogger> provider) {
        this.metricsLoggerProvider = provider;
    }

    public ForegroundServicesDialog get() {
        return newInstance(this.metricsLoggerProvider.get());
    }

    public static ForegroundServicesDialog_Factory create(Provider<MetricsLogger> provider) {
        return new ForegroundServicesDialog_Factory(provider);
    }

    public static ForegroundServicesDialog newInstance(MetricsLogger metricsLogger) {
        return new ForegroundServicesDialog(metricsLogger);
    }
}
