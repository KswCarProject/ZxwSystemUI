package com.android.systemui.statusbar.notification;

import android.app.Notification;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Map;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager$onNotificationPanelExpandStateChanged$1<T, U, R> implements BiFunction {
    public final /* synthetic */ Map<String, NotificationEntry> $expanded;

    public ConversationNotificationManager$onNotificationPanelExpandStateChanged$1(Map<String, NotificationEntry> map) {
        this.$expanded = map;
    }

    @NotNull
    public final ConversationNotificationManager.ConversationState apply(@NotNull String str, @NotNull ConversationNotificationManager.ConversationState conversationState) {
        return this.$expanded.containsKey(str) ? ConversationNotificationManager.ConversationState.copy$default(conversationState, 0, (Notification) null, 2, (Object) null) : conversationState;
    }
}
