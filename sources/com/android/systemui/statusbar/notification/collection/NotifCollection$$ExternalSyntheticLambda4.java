package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotifCollection$$ExternalSyntheticLambda4 implements NotifLifetimeExtender.OnEndLifetimeExtensionCallback {
    public final /* synthetic */ NotifCollection f$0;

    public /* synthetic */ NotifCollection$$ExternalSyntheticLambda4(NotifCollection notifCollection) {
        this.f$0 = notifCollection;
    }

    public final void onEndLifetimeExtension(NotifLifetimeExtender notifLifetimeExtender, NotificationEntry notificationEntry) {
        this.f$0.onEndLifetimeExtension(notifLifetimeExtender, notificationEntry);
    }
}
