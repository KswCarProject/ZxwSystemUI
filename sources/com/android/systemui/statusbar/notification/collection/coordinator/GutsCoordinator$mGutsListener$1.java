package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import org.jetbrains.annotations.NotNull;

/* compiled from: GutsCoordinator.kt */
public final class GutsCoordinator$mGutsListener$1 implements NotifGutsViewListener {
    public final /* synthetic */ GutsCoordinator this$0;

    public GutsCoordinator$mGutsListener$1(GutsCoordinator gutsCoordinator) {
        this.this$0 = gutsCoordinator;
    }

    public void onGutsOpen(@NotNull NotificationEntry notificationEntry, @NotNull NotificationGuts notificationGuts) {
        this.this$0.logger.logGutsOpened(notificationEntry.getKey(), notificationGuts);
        if (notificationGuts.isLeavebehind()) {
            this.this$0.closeGutsAndEndLifetimeExtension(notificationEntry);
        } else {
            this.this$0.notifsWithOpenGuts.add(notificationEntry.getKey());
        }
    }

    public void onGutsClose(@NotNull NotificationEntry notificationEntry) {
        this.this$0.logger.logGutsClosed(notificationEntry.getKey());
        this.this$0.closeGutsAndEndLifetimeExtension(notificationEntry);
    }
}
