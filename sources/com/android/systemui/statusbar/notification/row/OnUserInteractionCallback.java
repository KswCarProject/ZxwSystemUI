package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;

public interface OnUserInteractionCallback {
    void onImportanceChanged(NotificationEntry notificationEntry);

    Runnable registerFutureDismissal(NotificationEntry notificationEntry, int i);
}
