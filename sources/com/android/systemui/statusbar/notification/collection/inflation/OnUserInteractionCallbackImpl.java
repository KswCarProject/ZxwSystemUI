package com.android.systemui.statusbar.notification.collection.inflation;

import android.os.SystemClock;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpManager;

public class OnUserInteractionCallbackImpl implements OnUserInteractionCallback {
    public final HeadsUpManager mHeadsUpManager;
    public final NotifCollection mNotifCollection;
    public final StatusBarStateController mStatusBarStateController;
    public final NotificationVisibilityProvider mVisibilityProvider;
    public final VisualStabilityCoordinator mVisualStabilityCoordinator;

    public OnUserInteractionCallbackImpl(NotificationVisibilityProvider notificationVisibilityProvider, NotifCollection notifCollection, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, VisualStabilityCoordinator visualStabilityCoordinator) {
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mNotifCollection = notifCollection;
        this.mHeadsUpManager = headsUpManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mVisualStabilityCoordinator = visualStabilityCoordinator;
    }

    public final DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry) {
        int i;
        if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            i = 1;
        } else {
            i = this.mStatusBarStateController.isDozing() ? 2 : 3;
        }
        return new DismissedByUserStats(i, 1, this.mVisibilityProvider.obtain(notificationEntry, true));
    }

    public void onImportanceChanged(NotificationEntry notificationEntry) {
        this.mVisualStabilityCoordinator.temporarilyAllowSectionChanges(notificationEntry, SystemClock.uptimeMillis());
    }

    public Runnable registerFutureDismissal(NotificationEntry notificationEntry, int i) {
        return this.mNotifCollection.registerFutureDismissal(notificationEntry, i, new OnUserInteractionCallbackImpl$$ExternalSyntheticLambda0(this));
    }
}
