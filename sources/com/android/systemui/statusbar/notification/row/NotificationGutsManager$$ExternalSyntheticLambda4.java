package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationGutsManager$$ExternalSyntheticLambda4 implements NotificationGuts.OnGutsClosedListener {
    public final /* synthetic */ NotificationGutsManager f$0;
    public final /* synthetic */ ExpandableNotificationRow f$1;
    public final /* synthetic */ NotificationEntry f$2;

    public /* synthetic */ NotificationGutsManager$$ExternalSyntheticLambda4(NotificationGutsManager notificationGutsManager, ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry) {
        this.f$0 = notificationGutsManager;
        this.f$1 = expandableNotificationRow;
        this.f$2 = notificationEntry;
    }

    public final void onGutsClosed(NotificationGuts notificationGuts) {
        this.f$0.lambda$bindGuts$0(this.f$1, this.f$2, notificationGuts);
    }
}
