package com.android.systemui.statusbar.notification;

import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationLaunchAnimatorController.kt */
public final class NotificationLaunchAnimatorControllerProvider {
    @NotNull
    public final HeadsUpManagerPhone headsUpManager;
    @NotNull
    public final InteractionJankMonitor jankMonitor;
    @NotNull
    public final NotificationListContainer notificationListContainer;
    @NotNull
    public final NotificationShadeWindowViewController notificationShadeWindowViewController;

    @NotNull
    public final NotificationLaunchAnimatorController getAnimatorController(@NotNull ExpandableNotificationRow expandableNotificationRow) {
        return getAnimatorController$default(this, expandableNotificationRow, (Runnable) null, 2, (Object) null);
    }

    public NotificationLaunchAnimatorControllerProvider(@NotNull NotificationShadeWindowViewController notificationShadeWindowViewController2, @NotNull NotificationListContainer notificationListContainer2, @NotNull HeadsUpManagerPhone headsUpManagerPhone, @NotNull InteractionJankMonitor interactionJankMonitor) {
        this.notificationShadeWindowViewController = notificationShadeWindowViewController2;
        this.notificationListContainer = notificationListContainer2;
        this.headsUpManager = headsUpManagerPhone;
        this.jankMonitor = interactionJankMonitor;
    }

    public static /* synthetic */ NotificationLaunchAnimatorController getAnimatorController$default(NotificationLaunchAnimatorControllerProvider notificationLaunchAnimatorControllerProvider, ExpandableNotificationRow expandableNotificationRow, Runnable runnable, int i, Object obj) {
        if ((i & 2) != 0) {
            runnable = null;
        }
        return notificationLaunchAnimatorControllerProvider.getAnimatorController(expandableNotificationRow, runnable);
    }

    @NotNull
    public final NotificationLaunchAnimatorController getAnimatorController(@NotNull ExpandableNotificationRow expandableNotificationRow, @Nullable Runnable runnable) {
        return new NotificationLaunchAnimatorController(this.notificationShadeWindowViewController, this.notificationListContainer, this.headsUpManager, expandableNotificationRow, this.jankMonitor, runnable);
    }
}
