package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.statusbar.policy.KeyguardStateController;

/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public final class KeyguardNotificationVisibilityProviderImpl$start$1 implements KeyguardStateController.Callback {
    public final /* synthetic */ KeyguardNotificationVisibilityProviderImpl this$0;

    public KeyguardNotificationVisibilityProviderImpl$start$1(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl) {
        this.this$0 = keyguardNotificationVisibilityProviderImpl;
    }

    public void onUnlockedChanged() {
        this.this$0.notifyStateChanged("onUnlockedChanged");
    }

    public void onKeyguardShowingChanged() {
        this.this$0.notifyStateChanged("onKeyguardShowingChanged");
    }
}
