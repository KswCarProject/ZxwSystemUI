package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.service.notification.StatusBarNotification;

public interface InternalNotifUpdater {
    void onInternalNotificationUpdate(StatusBarNotification statusBarNotification, String str);
}
