package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$findBestTransferChild$4 extends Lambda implements Function1<NotificationEntry, Comparable<?>> {
    public static final HeadsUpCoordinator$findBestTransferChild$4 INSTANCE = new HeadsUpCoordinator$findBestTransferChild$4();

    public HeadsUpCoordinator$findBestTransferChild$4() {
        super(1);
    }

    @Nullable
    public final Comparable<?> invoke(@NotNull NotificationEntry notificationEntry) {
        return Long.valueOf(-notificationEntry.getSbn().getNotification().when);
    }
}
