package com.android.systemui.statusbar.events;

import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SystemEventCoordinator_Factory implements Factory<SystemEventCoordinator> {
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<PrivacyItemController> privacyControllerProvider;
    public final Provider<SystemClock> systemClockProvider;

    public SystemEventCoordinator_Factory(Provider<SystemClock> provider, Provider<BatteryController> provider2, Provider<PrivacyItemController> provider3) {
        this.systemClockProvider = provider;
        this.batteryControllerProvider = provider2;
        this.privacyControllerProvider = provider3;
    }

    public SystemEventCoordinator get() {
        return newInstance(this.systemClockProvider.get(), this.batteryControllerProvider.get(), this.privacyControllerProvider.get());
    }

    public static SystemEventCoordinator_Factory create(Provider<SystemClock> provider, Provider<BatteryController> provider2, Provider<PrivacyItemController> provider3) {
        return new SystemEventCoordinator_Factory(provider, provider2, provider3);
    }

    public static SystemEventCoordinator newInstance(SystemClock systemClock, BatteryController batteryController, PrivacyItemController privacyItemController) {
        return new SystemEventCoordinator(systemClock, batteryController, privacyItemController);
    }
}
