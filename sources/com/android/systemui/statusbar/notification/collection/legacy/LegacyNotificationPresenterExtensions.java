package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import org.jetbrains.annotations.NotNull;

public class LegacyNotificationPresenterExtensions implements NotifShadeEventSource {
    public boolean mEntryListenerAdded;
    public final NotificationEntryManager mEntryManager;
    public Runnable mNotifRemovedByUserCallback;
    public Runnable mShadeEmptiedCallback;

    public LegacyNotificationPresenterExtensions(NotificationEntryManager notificationEntryManager) {
        this.mEntryManager = notificationEntryManager;
    }

    public final void ensureEntryListenerAdded() {
        if (!this.mEntryListenerAdded) {
            this.mEntryListenerAdded = true;
            this.mEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
                public void onEntryRemoved(@NotNull NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                    if (!(notificationEntry.getSbn() == null || LegacyNotificationPresenterExtensions.this.mEntryManager.hasActiveNotifications() || LegacyNotificationPresenterExtensions.this.mShadeEmptiedCallback == null)) {
                        LegacyNotificationPresenterExtensions.this.mShadeEmptiedCallback.run();
                    }
                    if (z && LegacyNotificationPresenterExtensions.this.mNotifRemovedByUserCallback != null) {
                        LegacyNotificationPresenterExtensions.this.mNotifRemovedByUserCallback.run();
                    }
                }
            });
        }
    }

    public void setNotifRemovedByUserCallback(Runnable runnable) {
        if (this.mNotifRemovedByUserCallback == null) {
            this.mNotifRemovedByUserCallback = runnable;
            ensureEntryListenerAdded();
            return;
        }
        throw new IllegalStateException("mNotifRemovedByUserCallback already set");
    }

    public void setShadeEmptiedCallback(Runnable runnable) {
        if (this.mShadeEmptiedCallback == null) {
            this.mShadeEmptiedCallback = runnable;
            ensureEntryListenerAdded();
            return;
        }
        throw new IllegalStateException("mShadeEmptiedCallback already set");
    }
}
