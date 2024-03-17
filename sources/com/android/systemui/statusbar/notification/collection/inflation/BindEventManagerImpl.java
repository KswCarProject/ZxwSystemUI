package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import org.jetbrains.annotations.NotNull;

/* compiled from: BindEventManagerImpl.kt */
public final class BindEventManagerImpl extends BindEventManager {
    public final void notifyViewBound(@NotNull NotificationEntry notificationEntry) {
        for (BindEventManager.Listener onViewBound : getListeners()) {
            onViewBound.onViewBound(notificationEntry);
        }
    }

    public final void attachToLegacyPipeline(@NotNull NotificationEntryManager notificationEntryManager) {
        notificationEntryManager.addNotificationEntryListener(new BindEventManagerImpl$attachToLegacyPipeline$1(this));
    }
}
