package com.android.systemui.statusbar.notification;

import android.util.FloatProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationWakeUpCoordinator.kt */
public final class NotificationWakeUpCoordinator$mNotificationVisibility$1 extends FloatProperty<NotificationWakeUpCoordinator> {
    public NotificationWakeUpCoordinator$mNotificationVisibility$1() {
        super("notificationVisibility");
    }

    public void setValue(@NotNull NotificationWakeUpCoordinator notificationWakeUpCoordinator, float f) {
        notificationWakeUpCoordinator.setVisibilityAmount(f);
    }

    @Nullable
    public Float get(@NotNull NotificationWakeUpCoordinator notificationWakeUpCoordinator) {
        return Float.valueOf(notificationWakeUpCoordinator.mLinearVisibilityAmount);
    }
}
