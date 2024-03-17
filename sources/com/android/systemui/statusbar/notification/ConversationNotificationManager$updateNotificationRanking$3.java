package com.android.systemui.statusbar.notification;

import com.android.internal.widget.ConversationLayout;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager$updateNotificationRanking$3 extends Lambda implements Function1<ConversationLayout, Boolean> {
    public final /* synthetic */ boolean $important;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$updateNotificationRanking$3(boolean z) {
        super(1);
        this.$important = z;
    }

    @NotNull
    public final Boolean invoke(@NotNull ConversationLayout conversationLayout) {
        return Boolean.valueOf(conversationLayout.isImportantConversation() == this.$important);
    }
}
