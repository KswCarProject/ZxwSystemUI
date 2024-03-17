package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StatusBarRemoteInputCallback$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ StatusBarRemoteInputCallback f$0;
    public final /* synthetic */ NotificationStackScrollLayout f$1;

    public /* synthetic */ StatusBarRemoteInputCallback$$ExternalSyntheticLambda4(StatusBarRemoteInputCallback statusBarRemoteInputCallback, NotificationStackScrollLayout notificationStackScrollLayout) {
        this.f$0 = statusBarRemoteInputCallback;
        this.f$1 = notificationStackScrollLayout;
    }

    public final void run() {
        this.f$0.lambda$onWorkChallengeChanged$0(this.f$1);
    }
}
