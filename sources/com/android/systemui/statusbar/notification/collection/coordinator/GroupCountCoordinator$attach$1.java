package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: GroupCountCoordinator.kt */
public /* synthetic */ class GroupCountCoordinator$attach$1 implements OnBeforeFinalizeFilterListener {
    public final /* synthetic */ GroupCountCoordinator $tmp0;

    public GroupCountCoordinator$attach$1(GroupCountCoordinator groupCountCoordinator) {
        this.$tmp0 = groupCountCoordinator;
    }

    public final void onBeforeFinalizeFilter(@NotNull List<? extends ListEntry> list) {
        this.$tmp0.onBeforeFinalizeFilter(list);
    }
}
