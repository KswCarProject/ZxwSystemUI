package com.android.systemui.statusbar.phone;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LightBarController_Factory_Factory implements Factory<LightBarController.Factory> {
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<DarkIconDispatcher> darkIconDispatcherProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<NavigationModeController> navModeControllerProvider;

    public LightBarController_Factory_Factory(Provider<DarkIconDispatcher> provider, Provider<BatteryController> provider2, Provider<NavigationModeController> provider3, Provider<DumpManager> provider4) {
        this.darkIconDispatcherProvider = provider;
        this.batteryControllerProvider = provider2;
        this.navModeControllerProvider = provider3;
        this.dumpManagerProvider = provider4;
    }

    public LightBarController.Factory get() {
        return newInstance(this.darkIconDispatcherProvider.get(), this.batteryControllerProvider.get(), this.navModeControllerProvider.get(), this.dumpManagerProvider.get());
    }

    public static LightBarController_Factory_Factory create(Provider<DarkIconDispatcher> provider, Provider<BatteryController> provider2, Provider<NavigationModeController> provider3, Provider<DumpManager> provider4) {
        return new LightBarController_Factory_Factory(provider, provider2, provider3, provider4);
    }

    public static LightBarController.Factory newInstance(DarkIconDispatcher darkIconDispatcher, BatteryController batteryController, NavigationModeController navigationModeController, DumpManager dumpManager) {
        return new LightBarController.Factory(darkIconDispatcher, batteryController, navigationModeController, dumpManager);
    }
}
