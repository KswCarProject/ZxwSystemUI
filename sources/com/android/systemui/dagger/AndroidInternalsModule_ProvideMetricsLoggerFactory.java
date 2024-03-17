package com.android.systemui.dagger;

import com.android.internal.logging.MetricsLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AndroidInternalsModule_ProvideMetricsLoggerFactory implements Factory<MetricsLogger> {
    public final AndroidInternalsModule module;

    public AndroidInternalsModule_ProvideMetricsLoggerFactory(AndroidInternalsModule androidInternalsModule) {
        this.module = androidInternalsModule;
    }

    public MetricsLogger get() {
        return provideMetricsLogger(this.module);
    }

    public static AndroidInternalsModule_ProvideMetricsLoggerFactory create(AndroidInternalsModule androidInternalsModule) {
        return new AndroidInternalsModule_ProvideMetricsLoggerFactory(androidInternalsModule);
    }

    public static MetricsLogger provideMetricsLogger(AndroidInternalsModule androidInternalsModule) {
        return (MetricsLogger) Preconditions.checkNotNullFromProvides(androidInternalsModule.provideMetricsLogger());
    }
}
