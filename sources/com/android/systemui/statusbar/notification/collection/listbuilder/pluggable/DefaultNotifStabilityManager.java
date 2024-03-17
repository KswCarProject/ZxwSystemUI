package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifStabilityManager.kt */
public final class DefaultNotifStabilityManager extends NotifStabilityManager {
    @NotNull
    public static final DefaultNotifStabilityManager INSTANCE = new DefaultNotifStabilityManager();

    public boolean isEntryReorderingAllowed(@NotNull ListEntry listEntry) {
        return true;
    }

    public boolean isEveryChangeAllowed() {
        return true;
    }

    public boolean isGroupChangeAllowed(@NotNull NotificationEntry notificationEntry) {
        return true;
    }

    public boolean isGroupPruneAllowed(@NotNull GroupEntry groupEntry) {
        return true;
    }

    public boolean isPipelineRunAllowed() {
        return true;
    }

    public boolean isSectionChangeAllowed(@NotNull NotificationEntry notificationEntry) {
        return true;
    }

    public void onBeginRun() {
    }

    public void onEntryReorderSuppressed() {
    }

    public DefaultNotifStabilityManager() {
        super("DefaultNotifStabilityManager");
    }
}
