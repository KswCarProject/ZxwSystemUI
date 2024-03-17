package com.android.systemui.dagger;

import dagger.internal.Factory;

public final class FrameworkServicesModule_ProvideIsTestHarnessFactory implements Factory<Boolean> {

    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIsTestHarnessFactory INSTANCE = new FrameworkServicesModule_ProvideIsTestHarnessFactory();
    }

    public Boolean get() {
        return Boolean.valueOf(provideIsTestHarness());
    }

    public static FrameworkServicesModule_ProvideIsTestHarnessFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideIsTestHarness() {
        return FrameworkServicesModule.provideIsTestHarness();
    }
}
