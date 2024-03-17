package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifViewBarn.kt */
public final class NotifViewBarn {
    @NotNull
    public final Map<String, NotifViewController> rowMap = new LinkedHashMap();

    @NotNull
    public final NodeController requireNodeController(@NotNull ListEntry listEntry) {
        NotifViewController notifViewController = this.rowMap.get(listEntry.getKey());
        if (notifViewController != null) {
            return notifViewController;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("No view has been registered for entry: ", listEntry.getKey()).toString());
    }

    @NotNull
    public final NotifGroupController requireGroupController(@NotNull NotificationEntry notificationEntry) {
        NotifViewController notifViewController = this.rowMap.get(notificationEntry.getKey());
        if (notifViewController != null) {
            return notifViewController;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("No view has been registered for entry: ", notificationEntry.getKey()).toString());
    }

    @NotNull
    public final NotifRowController requireRowController(@NotNull NotificationEntry notificationEntry) {
        NotifViewController notifViewController = this.rowMap.get(notificationEntry.getKey());
        if (notifViewController != null) {
            return notifViewController;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("No view has been registered for entry: ", notificationEntry.getKey()).toString());
    }

    public final void registerViewForEntry(@NotNull ListEntry listEntry, @NotNull NotifViewController notifViewController) {
        this.rowMap.put(listEntry.getKey(), notifViewController);
    }

    public final void removeViewForEntry(@NotNull ListEntry listEntry) {
        this.rowMap.remove(listEntry.getKey());
    }
}
