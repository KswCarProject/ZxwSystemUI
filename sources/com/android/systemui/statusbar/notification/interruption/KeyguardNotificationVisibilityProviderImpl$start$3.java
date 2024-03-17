package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.plugins.statusbar.StatusBarStateController;

/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public final class KeyguardNotificationVisibilityProviderImpl$start$3 implements StatusBarStateController.StateListener {
    public final /* synthetic */ KeyguardNotificationVisibilityProviderImpl this$0;

    public KeyguardNotificationVisibilityProviderImpl$start$3(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl) {
        this.this$0 = keyguardNotificationVisibilityProviderImpl;
    }

    public void onStateChanged(int i) {
        this.this$0.notifyStateChanged("onStatusBarStateChanged");
    }

    public void onUpcomingStateChanged(int i) {
        this.this$0.notifyStateChanged("onStatusBarUpcomingStateChanged");
    }
}
