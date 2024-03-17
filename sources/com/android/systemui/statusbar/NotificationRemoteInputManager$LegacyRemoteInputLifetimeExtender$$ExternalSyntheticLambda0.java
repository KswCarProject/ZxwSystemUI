package com.android.systemui.statusbar;

import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationRemoteInputManager$LegacyRemoteInputLifetimeExtender$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender f$0;
    public final /* synthetic */ NotificationEntry f$1;

    public /* synthetic */ NotificationRemoteInputManager$LegacyRemoteInputLifetimeExtender$$ExternalSyntheticLambda0(NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender legacyRemoteInputLifetimeExtender, NotificationEntry notificationEntry) {
        this.f$0 = legacyRemoteInputLifetimeExtender;
        this.f$1 = notificationEntry;
    }

    public final void run() {
        this.f$0.lambda$onRemoteInputSent$0(this.f$1);
    }
}
