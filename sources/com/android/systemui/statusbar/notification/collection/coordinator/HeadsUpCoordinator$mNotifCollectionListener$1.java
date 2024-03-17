package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$mNotifCollectionListener$1 implements NotifCollectionListener {
    public final /* synthetic */ HeadsUpCoordinator this$0;

    public HeadsUpCoordinator$mNotifCollectionListener$1(HeadsUpCoordinator headsUpCoordinator) {
        this.this$0 = headsUpCoordinator;
    }

    public void onEntryAdded(@NotNull NotificationEntry notificationEntry) {
        this.this$0.mPostedEntries.put(notificationEntry.getKey(), new HeadsUpCoordinator.PostedEntry(notificationEntry, true, false, this.this$0.mNotificationInterruptStateProvider.shouldHeadsUp(notificationEntry), true, false, false));
    }

    public void onEntryUpdated(@NotNull NotificationEntry notificationEntry) {
        HeadsUpCoordinator.PostedEntry postedEntry = (HeadsUpCoordinator.PostedEntry) this.this$0.mPostedEntries.compute(notificationEntry.getKey(), new HeadsUpCoordinator$mNotifCollectionListener$1$onEntryUpdated$posted$1(notificationEntry, this.this$0.mNotificationInterruptStateProvider.shouldHeadsUp(notificationEntry), this.this$0.shouldHunAgain(notificationEntry), this.this$0.mHeadsUpManager.isAlerting(notificationEntry.getKey()), this.this$0.isEntryBinding(notificationEntry)));
        if (!(postedEntry != null && !postedEntry.getShouldHeadsUpEver())) {
            return;
        }
        if (postedEntry.isAlerting()) {
            this.this$0.mHeadsUpManager.removeNotification(postedEntry.getKey(), false);
        } else if (postedEntry.isBinding()) {
            this.this$0.cancelHeadsUpBind(postedEntry.getEntry());
        }
    }

    public void onEntryRemoved(@NotNull NotificationEntry notificationEntry, int i) {
        this.this$0.mPostedEntries.remove(notificationEntry.getKey());
        this.this$0.cancelHeadsUpBind(notificationEntry);
        String key = notificationEntry.getKey();
        if (this.this$0.mHeadsUpManager.isAlerting(key)) {
            this.this$0.mHeadsUpManager.removeNotification(notificationEntry.getKey(), this.this$0.mRemoteInputManager.isSpinning(key) && !NotificationRemoteInputManager.FORCE_REMOTE_INPUT_HISTORY);
        }
    }

    public void onEntryCleanUp(@NotNull NotificationEntry notificationEntry) {
        this.this$0.mHeadsUpViewBinder.abortBindCallback(notificationEntry);
    }
}
