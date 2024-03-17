package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public /* synthetic */ class HeadsUpCoordinator$attach$2 implements OnBeforeFinalizeFilterListener {
    public final /* synthetic */ HeadsUpCoordinator $tmp0;

    public HeadsUpCoordinator$attach$2(HeadsUpCoordinator headsUpCoordinator) {
        this.$tmp0 = headsUpCoordinator;
    }

    public final void onBeforeFinalizeFilter(@NotNull List<? extends ListEntry> list) {
        this.$tmp0.onBeforeFinalizeFilter(list);
    }
}
