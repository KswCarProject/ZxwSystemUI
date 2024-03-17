package com.android.systemui.statusbar.phone.dagger;

import android.view.View;
import com.android.systemui.battery.BatteryMeterView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_GetBatteryMeterViewFactory implements Factory<BatteryMeterView> {
    public final Provider<View> viewProvider;

    public StatusBarViewModule_GetBatteryMeterViewFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    public BatteryMeterView get() {
        return getBatteryMeterView(this.viewProvider.get());
    }

    public static StatusBarViewModule_GetBatteryMeterViewFactory create(Provider<View> provider) {
        return new StatusBarViewModule_GetBatteryMeterViewFactory(provider);
    }

    public static BatteryMeterView getBatteryMeterView(View view) {
        return (BatteryMeterView) Preconditions.checkNotNullFromProvides(StatusBarViewModule.getBatteryMeterView(view));
    }
}
