package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public interface KeyguardNotificationVisibilityProvider {
    void addOnStateChangedListener(@NotNull Consumer<String> consumer);

    boolean shouldHideNotification(@NotNull NotificationEntry notificationEntry);
}
