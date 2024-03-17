package com.android.systemui.statusbar.phone.fragment.dagger;

import android.view.View;
import com.android.systemui.R$id;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.statusbar.HeadsUpStatusBarView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import java.util.Optional;

public interface StatusBarFragmentModule {
    static PhoneStatusBarView providePhoneStatusBarView(CollapsedStatusBarFragment collapsedStatusBarFragment) {
        return (PhoneStatusBarView) collapsedStatusBarFragment.getView();
    }

    static BatteryMeterView provideBatteryMeterView(PhoneStatusBarView phoneStatusBarView) {
        return (BatteryMeterView) phoneStatusBarView.findViewById(R$id.battery);
    }

    static View provideLightsOutNotifView(PhoneStatusBarView phoneStatusBarView) {
        return phoneStatusBarView.findViewById(R$id.notification_lights_out);
    }

    static View provideOperatorNameView(PhoneStatusBarView phoneStatusBarView) {
        return phoneStatusBarView.findViewById(R$id.operator_name);
    }

    static Optional<View> provideOperatorFrameNameView(PhoneStatusBarView phoneStatusBarView) {
        return Optional.ofNullable(phoneStatusBarView.findViewById(R$id.operator_name_frame));
    }

    static Clock provideClock(PhoneStatusBarView phoneStatusBarView) {
        return (Clock) phoneStatusBarView.findViewById(R$id.clock);
    }

    static StatusBarUserSwitcherContainer provideStatusBarUserSwitcherContainer(PhoneStatusBarView phoneStatusBarView) {
        return (StatusBarUserSwitcherContainer) phoneStatusBarView.findViewById(R$id.user_switcher_container);
    }

    static PhoneStatusBarViewController providePhoneStatusBarViewController(PhoneStatusBarViewController.Factory factory, PhoneStatusBarView phoneStatusBarView, NotificationPanelViewController notificationPanelViewController) {
        return factory.create(phoneStatusBarView, notificationPanelViewController.getStatusBarTouchEventHandler());
    }

    static PhoneStatusBarTransitions providePhoneStatusBarTransitions(PhoneStatusBarView phoneStatusBarView, StatusBarWindowController statusBarWindowController) {
        return new PhoneStatusBarTransitions(phoneStatusBarView, statusBarWindowController.getBackgroundView());
    }

    static HeadsUpStatusBarView providesHeasdUpStatusBarView(PhoneStatusBarView phoneStatusBarView) {
        return (HeadsUpStatusBarView) phoneStatusBarView.findViewById(R$id.heads_up_status_bar_view);
    }
}
