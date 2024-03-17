package com.android.systemui.dagger;

import com.android.internal.app.IBatteryStats;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvideIBatteryStatsFactory implements Factory<IBatteryStats> {

    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIBatteryStatsFactory INSTANCE = new FrameworkServicesModule_ProvideIBatteryStatsFactory();
    }

    public IBatteryStats get() {
        return provideIBatteryStats();
    }

    public static FrameworkServicesModule_ProvideIBatteryStatsFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IBatteryStats provideIBatteryStats() {
        return (IBatteryStats) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIBatteryStats());
    }
}
