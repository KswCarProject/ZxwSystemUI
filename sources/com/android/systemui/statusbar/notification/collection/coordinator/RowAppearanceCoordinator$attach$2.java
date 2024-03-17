package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderEntryListener;
import com.android.systemui.statusbar.notification.collection.render.NotifRowController;
import org.jetbrains.annotations.NotNull;

/* compiled from: RowAppearanceCoordinator.kt */
public /* synthetic */ class RowAppearanceCoordinator$attach$2 implements OnAfterRenderEntryListener {
    public final /* synthetic */ RowAppearanceCoordinator $tmp0;

    public RowAppearanceCoordinator$attach$2(RowAppearanceCoordinator rowAppearanceCoordinator) {
        this.$tmp0 = rowAppearanceCoordinator;
    }

    public final void onAfterRenderEntry(@NotNull NotificationEntry notificationEntry, @NotNull NotifRowController notifRowController) {
        this.$tmp0.onAfterRenderEntry(notificationEntry, notifRowController);
    }
}
