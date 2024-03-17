package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvideBatteryMeterViewFactory implements Factory<BatteryMeterView> {
    public final Provider<PhoneStatusBarView> viewProvider;

    public StatusBarFragmentModule_ProvideBatteryMeterViewFactory(Provider<PhoneStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public BatteryMeterView get() {
        return provideBatteryMeterView(this.viewProvider.get());
    }

    public static StatusBarFragmentModule_ProvideBatteryMeterViewFactory create(Provider<PhoneStatusBarView> provider) {
        return new StatusBarFragmentModule_ProvideBatteryMeterViewFactory(provider);
    }

    public static BatteryMeterView provideBatteryMeterView(PhoneStatusBarView phoneStatusBarView) {
        return (BatteryMeterView) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.provideBatteryMeterView(phoneStatusBarView));
    }
}
