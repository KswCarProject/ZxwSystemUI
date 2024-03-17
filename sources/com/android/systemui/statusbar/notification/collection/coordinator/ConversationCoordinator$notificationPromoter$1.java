package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.NotificationChannel;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ConversationCoordinator.kt */
public final class ConversationCoordinator$notificationPromoter$1 extends NotifPromoter {
    public final /* synthetic */ ConversationCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ConversationCoordinator$notificationPromoter$1(ConversationCoordinator conversationCoordinator) {
        super("ConversationCoordinator");
        this.this$0 = conversationCoordinator;
    }

    public boolean shouldPromoteToTopLevel(@NotNull NotificationEntry notificationEntry) {
        NotificationChannel channel = notificationEntry.getChannel();
        boolean z = false;
        if (channel != null && channel.isImportantConversation()) {
            z = true;
        }
        if (z) {
            GroupEntry parent = notificationEntry.getParent();
            NotificationEntry summary = parent == null ? null : parent.getSummary();
            if (summary != null && Intrinsics.areEqual((Object) notificationEntry.getChannel(), (Object) summary.getChannel())) {
                this.this$0.promotedEntriesToSummaryOfSameChannel.put(notificationEntry, summary);
            }
        }
        return z;
    }
}
