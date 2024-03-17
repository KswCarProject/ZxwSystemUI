package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.Trace;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.collection.render.NotifStats;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: StackCoordinator.kt */
public final class StackCoordinator implements Coordinator {
    @NotNull
    public final NotificationIconAreaController notificationIconAreaController;

    public final void onAfterRenderList(@NotNull List<? extends ListEntry> list, @NotNull NotifStackController notifStackController) {
        Trace.beginSection("StackCoordinator.onAfterRenderList");
        try {
            notifStackController.setNotifStats(calculateNotifStats(list));
            this.notificationIconAreaController.updateNotificationIcons(list);
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public StackCoordinator(@NotNull NotificationIconAreaController notificationIconAreaController2) {
        this.notificationIconAreaController = notificationIconAreaController2;
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        notifPipeline.addOnAfterRenderListListener(new StackCoordinator$attach$1(this));
    }

    public final NotifStats calculateNotifStats(List<? extends ListEntry> list) {
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        for (ListEntry listEntry : list) {
            NotifSection section = listEntry.getSection();
            if (section != null) {
                NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
                if (representativeEntry != null) {
                    boolean z5 = section.getBucket() == 6;
                    boolean isClearable = representativeEntry.isClearable();
                    if (z5 && isClearable) {
                        z4 = true;
                    } else if (z5 && !isClearable) {
                        z3 = true;
                    } else if (!z5 && isClearable) {
                        z2 = true;
                    } else if (!z5 && !isClearable) {
                        z = true;
                    }
                } else {
                    throw new IllegalStateException(Intrinsics.stringPlus("Null notif entry for ", listEntry.getKey()).toString());
                }
            } else {
                throw new IllegalStateException(Intrinsics.stringPlus("Null section for ", listEntry.getKey()).toString());
            }
        }
        return new NotifStats(list.size(), z, z2, z3, z4);
    }
}
