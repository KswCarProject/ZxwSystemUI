package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import org.jetbrains.annotations.NotNull;

/* compiled from: GutsCoordinator.kt */
public final class GutsCoordinator$mLifetimeExtender$1 implements NotifLifetimeExtender {
    public final /* synthetic */ GutsCoordinator this$0;

    @NotNull
    public String getName() {
        return "GutsCoordinator";
    }

    public GutsCoordinator$mLifetimeExtender$1(GutsCoordinator gutsCoordinator) {
        this.this$0 = gutsCoordinator;
    }

    public void setCallback(@NotNull NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback) {
        this.this$0.onEndLifetimeExtensionCallback = onEndLifetimeExtensionCallback;
    }

    public boolean maybeExtendLifetime(@NotNull NotificationEntry notificationEntry, int i) {
        boolean access$isCurrentlyShowingGuts = this.this$0.isCurrentlyShowingGuts(notificationEntry);
        if (access$isCurrentlyShowingGuts) {
            this.this$0.notifsExtendingLifetime.add(notificationEntry.getKey());
        }
        return access$isCurrentlyShowingGuts;
    }

    public void cancelLifetimeExtension(@NotNull NotificationEntry notificationEntry) {
        this.this$0.notifsExtendingLifetime.remove(notificationEntry.getKey());
    }
}
