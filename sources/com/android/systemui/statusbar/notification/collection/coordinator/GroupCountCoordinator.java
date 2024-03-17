package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.ArrayMap;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.NotifGroupController;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: GroupCountCoordinator.kt */
public final class GroupCountCoordinator implements Coordinator {
    @NotNull
    public final ArrayMap<GroupEntry, Integer> untruncatedChildCounts = new ArrayMap<>();

    public void attach(@NotNull NotifPipeline notifPipeline) {
        notifPipeline.addOnBeforeFinalizeFilterListener(new GroupCountCoordinator$attach$1(this));
        notifPipeline.addOnAfterRenderGroupListener(new GroupCountCoordinator$attach$2(this));
    }

    public final void onBeforeFinalizeFilter(List<? extends ListEntry> list) {
        this.untruncatedChildCounts.clear();
        for (GroupEntry groupEntry : SequencesKt___SequencesKt.filter(CollectionsKt___CollectionsKt.asSequence(list), GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1.INSTANCE)) {
            this.untruncatedChildCounts.put(groupEntry, Integer.valueOf(groupEntry.getChildren().size()));
        }
    }

    public final void onAfterRenderGroup(GroupEntry groupEntry, NotifGroupController notifGroupController) {
        Integer num = this.untruncatedChildCounts.get(groupEntry);
        if (num != null) {
            notifGroupController.setUntruncatedChildCount(num.intValue());
            return;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("No untruncated child count for group: ", groupEntry.getKey()).toString());
    }
}
