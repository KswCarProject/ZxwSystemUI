package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$6 extends Lambda implements Function1<HeadsUpCoordinator.PostedEntry, Boolean> {
    public final /* synthetic */ NotificationEntry $logicalSummary;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$6(NotificationEntry notificationEntry) {
        super(1);
        this.$logicalSummary = notificationEntry;
    }

    @NotNull
    public final Boolean invoke(@NotNull HeadsUpCoordinator.PostedEntry postedEntry) {
        return Boolean.valueOf(!Intrinsics.areEqual((Object) postedEntry.getKey(), (Object) this.$logicalSummary.getKey()));
    }
}
