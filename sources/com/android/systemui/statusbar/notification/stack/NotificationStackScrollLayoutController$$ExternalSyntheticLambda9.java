package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.phone.KeyguardBypassController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationStackScrollLayoutController$$ExternalSyntheticLambda9 implements KeyguardBypassController.OnBypassStateChangedListener {
    public final /* synthetic */ NotificationStackScrollLayout f$0;

    public /* synthetic */ NotificationStackScrollLayoutController$$ExternalSyntheticLambda9(NotificationStackScrollLayout notificationStackScrollLayout) {
        this.f$0 = notificationStackScrollLayout;
    }

    public final void onBypassStateChanged(boolean z) {
        this.f$0.setKeyguardBypassEnabled(z);
    }
}
