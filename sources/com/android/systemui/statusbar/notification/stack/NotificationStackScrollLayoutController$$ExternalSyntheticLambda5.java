package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationStackScrollLayoutController$$ExternalSyntheticLambda5 implements GroupExpansionManager.OnGroupExpansionChangeListener {
    public final /* synthetic */ NotificationStackScrollLayoutController f$0;

    public /* synthetic */ NotificationStackScrollLayoutController$$ExternalSyntheticLambda5(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.f$0 = notificationStackScrollLayoutController;
    }

    public final void onGroupExpansionChange(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        this.f$0.lambda$attach$9(expandableNotificationRow, z);
    }
}
