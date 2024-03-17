package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$findBestTransferChild$1 extends Lambda implements Function1<NotificationEntry, Boolean> {
    public static final HeadsUpCoordinator$findBestTransferChild$1 INSTANCE = new HeadsUpCoordinator$findBestTransferChild$1();

    public HeadsUpCoordinator$findBestTransferChild$1() {
        super(1);
    }

    @NotNull
    public final Boolean invoke(@NotNull NotificationEntry notificationEntry) {
        return Boolean.valueOf(!notificationEntry.getSbn().getNotification().isGroupSummary());
    }
}
