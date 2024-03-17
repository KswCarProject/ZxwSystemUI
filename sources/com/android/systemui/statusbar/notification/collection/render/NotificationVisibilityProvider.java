package com.android.systemui.statusbar.notification.collection.render;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationVisibilityProvider.kt */
public interface NotificationVisibilityProvider {
    @NotNull
    NotificationVisibility.NotificationLocation getLocation(@NotNull String str);

    @NotNull
    NotificationVisibility obtain(@NotNull NotificationEntry notificationEntry, boolean z);

    @NotNull
    NotificationVisibility obtain(@NotNull String str, boolean z);
}
