package com.android.systemui.statusbar.notification;

import android.content.Intent;
import android.view.View;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

public interface NotificationActivityStarter {
    boolean isCollapsingToShowActivityOverLockscreen() {
        return false;
    }

    void onDragSuccess(NotificationEntry notificationEntry);

    void onNotificationClicked(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow);

    void startHistoryIntent(View view, boolean z);

    void startNotificationGutsIntent(Intent intent, int i, ExpandableNotificationRow expandableNotificationRow);
}
