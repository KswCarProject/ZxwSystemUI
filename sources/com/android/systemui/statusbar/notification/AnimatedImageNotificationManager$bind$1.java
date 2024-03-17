package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

/* compiled from: ConversationNotifications.kt */
public final class AnimatedImageNotificationManager$bind$1 implements OnHeadsUpChangedListener {
    public final /* synthetic */ AnimatedImageNotificationManager this$0;

    public AnimatedImageNotificationManager$bind$1(AnimatedImageNotificationManager animatedImageNotificationManager) {
        this.this$0 = animatedImageNotificationManager;
    }

    public void onHeadsUpStateChanged(@NotNull NotificationEntry notificationEntry, boolean z) {
        Unit unused = this.this$0.updateAnimatedImageDrawables(notificationEntry);
    }
}
