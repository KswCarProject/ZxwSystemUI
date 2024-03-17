package com.android.systemui.statusbar.notification.collection;

import android.service.notification.StatusBarNotification;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotifCollection$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ NotifCollection f$0;
    public final /* synthetic */ StatusBarNotification f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ NotifCollection$$ExternalSyntheticLambda6(NotifCollection notifCollection, StatusBarNotification statusBarNotification, String str, String str2) {
        this.f$0 = notifCollection;
        this.f$1 = statusBarNotification;
        this.f$2 = str;
        this.f$3 = str2;
    }

    public final void run() {
        this.f$0.lambda$getInternalNotifUpdater$4(this.f$1, this.f$2, this.f$3);
    }
}
