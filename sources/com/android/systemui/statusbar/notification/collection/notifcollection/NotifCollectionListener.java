package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.app.NotificationChannel;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;

public interface NotifCollectionListener {
    void onEntryAdded(NotificationEntry notificationEntry) {
    }

    void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
    }

    void onEntryCleanUp(NotificationEntry notificationEntry) {
    }

    void onEntryInit(NotificationEntry notificationEntry) {
    }

    void onEntryRemoved(NotificationEntry notificationEntry, int i) {
    }

    void onEntryUpdated(NotificationEntry notificationEntry) {
    }

    void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
    }

    void onRankingApplied() {
    }

    void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
    }

    void onEntryUpdated(NotificationEntry notificationEntry, boolean z) {
        onEntryUpdated(notificationEntry);
    }
}
