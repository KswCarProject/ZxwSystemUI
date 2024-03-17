package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OnUserInteractionCallbackImpl$$ExternalSyntheticLambda0 implements NotifCollection.DismissedByUserStatsCreator {
    public final /* synthetic */ OnUserInteractionCallbackImpl f$0;

    public /* synthetic */ OnUserInteractionCallbackImpl$$ExternalSyntheticLambda0(OnUserInteractionCallbackImpl onUserInteractionCallbackImpl) {
        this.f$0 = onUserInteractionCallbackImpl;
    }

    public final DismissedByUserStats createDismissedByUserStats(NotificationEntry notificationEntry) {
        return this.f$0.getDismissedByUserStats(notificationEntry);
    }
}
