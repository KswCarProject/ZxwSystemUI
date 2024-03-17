package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: StackCoordinator.kt */
public /* synthetic */ class StackCoordinator$attach$1 implements OnAfterRenderListListener {
    public final /* synthetic */ StackCoordinator $tmp0;

    public StackCoordinator$attach$1(StackCoordinator stackCoordinator) {
        this.$tmp0 = stackCoordinator;
    }

    public final void onAfterRenderList(@NotNull List<? extends ListEntry> list, @NotNull NotifStackController notifStackController) {
        this.$tmp0.onAfterRenderList(list, notifStackController);
    }
}
