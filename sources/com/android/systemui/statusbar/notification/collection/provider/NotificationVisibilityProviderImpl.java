package com.android.systemui.statusbar.notification.collection.provider;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationVisibilityProviderImpl.kt */
public final class NotificationVisibilityProviderImpl implements NotificationVisibilityProvider {
    @NotNull
    public final CommonNotifCollection notifCollection;
    @NotNull
    public final NotifLiveDataStore notifDataStore;

    public NotificationVisibilityProviderImpl(@NotNull NotifLiveDataStore notifLiveDataStore, @NotNull CommonNotifCollection commonNotifCollection) {
        this.notifDataStore = notifLiveDataStore;
        this.notifCollection = commonNotifCollection;
    }

    @NotNull
    public NotificationVisibility obtain(@NotNull NotificationEntry notificationEntry, boolean z) {
        int count = getCount();
        int rank = notificationEntry.getRanking().getRank();
        boolean z2 = true;
        boolean z3 = notificationEntry.getRow() != null;
        NotificationVisibility.NotificationLocation notificationLocation = NotificationLogger.getNotificationLocation(notificationEntry);
        String key = notificationEntry.getKey();
        if (!z || !z3) {
            z2 = false;
        }
        return NotificationVisibility.obtain(key, rank, count, z2, notificationLocation);
    }

    @NotNull
    public NotificationVisibility obtain(@NotNull String str, boolean z) {
        NotificationEntry entry = this.notifCollection.getEntry(str);
        if (entry == null) {
            return NotificationVisibility.obtain(str, -1, getCount(), false);
        }
        return obtain(entry, z);
    }

    @NotNull
    public NotificationVisibility.NotificationLocation getLocation(@NotNull String str) {
        return NotificationLogger.getNotificationLocation(this.notifCollection.getEntry(str));
    }

    public final int getCount() {
        return this.notifDataStore.getActiveNotifCount().getValue().intValue();
    }
}
