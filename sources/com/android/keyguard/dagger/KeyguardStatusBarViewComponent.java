package com.android.keyguard.dagger;

import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarViewController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;

public interface KeyguardStatusBarViewComponent {

    public interface Factory {
        KeyguardStatusBarViewComponent build(KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider);
    }

    KeyguardStatusBarViewController getKeyguardStatusBarViewController();
}
