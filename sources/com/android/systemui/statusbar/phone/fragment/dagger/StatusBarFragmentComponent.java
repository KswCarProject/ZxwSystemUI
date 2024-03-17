package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.LightsOutNotifController;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.StatusBarDemoMode;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;

public interface StatusBarFragmentComponent {

    public interface Factory {
        StatusBarFragmentComponent create(CollapsedStatusBarFragment collapsedStatusBarFragment);
    }

    BatteryMeterViewController getBatteryMeterViewController();

    HeadsUpAppearanceController getHeadsUpAppearanceController();

    LightsOutNotifController getLightsOutNotifController();

    PhoneStatusBarTransitions getPhoneStatusBarTransitions();

    PhoneStatusBarView getPhoneStatusBarView();

    PhoneStatusBarViewController getPhoneStatusBarViewController();

    StatusBarDemoMode getStatusBarDemoMode();

    void init() {
        getBatteryMeterViewController().init();
        getHeadsUpAppearanceController().init();
        getPhoneStatusBarViewController().init();
        getLightsOutNotifController().init();
        getStatusBarDemoMode().init();
    }
}
