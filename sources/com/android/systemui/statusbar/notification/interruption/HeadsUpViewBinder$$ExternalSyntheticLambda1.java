package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HeadsUpViewBinder$$ExternalSyntheticLambda1 implements NotifBindPipeline.BindCallback {
    public final /* synthetic */ HeadsUpViewBinder f$0;
    public final /* synthetic */ NotificationEntry f$1;
    public final /* synthetic */ RowContentBindParams f$2;
    public final /* synthetic */ NotifBindPipeline.BindCallback f$3;

    public /* synthetic */ HeadsUpViewBinder$$ExternalSyntheticLambda1(HeadsUpViewBinder headsUpViewBinder, NotificationEntry notificationEntry, RowContentBindParams rowContentBindParams, NotifBindPipeline.BindCallback bindCallback) {
        this.f$0 = headsUpViewBinder;
        this.f$1 = notificationEntry;
        this.f$2 = rowContentBindParams;
        this.f$3 = bindCallback;
    }

    public final void onBindFinished(NotificationEntry notificationEntry) {
        this.f$0.lambda$bindHeadsUpView$0(this.f$1, this.f$2, this.f$3, notificationEntry);
    }
}
