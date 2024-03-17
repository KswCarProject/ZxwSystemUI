package com.android.systemui.statusbar.notification;

import com.android.internal.widget.ConversationLayout;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager$updateNotificationRanking$4$1 implements Runnable {
    public final /* synthetic */ boolean $important;
    public final /* synthetic */ ConversationLayout $layout;

    public ConversationNotificationManager$updateNotificationRanking$4$1(ConversationLayout conversationLayout, boolean z) {
        this.$layout = conversationLayout;
        this.$important = z;
    }

    public final void run() {
        this.$layout.setIsImportantConversation(this.$important, true);
    }
}
