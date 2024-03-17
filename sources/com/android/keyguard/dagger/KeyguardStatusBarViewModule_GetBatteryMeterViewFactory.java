package com.android.keyguard.dagger;

import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class KeyguardStatusBarViewModule_GetBatteryMeterViewFactory implements Factory<BatteryMeterView> {
    public final Provider<KeyguardStatusBarView> viewProvider;

    public KeyguardStatusBarViewModule_GetBatteryMeterViewFactory(Provider<KeyguardStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public BatteryMeterView get() {
        return getBatteryMeterView(this.viewProvider.get());
    }

    public static KeyguardStatusBarViewModule_GetBatteryMeterViewFactory create(Provider<KeyguardStatusBarView> provider) {
        return new KeyguardStatusBarViewModule_GetBatteryMeterViewFactory(provider);
    }

    public static BatteryMeterView getBatteryMeterView(KeyguardStatusBarView keyguardStatusBarView) {
        return (BatteryMeterView) Preconditions.checkNotNullFromProvides(KeyguardStatusBarViewModule.getBatteryMeterView(keyguardStatusBarView));
    }
}
