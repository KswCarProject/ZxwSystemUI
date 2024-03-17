package com.android.systemui.statusbar.phone;

import android.app.PendingIntent;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StatusBarNotificationActivityStarter$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ StatusBarNotificationActivityStarter f$0;
    public final /* synthetic */ NotificationEntry f$1;
    public final /* synthetic */ ExpandableNotificationRow f$2;
    public final /* synthetic */ PendingIntent f$3;
    public final /* synthetic */ boolean f$4;
    public final /* synthetic */ boolean f$5;

    public /* synthetic */ StatusBarNotificationActivityStarter$$ExternalSyntheticLambda3(StatusBarNotificationActivityStarter statusBarNotificationActivityStarter, NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, PendingIntent pendingIntent, boolean z, boolean z2) {
        this.f$0 = statusBarNotificationActivityStarter;
        this.f$1 = notificationEntry;
        this.f$2 = expandableNotificationRow;
        this.f$3 = pendingIntent;
        this.f$4 = z;
        this.f$5 = z2;
    }

    public final void run() {
        this.f$0.lambda$handleNotificationClickAfterKeyguardDismissed$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
