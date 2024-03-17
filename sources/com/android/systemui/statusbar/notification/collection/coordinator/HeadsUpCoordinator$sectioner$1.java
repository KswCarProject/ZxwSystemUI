package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$sectioner$1 extends NotifSectioner {
    public final /* synthetic */ HeadsUpCoordinator this$0;

    @Nullable
    public NodeController getHeaderNodeController() {
        return null;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$sectioner$1(HeadsUpCoordinator headsUpCoordinator) {
        super("HeadsUp", 2);
        this.this$0 = headsUpCoordinator;
    }

    public boolean isInSection(@NotNull ListEntry listEntry) {
        return this.this$0.isGoingToShowHunNoRetract(listEntry);
    }

    @NotNull
    public NotifComparator getComparator() {
        return new HeadsUpCoordinator$sectioner$1$getComparator$1(this.this$0);
    }
}
