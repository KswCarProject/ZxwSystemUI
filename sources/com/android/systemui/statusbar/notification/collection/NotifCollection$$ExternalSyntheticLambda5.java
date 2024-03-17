package com.android.systemui.statusbar.notification.collection;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.notifcollection.InternalNotifUpdater;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotifCollection$$ExternalSyntheticLambda5 implements InternalNotifUpdater {
    public final /* synthetic */ NotifCollection f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ NotifCollection$$ExternalSyntheticLambda5(NotifCollection notifCollection, String str) {
        this.f$0 = notifCollection;
        this.f$1 = str;
    }

    public final void onInternalNotificationUpdate(StatusBarNotification statusBarNotification, String str) {
        this.f$0.lambda$getInternalNotifUpdater$5(this.f$1, statusBarNotification, str);
    }
}
