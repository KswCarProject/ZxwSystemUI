package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager$onEntryViewBound$1 implements ExpandableNotificationRow.OnExpansionChangedListener {
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ ConversationNotificationManager this$0;

    public ConversationNotificationManager$onEntryViewBound$1(NotificationEntry notificationEntry, ConversationNotificationManager conversationNotificationManager) {
        this.$entry = notificationEntry;
        this.this$0 = conversationNotificationManager;
    }

    public final void onExpansionChanged(final boolean z) {
        ExpandableNotificationRow row = this.$entry.getRow();
        boolean z2 = false;
        if (row != null && row.isShown()) {
            z2 = true;
        }
        if (!z2 || !z) {
            ConversationNotificationManager.onEntryViewBound$updateCount(this.this$0, this.$entry, z);
            return;
        }
        ExpandableNotificationRow row2 = this.$entry.getRow();
        final ConversationNotificationManager conversationNotificationManager = this.this$0;
        final NotificationEntry notificationEntry = this.$entry;
        row2.performOnIntrinsicHeightReached(new Runnable() {
            public final void run() {
                ConversationNotificationManager.onEntryViewBound$updateCount(conversationNotificationManager, notificationEntry, z);
            }
        });
    }
}
