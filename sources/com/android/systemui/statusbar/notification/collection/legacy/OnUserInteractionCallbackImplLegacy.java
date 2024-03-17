package com.android.systemui.statusbar.notification.collection.legacy;

import android.service.notification.NotificationListenerService;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpManager;

public class OnUserInteractionCallbackImplLegacy implements OnUserInteractionCallback {
    public final GroupMembershipManager mGroupMembershipManager;
    public final HeadsUpManager mHeadsUpManager;
    public final NotificationEntryManager mNotificationEntryManager;
    public final StatusBarStateController mStatusBarStateController;
    public final NotificationVisibilityProvider mVisibilityProvider;
    public final VisualStabilityManager mVisualStabilityManager;

    public OnUserInteractionCallbackImplLegacy(NotificationEntryManager notificationEntryManager, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, VisualStabilityManager visualStabilityManager, GroupMembershipManager groupMembershipManager) {
        this.mNotificationEntryManager = notificationEntryManager;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mHeadsUpManager = headsUpManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mGroupMembershipManager = groupMembershipManager;
    }

    /* renamed from: onDismiss */
    public final void lambda$registerFutureDismissal$0(NotificationEntry notificationEntry, @NotificationListenerService.NotificationCancelReason int i, NotificationEntry notificationEntry2) {
        int i2;
        if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            i2 = 1;
        } else {
            i2 = this.mStatusBarStateController.isDozing() ? 2 : 3;
        }
        if (notificationEntry2 != null) {
            lambda$registerFutureDismissal$0(notificationEntry2, i, (NotificationEntry) null);
        }
        this.mNotificationEntryManager.performRemoveNotification(notificationEntry.getSbn(), new DismissedByUserStats(i2, 1, this.mVisibilityProvider.obtain(notificationEntry, true)), i);
    }

    public void onImportanceChanged(NotificationEntry notificationEntry) {
        this.mVisualStabilityManager.temporarilyAllowReordering();
    }

    public final NotificationEntry getGroupSummaryToDismiss(NotificationEntry notificationEntry) {
        if (!this.mGroupMembershipManager.isOnlyChildInGroup(notificationEntry)) {
            return null;
        }
        NotificationEntry logicalGroupSummary = this.mGroupMembershipManager.getLogicalGroupSummary(notificationEntry);
        if (logicalGroupSummary.isDismissable()) {
            return logicalGroupSummary;
        }
        return null;
    }

    public Runnable registerFutureDismissal(NotificationEntry notificationEntry, int i) {
        return new OnUserInteractionCallbackImplLegacy$$ExternalSyntheticLambda0(this, notificationEntry, i, getGroupSummaryToDismiss(notificationEntry));
    }
}
