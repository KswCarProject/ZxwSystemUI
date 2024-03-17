package com.android.systemui.statusbar.notification.interruption;

import com.android.keyguard.KeyguardUpdateMonitorCallback;

/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public final class KeyguardNotificationVisibilityProviderImpl$start$2 extends KeyguardUpdateMonitorCallback {
    public final /* synthetic */ KeyguardNotificationVisibilityProviderImpl this$0;

    public KeyguardNotificationVisibilityProviderImpl$start$2(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl) {
        this.this$0 = keyguardNotificationVisibilityProviderImpl;
    }

    public void onStrongAuthStateChanged(int i) {
        this.this$0.notifyStateChanged("onStrongAuthStateChanged");
    }
}
