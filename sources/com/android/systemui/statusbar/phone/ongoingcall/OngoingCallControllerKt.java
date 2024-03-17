package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.Notification;
import android.util.Log;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: OngoingCallController.kt */
public final class OngoingCallControllerKt {
    public static final boolean DEBUG = Log.isLoggable("OngoingCallController", 3);

    public static final boolean isCallNotification(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().isStyle(Notification.CallStyle.class);
    }
}
