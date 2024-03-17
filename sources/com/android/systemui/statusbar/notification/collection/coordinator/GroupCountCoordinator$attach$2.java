package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderGroupListener;
import com.android.systemui.statusbar.notification.collection.render.NotifGroupController;
import org.jetbrains.annotations.NotNull;

/* compiled from: GroupCountCoordinator.kt */
public /* synthetic */ class GroupCountCoordinator$attach$2 implements OnAfterRenderGroupListener {
    public final /* synthetic */ GroupCountCoordinator $tmp0;

    public GroupCountCoordinator$attach$2(GroupCountCoordinator groupCountCoordinator) {
        this.$tmp0 = groupCountCoordinator;
    }

    public final void onAfterRenderGroup(@NotNull GroupEntry groupEntry, @NotNull NotifGroupController notifGroupController) {
        this.$tmp0.onAfterRenderGroup(groupEntry, notifGroupController);
    }
}
