package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationStackScrollLayoutController$8$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationStackScrollLayoutController.AnonymousClass8 f$0;
    public final /* synthetic */ ExpandableNotificationRow f$1;

    public /* synthetic */ NotificationStackScrollLayoutController$8$$ExternalSyntheticLambda0(NotificationStackScrollLayoutController.AnonymousClass8 r1, ExpandableNotificationRow expandableNotificationRow) {
        this.f$0 = r1;
        this.f$1 = expandableNotificationRow;
    }

    public final void run() {
        this.f$0.lambda$onHeadsUpUnPinned$0(this.f$1);
    }
}
