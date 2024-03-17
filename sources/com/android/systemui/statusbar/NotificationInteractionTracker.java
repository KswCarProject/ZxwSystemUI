package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationInteractionTracker.kt */
public final class NotificationInteractionTracker implements NotifCollectionListener, NotificationInteractionListener {
    @NotNull
    public final NotificationClickNotifier clicker;
    @NotNull
    public final NotificationEntryManager entryManager;
    @NotNull
    public final Map<String, Boolean> interactions = new LinkedHashMap();

    public NotificationInteractionTracker(@NotNull NotificationClickNotifier notificationClickNotifier, @NotNull NotificationEntryManager notificationEntryManager) {
        this.clicker = notificationClickNotifier;
        this.entryManager = notificationEntryManager;
        notificationClickNotifier.addNotificationInteractionListener(this);
        notificationEntryManager.addCollectionListener(this);
    }

    public final boolean hasUserInteractedWith(@NotNull String str) {
        Boolean bool = this.interactions.get(str);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public void onEntryAdded(@NotNull NotificationEntry notificationEntry) {
        this.interactions.put(notificationEntry.getKey(), Boolean.FALSE);
    }

    public void onEntryCleanUp(@NotNull NotificationEntry notificationEntry) {
        this.interactions.remove(notificationEntry.getKey());
    }

    public void onNotificationInteraction(@NotNull String str) {
        this.interactions.put(str, Boolean.TRUE);
    }
}
