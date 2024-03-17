package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.Log;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import org.jetbrains.annotations.NotNull;

/* compiled from: RemoteInputCoordinator.kt */
public final class RemoteInputCoordinator$mCollectionListener$1 implements NotifCollectionListener {
    public final /* synthetic */ RemoteInputCoordinator this$0;

    public RemoteInputCoordinator$mCollectionListener$1(RemoteInputCoordinator remoteInputCoordinator) {
        this.this$0 = remoteInputCoordinator;
    }

    public void onEntryUpdated(@NotNull NotificationEntry notificationEntry, boolean z) {
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "mCollectionListener.onEntryUpdated(entry=" + notificationEntry.getKey() + ", fromSystem=" + z + ')');
        }
        if (z) {
            this.this$0.mSmartReplyController.stopSending(notificationEntry);
        }
    }

    public void onEntryRemoved(@NotNull NotificationEntry notificationEntry, int i) {
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "mCollectionListener.onEntryRemoved(entry=" + notificationEntry.getKey() + ')');
        }
        this.this$0.mSmartReplyController.stopSending(notificationEntry);
        if (i == 1 || i == 2) {
            this.this$0.mNotificationRemoteInputManager.cleanUpRemoteInputForUserRemoval(notificationEntry);
        }
    }
}
