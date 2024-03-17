package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.List;
import java.util.Map;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$onBeforeFinalizeFilter$1$logicalMembersByGroup$1 extends Lambda implements Function1<NotificationEntry, Boolean> {
    public final /* synthetic */ Map<String, List<HeadsUpCoordinator.PostedEntry>> $postedEntriesByGroup;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$onBeforeFinalizeFilter$1$logicalMembersByGroup$1(Map<String, ? extends List<HeadsUpCoordinator.PostedEntry>> map) {
        super(1);
        this.$postedEntriesByGroup = map;
    }

    @NotNull
    public final Boolean invoke(@NotNull NotificationEntry notificationEntry) {
        return Boolean.valueOf(this.$postedEntriesByGroup.containsKey(notificationEntry.getSbn().getGroupKey()));
    }
}
