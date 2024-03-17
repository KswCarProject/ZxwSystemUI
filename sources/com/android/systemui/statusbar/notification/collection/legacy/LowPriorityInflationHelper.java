package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;

public class LowPriorityInflationHelper {
    public final NotificationGroupManagerLegacy mGroupManager;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final RowContentBindStage mRowContentBindStage;

    public LowPriorityInflationHelper(NotificationGroupManagerLegacy notificationGroupManagerLegacy, RowContentBindStage rowContentBindStage, NotifPipelineFlags notifPipelineFlags) {
        this.mGroupManager = notificationGroupManagerLegacy;
        this.mRowContentBindStage = rowContentBindStage;
        this.mNotifPipelineFlags = notifPipelineFlags;
    }

    public void recheckLowPriorityViewAndInflate(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mRowContentBindStage.getStageParams(notificationEntry);
        boolean shouldUseLowPriorityView = shouldUseLowPriorityView(notificationEntry);
        if (!expandableNotificationRow.isRemoved() && expandableNotificationRow.isLowPriority() != shouldUseLowPriorityView) {
            rowContentBindParams.setUseLowPriority(shouldUseLowPriorityView);
            this.mRowContentBindStage.requestRebind(notificationEntry, new LowPriorityInflationHelper$$ExternalSyntheticLambda0(expandableNotificationRow, shouldUseLowPriorityView));
        }
    }

    public boolean shouldUseLowPriorityView(NotificationEntry notificationEntry) {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return notificationEntry.isAmbient() && !this.mGroupManager.isChildInGroup(notificationEntry);
    }
}
