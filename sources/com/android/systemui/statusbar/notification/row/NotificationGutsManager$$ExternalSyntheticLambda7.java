package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.row.NotificationGuts;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationGutsManager$$ExternalSyntheticLambda7 implements NotificationGuts.OnHeightChangedListener {
    public final /* synthetic */ NotificationGutsManager f$0;
    public final /* synthetic */ ExpandableNotificationRow f$1;

    public /* synthetic */ NotificationGutsManager$$ExternalSyntheticLambda7(NotificationGutsManager notificationGutsManager, ExpandableNotificationRow expandableNotificationRow) {
        this.f$0 = notificationGutsManager;
        this.f$1 = expandableNotificationRow;
    }

    public final void onHeightChanged(NotificationGuts notificationGuts) {
        this.f$0.lambda$initializeSnoozeView$1(this.f$1, notificationGuts);
    }
}
