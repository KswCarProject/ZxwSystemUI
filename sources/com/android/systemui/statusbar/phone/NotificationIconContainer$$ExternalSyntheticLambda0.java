package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.StatusBarIconView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationIconContainer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationIconContainer f$0;
    public final /* synthetic */ StatusBarIconView f$1;

    public /* synthetic */ NotificationIconContainer$$ExternalSyntheticLambda0(NotificationIconContainer notificationIconContainer, StatusBarIconView statusBarIconView) {
        this.f$0 = notificationIconContainer;
        this.f$1 = statusBarIconView;
    }

    public final void run() {
        this.f$0.lambda$onViewRemoved$0(this.f$1);
    }
}
