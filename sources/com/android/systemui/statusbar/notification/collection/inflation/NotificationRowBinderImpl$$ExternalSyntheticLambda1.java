package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.android.systemui.statusbar.notification.row.RowInflaterTask;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationRowBinderImpl$$ExternalSyntheticLambda1 implements RowInflaterTask.RowInflationFinishedListener {
    public final /* synthetic */ NotificationRowBinderImpl f$0;
    public final /* synthetic */ NotificationEntry f$1;
    public final /* synthetic */ NotifInflater.Params f$2;
    public final /* synthetic */ NotificationRowContentBinder.InflationCallback f$3;

    public /* synthetic */ NotificationRowBinderImpl$$ExternalSyntheticLambda1(NotificationRowBinderImpl notificationRowBinderImpl, NotificationEntry notificationEntry, NotifInflater.Params params, NotificationRowContentBinder.InflationCallback inflationCallback) {
        this.f$0 = notificationRowBinderImpl;
        this.f$1 = notificationEntry;
        this.f$2 = params;
        this.f$3 = inflationCallback;
    }

    public final void onInflationFinished(ExpandableNotificationRow expandableNotificationRow) {
        this.f$0.lambda$inflateViews$0(this.f$1, this.f$2, this.f$3, expandableNotificationRow);
    }
}
