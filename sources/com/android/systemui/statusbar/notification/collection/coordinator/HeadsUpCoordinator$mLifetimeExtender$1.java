package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$mLifetimeExtender$1 implements NotifLifetimeExtender {
    public final /* synthetic */ HeadsUpCoordinator this$0;

    @NotNull
    public String getName() {
        return "HeadsUpCoordinator";
    }

    public HeadsUpCoordinator$mLifetimeExtender$1(HeadsUpCoordinator headsUpCoordinator) {
        this.this$0 = headsUpCoordinator;
    }

    public void setCallback(@NotNull NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback) {
        this.this$0.mEndLifetimeExtension = onEndLifetimeExtensionCallback;
    }

    public boolean maybeExtendLifetime(@NotNull NotificationEntry notificationEntry, int i) {
        if (this.this$0.mHeadsUpManager.canRemoveImmediately(notificationEntry.getKey())) {
            return false;
        }
        if (this.this$0.isSticky(notificationEntry)) {
            this.this$0.mNotifsExtendingLifetime.put(notificationEntry, this.this$0.mExecutor.executeDelayed(new HeadsUpCoordinator$mLifetimeExtender$1$maybeExtendLifetime$1(this.this$0, notificationEntry), this.this$0.mHeadsUpManager.getEarliestRemovalTime(notificationEntry.getKey())));
            return true;
        }
        this.this$0.mExecutor.execute(new HeadsUpCoordinator$mLifetimeExtender$1$maybeExtendLifetime$2(this.this$0, notificationEntry));
        this.this$0.mNotifsExtendingLifetime.put(notificationEntry, (Object) null);
        return true;
    }

    public void cancelLifetimeExtension(@NotNull NotificationEntry notificationEntry) {
        Runnable runnable = (Runnable) this.this$0.mNotifsExtendingLifetime.remove(notificationEntry);
        if (runnable != null) {
            runnable.run();
        }
    }
}
