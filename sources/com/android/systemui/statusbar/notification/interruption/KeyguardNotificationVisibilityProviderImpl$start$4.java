package com.android.systemui.statusbar.notification.interruption;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public final class KeyguardNotificationVisibilityProviderImpl$start$4 extends BroadcastReceiver {
    public final /* synthetic */ KeyguardNotificationVisibilityProviderImpl this$0;

    public KeyguardNotificationVisibilityProviderImpl$start$4(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl) {
        this.this$0 = keyguardNotificationVisibilityProviderImpl;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        if (this.this$0.isLockedOrLocking()) {
            KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl = this.this$0;
            String action = intent.getAction();
            Intrinsics.checkNotNull(action);
            keyguardNotificationVisibilityProviderImpl.notifyStateChanged(action);
        }
    }
}
