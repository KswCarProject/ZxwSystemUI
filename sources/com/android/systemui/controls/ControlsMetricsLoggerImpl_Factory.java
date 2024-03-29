package com.android.systemui.controls;

import dagger.internal.Factory;

public final class ControlsMetricsLoggerImpl_Factory implements Factory<ControlsMetricsLoggerImpl> {

    public static final class InstanceHolder {
        public static final ControlsMetricsLoggerImpl_Factory INSTANCE = new ControlsMetricsLoggerImpl_Factory();
    }

    public ControlsMetricsLoggerImpl get() {
        return newInstance();
    }

    public static ControlsMetricsLoggerImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ControlsMetricsLoggerImpl newInstance() {
        return new ControlsMetricsLoggerImpl();
    }
}
