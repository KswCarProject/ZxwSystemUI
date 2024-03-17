package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeEventCoordinator.kt */
public final class ShadeEventCoordinator$mNotifCollectionListener$1 implements NotifCollectionListener {
    public final /* synthetic */ ShadeEventCoordinator this$0;

    public ShadeEventCoordinator$mNotifCollectionListener$1(ShadeEventCoordinator shadeEventCoordinator) {
        this.this$0 = shadeEventCoordinator;
    }

    public void onEntryRemoved(@NotNull NotificationEntry notificationEntry, int i) {
        boolean z = true;
        this.this$0.mEntryRemoved = true;
        ShadeEventCoordinator shadeEventCoordinator = this.this$0;
        if (!(i == 1 || i == 3 || i == 2)) {
            z = false;
        }
        shadeEventCoordinator.mEntryRemovedByUser = z;
    }
}
