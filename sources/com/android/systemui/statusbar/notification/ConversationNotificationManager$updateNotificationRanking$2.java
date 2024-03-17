package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.internal.widget.ConversationLayout;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.Nullable;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager$updateNotificationRanking$2 extends Lambda implements Function1<View, ConversationLayout> {
    public static final ConversationNotificationManager$updateNotificationRanking$2 INSTANCE = new ConversationNotificationManager$updateNotificationRanking$2();

    public ConversationNotificationManager$updateNotificationRanking$2() {
        super(1);
    }

    @Nullable
    public final ConversationLayout invoke(View view) {
        if (view instanceof ConversationLayout) {
            return (ConversationLayout) view;
        }
        return null;
    }
}
