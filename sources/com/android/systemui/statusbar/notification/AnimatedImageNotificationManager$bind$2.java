package com.android.systemui.statusbar.notification;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Unit;

/* compiled from: ConversationNotifications.kt */
public final class AnimatedImageNotificationManager$bind$2 implements StatusBarStateController.StateListener {
    public final /* synthetic */ AnimatedImageNotificationManager this$0;

    public AnimatedImageNotificationManager$bind$2(AnimatedImageNotificationManager animatedImageNotificationManager) {
        this.this$0 = animatedImageNotificationManager;
    }

    public void onExpandedChanged(boolean z) {
        this.this$0.isStatusBarExpanded = z;
        AnimatedImageNotificationManager animatedImageNotificationManager = this.this$0;
        for (NotificationEntry access$updateAnimatedImageDrawables : this.this$0.notifCollection.getAllNotifs()) {
            Unit unused = animatedImageNotificationManager.updateAnimatedImageDrawables(access$updateAnimatedImageDrawables);
        }
    }
}
