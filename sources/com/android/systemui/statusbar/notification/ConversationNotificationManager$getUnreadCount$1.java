package com.android.systemui.statusbar.notification;

import android.app.Notification;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager$getUnreadCount$1<T, U, R> implements BiFunction {
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ Notification.Builder $recoveredBuilder;
    public final /* synthetic */ ConversationNotificationManager this$0;

    public ConversationNotificationManager$getUnreadCount$1(NotificationEntry notificationEntry, ConversationNotificationManager conversationNotificationManager, Notification.Builder builder) {
        this.$entry = notificationEntry;
        this.this$0 = conversationNotificationManager;
        this.$recoveredBuilder = builder;
    }

    @Nullable
    public final ConversationNotificationManager.ConversationState apply(@NotNull String str, @Nullable ConversationNotificationManager.ConversationState conversationState) {
        int i = 1;
        if (conversationState != null) {
            i = this.this$0.shouldIncrementUnread(conversationState, this.$recoveredBuilder) ? conversationState.getUnreadCount() + 1 : conversationState.getUnreadCount();
        }
        return new ConversationNotificationManager.ConversationState(i, this.$entry.getSbn().getNotification());
    }
}
