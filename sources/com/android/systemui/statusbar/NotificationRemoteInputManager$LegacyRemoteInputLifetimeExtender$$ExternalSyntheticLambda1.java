package com.android.systemui.statusbar;

import com.android.systemui.statusbar.NotificationRemoteInputManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationRemoteInputManager$LegacyRemoteInputLifetimeExtender$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ NotificationRemoteInputManager$LegacyRemoteInputLifetimeExtender$$ExternalSyntheticLambda1(NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender legacyRemoteInputLifetimeExtender, String str) {
        this.f$0 = legacyRemoteInputLifetimeExtender;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.lambda$releaseNotificationIfKeptForRemoteInputHistory$1(this.f$1);
    }
}
