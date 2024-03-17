package com.android.systemui.statusbar.notification.stack;

import java.util.function.BiConsumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationStackScrollLayoutController$$ExternalSyntheticLambda14 implements BiConsumer {
    public final /* synthetic */ NotificationRoundnessManager f$0;

    public /* synthetic */ NotificationStackScrollLayoutController$$ExternalSyntheticLambda14(NotificationRoundnessManager notificationRoundnessManager) {
        this.f$0 = notificationRoundnessManager;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.setExpanded(((Float) obj).floatValue(), ((Float) obj2).floatValue());
    }
}
