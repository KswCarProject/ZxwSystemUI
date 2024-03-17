package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager$onNotificationPanelExpandStateChanged$2 extends Lambda implements Function1<NotificationEntry, ExpandableNotificationRow> {
    public static final ConversationNotificationManager$onNotificationPanelExpandStateChanged$2 INSTANCE = new ConversationNotificationManager$onNotificationPanelExpandStateChanged$2();

    public ConversationNotificationManager$onNotificationPanelExpandStateChanged$2() {
        super(1);
    }

    @Nullable
    public final ExpandableNotificationRow invoke(@NotNull NotificationEntry notificationEntry) {
        return notificationEntry.getRow();
    }
}
