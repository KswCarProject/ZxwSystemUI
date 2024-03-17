package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.statusbar.policy.Clock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarDemoMode_Factory implements Factory<StatusBarDemoMode> {
    public final Provider<Clock> clockViewProvider;
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<Integer> displayIdProvider;
    public final Provider<NavigationBarController> navigationBarControllerProvider;
    public final Provider<View> operatorNameViewProvider;
    public final Provider<PhoneStatusBarTransitions> phoneStatusBarTransitionsProvider;

    public StatusBarDemoMode_Factory(Provider<Clock> provider, Provider<View> provider2, Provider<DemoModeController> provider3, Provider<PhoneStatusBarTransitions> provider4, Provider<NavigationBarController> provider5, Provider<Integer> provider6) {
        this.clockViewProvider = provider;
        this.operatorNameViewProvider = provider2;
        this.demoModeControllerProvider = provider3;
        this.phoneStatusBarTransitionsProvider = provider4;
        this.navigationBarControllerProvider = provider5;
        this.displayIdProvider = provider6;
    }

    public StatusBarDemoMode get() {
        return newInstance(this.clockViewProvider.get(), this.operatorNameViewProvider.get(), this.demoModeControllerProvider.get(), this.phoneStatusBarTransitionsProvider.get(), this.navigationBarControllerProvider.get(), this.displayIdProvider.get().intValue());
    }

    public static StatusBarDemoMode_Factory create(Provider<Clock> provider, Provider<View> provider2, Provider<DemoModeController> provider3, Provider<PhoneStatusBarTransitions> provider4, Provider<NavigationBarController> provider5, Provider<Integer> provider6) {
        return new StatusBarDemoMode_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static StatusBarDemoMode newInstance(Clock clock, View view, DemoModeController demoModeController, PhoneStatusBarTransitions phoneStatusBarTransitions, NavigationBarController navigationBarController, int i) {
        return new StatusBarDemoMode(clock, view, demoModeController, phoneStatusBarTransitions, navigationBarController, i);
    }
}
