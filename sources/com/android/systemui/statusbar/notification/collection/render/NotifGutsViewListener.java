package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifGutsViewListener.kt */
public interface NotifGutsViewListener {
    void onGutsClose(@NotNull NotificationEntry notificationEntry);

    void onGutsOpen(@NotNull NotificationEntry notificationEntry, @NotNull NotificationGuts notificationGuts);
}
