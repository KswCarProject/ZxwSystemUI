package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$findAlertOverride$1 extends Lambda implements Function1<HeadsUpCoordinator.PostedEntry, Boolean> {
    public static final HeadsUpCoordinator$findAlertOverride$1 INSTANCE = new HeadsUpCoordinator$findAlertOverride$1();

    public HeadsUpCoordinator$findAlertOverride$1() {
        super(1);
    }

    @NotNull
    public final Boolean invoke(@NotNull HeadsUpCoordinator.PostedEntry postedEntry) {
        return Boolean.valueOf(!postedEntry.getEntry().getSbn().getNotification().isGroupSummary());
    }
}
