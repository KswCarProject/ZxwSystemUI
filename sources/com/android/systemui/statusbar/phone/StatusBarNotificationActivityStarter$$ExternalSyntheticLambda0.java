package com.android.systemui.statusbar.phone;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.RemoteAnimationAdapter;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StatusBarNotificationActivityStarter$$ExternalSyntheticLambda0 implements ActivityLaunchAnimator.PendingIntentStarter {
    public final /* synthetic */ StatusBarNotificationActivityStarter f$0;
    public final /* synthetic */ ExpandableNotificationRow f$1;
    public final /* synthetic */ PendingIntent f$2;
    public final /* synthetic */ Intent f$3;
    public final /* synthetic */ NotificationEntry f$4;

    public /* synthetic */ StatusBarNotificationActivityStarter$$ExternalSyntheticLambda0(StatusBarNotificationActivityStarter statusBarNotificationActivityStarter, ExpandableNotificationRow expandableNotificationRow, PendingIntent pendingIntent, Intent intent, NotificationEntry notificationEntry) {
        this.f$0 = statusBarNotificationActivityStarter;
        this.f$1 = expandableNotificationRow;
        this.f$2 = pendingIntent;
        this.f$3 = intent;
        this.f$4 = notificationEntry;
    }

    public final int startPendingIntent(RemoteAnimationAdapter remoteAnimationAdapter) {
        return this.f$0.lambda$startNotificationIntent$4(this.f$1, this.f$2, this.f$3, this.f$4, remoteAnimationAdapter);
    }
}
