package com.android.systemui.statusbar.notification.stack;

import java.util.ArrayList;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationStackScrollLayout$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ NotificationStackScrollLayout f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ NotificationStackScrollLayout$$ExternalSyntheticLambda0(NotificationStackScrollLayout notificationStackScrollLayout, ArrayList arrayList, int i) {
        this.f$0 = notificationStackScrollLayout;
        this.f$1 = arrayList;
        this.f$2 = i;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$clearNotifications$8(this.f$1, this.f$2, (Boolean) obj);
    }
}
