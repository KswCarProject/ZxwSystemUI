package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$sectioner$1$getComparator$1 extends NotifComparator {
    public final /* synthetic */ HeadsUpCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$sectioner$1$getComparator$1(HeadsUpCoordinator headsUpCoordinator) {
        super("HeadsUp");
        this.this$0 = headsUpCoordinator;
    }

    public int compare(@NotNull ListEntry listEntry, @NotNull ListEntry listEntry2) {
        return this.this$0.mHeadsUpManager.compare(listEntry.getRepresentativeEntry(), listEntry2.getRepresentativeEntry());
    }
}
