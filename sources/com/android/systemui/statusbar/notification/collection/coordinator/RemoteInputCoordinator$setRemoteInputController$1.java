package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: RemoteInputCoordinator.kt */
public /* synthetic */ class RemoteInputCoordinator$setRemoteInputController$1 implements SmartReplyController.Callback {
    public final /* synthetic */ RemoteInputCoordinator $tmp0;

    public RemoteInputCoordinator$setRemoteInputController$1(RemoteInputCoordinator remoteInputCoordinator) {
        this.$tmp0 = remoteInputCoordinator;
    }

    public final void onSmartReplySent(@NotNull NotificationEntry notificationEntry, @NotNull CharSequence charSequence) {
        this.$tmp0.onSmartReplySent(notificationEntry, charSequence);
    }
}
