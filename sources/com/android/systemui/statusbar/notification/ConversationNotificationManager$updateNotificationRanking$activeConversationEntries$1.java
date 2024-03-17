package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager$updateNotificationRanking$activeConversationEntries$1 extends Lambda implements Function1<String, NotificationEntry> {
    public final /* synthetic */ ConversationNotificationManager this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$updateNotificationRanking$activeConversationEntries$1(ConversationNotificationManager conversationNotificationManager) {
        super(1);
        this.this$0 = conversationNotificationManager;
    }

    @Nullable
    public final NotificationEntry invoke(@NotNull String str) {
        return this.this$0.notifCollection.getEntry(str);
    }
}
