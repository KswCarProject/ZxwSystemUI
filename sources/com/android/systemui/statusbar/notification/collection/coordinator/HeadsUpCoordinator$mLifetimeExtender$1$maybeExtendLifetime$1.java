package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$mLifetimeExtender$1$maybeExtendLifetime$1 implements Runnable {
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ HeadsUpCoordinator this$0;

    public HeadsUpCoordinator$mLifetimeExtender$1$maybeExtendLifetime$1(HeadsUpCoordinator headsUpCoordinator, NotificationEntry notificationEntry) {
        this.this$0 = headsUpCoordinator;
        this.$entry = notificationEntry;
    }

    public final void run() {
        this.this$0.mHeadsUpManager.removeNotification(this.$entry.getKey(), true);
    }
}
