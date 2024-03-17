package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: DataStoreCoordinator.kt */
public final class DataStoreCoordinator implements Coordinator {
    @NotNull
    public final NotifLiveDataStoreImpl notifLiveDataStoreImpl;

    public DataStoreCoordinator(@NotNull NotifLiveDataStoreImpl notifLiveDataStoreImpl2) {
        this.notifLiveDataStoreImpl = notifLiveDataStoreImpl2;
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        notifPipeline.addOnAfterRenderListListener(new DataStoreCoordinator$attach$1(this));
    }

    public final void onAfterRenderList(@NotNull List<? extends ListEntry> list) {
        this.notifLiveDataStoreImpl.setActiveNotifList(flattenedEntryList(list));
    }

    public final List<NotificationEntry> flattenedEntryList(List<? extends ListEntry> list) {
        ArrayList arrayList = new ArrayList();
        for (ListEntry listEntry : list) {
            if (listEntry instanceof NotificationEntry) {
                arrayList.add(listEntry);
            } else if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry summary = groupEntry.getSummary();
                if (summary != null) {
                    arrayList.add(summary);
                    arrayList.addAll(groupEntry.getChildren());
                } else {
                    throw new IllegalStateException(Intrinsics.stringPlus("No Summary: ", groupEntry).toString());
                }
            } else {
                throw new IllegalStateException(Intrinsics.stringPlus("Unexpected entry ", listEntry).toString());
            }
        }
        return arrayList;
    }
}
