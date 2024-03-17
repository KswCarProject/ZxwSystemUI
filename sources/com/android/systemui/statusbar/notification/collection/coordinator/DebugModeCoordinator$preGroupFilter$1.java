package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import org.jetbrains.annotations.NotNull;

/* compiled from: DebugModeCoordinator.kt */
public final class DebugModeCoordinator$preGroupFilter$1 extends NotifFilter {
    public final /* synthetic */ DebugModeCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DebugModeCoordinator$preGroupFilter$1(DebugModeCoordinator debugModeCoordinator) {
        super("DebugModeCoordinator");
        this.this$0 = debugModeCoordinator;
    }

    public boolean shouldFilterOut(@NotNull NotificationEntry notificationEntry, long j) {
        return this.this$0.debugModeFilterProvider.shouldFilterOut(notificationEntry);
    }
}
