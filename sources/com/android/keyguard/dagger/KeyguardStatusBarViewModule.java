package com.android.keyguard.dagger;

import com.android.keyguard.CarrierText;
import com.android.systemui.R$id;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;

public abstract class KeyguardStatusBarViewModule {
    public static CarrierText getCarrierText(KeyguardStatusBarView keyguardStatusBarView) {
        return (CarrierText) keyguardStatusBarView.findViewById(R$id.keyguard_carrier_text);
    }

    public static BatteryMeterView getBatteryMeterView(KeyguardStatusBarView keyguardStatusBarView) {
        return (BatteryMeterView) keyguardStatusBarView.findViewById(R$id.battery);
    }

    public static StatusBarUserSwitcherContainer getUserSwitcherContainer(KeyguardStatusBarView keyguardStatusBarView) {
        return (StatusBarUserSwitcherContainer) keyguardStatusBarView.findViewById(R$id.user_switcher_container);
    }
}
