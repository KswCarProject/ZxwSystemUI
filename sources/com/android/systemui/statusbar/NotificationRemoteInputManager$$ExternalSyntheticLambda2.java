package com.android.systemui.statusbar;

import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationRemoteInputManager$$ExternalSyntheticLambda2 implements SmartReplyController.Callback {
    public final /* synthetic */ NotificationRemoteInputManager f$0;

    public /* synthetic */ NotificationRemoteInputManager$$ExternalSyntheticLambda2(NotificationRemoteInputManager notificationRemoteInputManager) {
        this.f$0 = notificationRemoteInputManager;
    }

    public final void onSmartReplySent(NotificationEntry notificationEntry, CharSequence charSequence) {
        this.f$0.lambda$setUpWithCallback$0(notificationEntry, charSequence);
    }
}
