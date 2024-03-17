package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HeadsUpViewBinder$$ExternalSyntheticLambda0 implements NotifBindPipeline.BindCallback {
    public final /* synthetic */ HeadsUpViewBinder f$0;

    public /* synthetic */ HeadsUpViewBinder$$ExternalSyntheticLambda0(HeadsUpViewBinder headsUpViewBinder) {
        this.f$0 = headsUpViewBinder;
    }

    public final void onBindFinished(NotificationEntry notificationEntry) {
        this.f$0.lambda$unbindHeadsUpView$1(notificationEntry);
    }
}
