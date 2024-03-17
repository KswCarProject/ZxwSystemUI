package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifStabilityManager.kt */
public abstract class NotifStabilityManager extends Pluggable<NotifStabilityManager> {
    public abstract boolean isEntryReorderingAllowed(@NotNull ListEntry listEntry);

    public abstract boolean isEveryChangeAllowed();

    public abstract boolean isGroupChangeAllowed(@NotNull NotificationEntry notificationEntry);

    public abstract boolean isGroupPruneAllowed(@NotNull GroupEntry groupEntry);

    public abstract boolean isPipelineRunAllowed();

    public abstract boolean isSectionChangeAllowed(@NotNull NotificationEntry notificationEntry);

    public abstract void onBeginRun();

    public abstract void onEntryReorderSuppressed();

    public NotifStabilityManager(@NotNull String str) {
        super(str);
    }
}
