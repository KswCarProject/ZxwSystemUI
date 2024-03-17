package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.phone.LightBarController_Factory  reason: case insensitive filesystem */
public final class C0005LightBarController_Factory implements Factory<LightBarController> {
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<Context> ctxProvider;
    public final Provider<DarkIconDispatcher> darkIconDispatcherProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<NavigationModeController> navModeControllerProvider;

    public C0005LightBarController_Factory(Provider<Context> provider, Provider<DarkIconDispatcher> provider2, Provider<BatteryController> provider3, Provider<NavigationModeController> provider4, Provider<DumpManager> provider5) {
        this.ctxProvider = provider;
        this.darkIconDispatcherProvider = provider2;
        this.batteryControllerProvider = provider3;
        this.navModeControllerProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    public LightBarController get() {
        return newInstance(this.ctxProvider.get(), this.darkIconDispatcherProvider.get(), this.batteryControllerProvider.get(), this.navModeControllerProvider.get(), this.dumpManagerProvider.get());
    }

    public static C0005LightBarController_Factory create(Provider<Context> provider, Provider<DarkIconDispatcher> provider2, Provider<BatteryController> provider3, Provider<NavigationModeController> provider4, Provider<DumpManager> provider5) {
        return new C0005LightBarController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static LightBarController newInstance(Context context, DarkIconDispatcher darkIconDispatcher, BatteryController batteryController, NavigationModeController navigationModeController, DumpManager dumpManager) {
        return new LightBarController(context, darkIconDispatcher, batteryController, navigationModeController, dumpManager);
    }
}
