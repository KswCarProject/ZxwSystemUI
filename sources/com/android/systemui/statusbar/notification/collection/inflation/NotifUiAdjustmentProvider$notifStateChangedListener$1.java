package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.NotificationLockscreenUserManager;

/* compiled from: NotifUiAdjustmentProvider.kt */
public final class NotifUiAdjustmentProvider$notifStateChangedListener$1 implements NotificationLockscreenUserManager.NotificationStateChangedListener {
    public final /* synthetic */ NotifUiAdjustmentProvider this$0;

    public NotifUiAdjustmentProvider$notifStateChangedListener$1(NotifUiAdjustmentProvider notifUiAdjustmentProvider) {
        this.this$0 = notifUiAdjustmentProvider;
    }

    public final void onNotificationStateChanged() {
        for (Runnable run : this.this$0.dirtyListeners) {
            run.run();
        }
    }
}
