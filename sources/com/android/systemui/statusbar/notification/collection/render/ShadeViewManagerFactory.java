package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeViewManager.kt */
public interface ShadeViewManagerFactory {
    @NotNull
    ShadeViewManager create(@NotNull NotificationListContainer notificationListContainer, @NotNull NotifStackController notifStackController);
}
