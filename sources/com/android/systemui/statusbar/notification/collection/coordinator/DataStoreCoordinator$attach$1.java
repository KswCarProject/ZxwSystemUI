package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: DataStoreCoordinator.kt */
public final class DataStoreCoordinator$attach$1 implements OnAfterRenderListListener {
    public final /* synthetic */ DataStoreCoordinator this$0;

    public DataStoreCoordinator$attach$1(DataStoreCoordinator dataStoreCoordinator) {
        this.this$0 = dataStoreCoordinator;
    }

    public final void onAfterRenderList(@NotNull List<ListEntry> list, @NotNull NotifStackController notifStackController) {
        this.this$0.onAfterRenderList(list);
    }
}
