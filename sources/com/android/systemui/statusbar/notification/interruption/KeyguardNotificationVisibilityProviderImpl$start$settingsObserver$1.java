package com.android.systemui.statusbar.notification.interruption;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public final class KeyguardNotificationVisibilityProviderImpl$start$settingsObserver$1 extends ContentObserver {
    public final /* synthetic */ KeyguardNotificationVisibilityProviderImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardNotificationVisibilityProviderImpl$start$settingsObserver$1(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl, Handler handler) {
        super(handler);
        this.this$0 = keyguardNotificationVisibilityProviderImpl;
    }

    public void onChange(boolean z, @Nullable Uri uri) {
        if (Intrinsics.areEqual((Object) uri, (Object) this.this$0.showSilentNotifsUri)) {
            this.this$0.readShowSilentNotificationSetting();
        }
        if (this.this$0.isLockedOrLocking()) {
            KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl = this.this$0;
            keyguardNotificationVisibilityProviderImpl.notifyStateChanged("Settings " + uri + " changed");
        }
    }
}
