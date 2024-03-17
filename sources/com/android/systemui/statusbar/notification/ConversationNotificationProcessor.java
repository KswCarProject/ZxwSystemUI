package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationProcessor {
    @NotNull
    public final ConversationNotificationManager conversationNotificationManager;
    @NotNull
    public final LauncherApps launcherApps;

    public ConversationNotificationProcessor(@NotNull LauncherApps launcherApps2, @NotNull ConversationNotificationManager conversationNotificationManager2) {
        this.launcherApps = launcherApps2;
        this.conversationNotificationManager = conversationNotificationManager2;
    }

    public final void processNotification(@NotNull NotificationEntry notificationEntry, @NotNull Notification.Builder builder) {
        Notification.Style style = builder.getStyle();
        Notification.MessagingStyle messagingStyle = style instanceof Notification.MessagingStyle ? (Notification.MessagingStyle) style : null;
        if (messagingStyle != null) {
            messagingStyle.setConversationType(notificationEntry.getRanking().getChannel().isImportantConversation() ? 2 : 1);
            ShortcutInfo conversationShortcutInfo = notificationEntry.getRanking().getConversationShortcutInfo();
            if (conversationShortcutInfo != null) {
                messagingStyle.setShortcutIcon(this.launcherApps.getShortcutIcon(conversationShortcutInfo));
                CharSequence label = conversationShortcutInfo.getLabel();
                if (label != null) {
                    messagingStyle.setConversationTitle(label);
                }
            }
            messagingStyle.setUnreadMessageCount(this.conversationNotificationManager.getUnreadCount(notificationEntry, builder));
        }
    }
}
