package com.android.systemui.statusbar.notification;

import android.app.Notification;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager$resetCount$1<T, U, R> implements BiFunction {
    public static final ConversationNotificationManager$resetCount$1<T, U, R> INSTANCE = new ConversationNotificationManager$resetCount$1<>();

    @Nullable
    public final ConversationNotificationManager.ConversationState apply(@NotNull String str, @Nullable ConversationNotificationManager.ConversationState conversationState) {
        if (conversationState == null) {
            return null;
        }
        return ConversationNotificationManager.ConversationState.copy$default(conversationState, 0, (Notification) null, 2, (Object) null);
    }
}
