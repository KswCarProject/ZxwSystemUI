package com.android.systemui.statusbar.phone;

import android.content.res.Configuration;
import java.util.function.Consumer;

/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerController$onViewAttached$2<T> implements Consumer {
    public final /* synthetic */ NotificationsQSContainerController this$0;

    public NotificationsQSContainerController$onViewAttached$2(NotificationsQSContainerController notificationsQSContainerController) {
        this.this$0 = notificationsQSContainerController;
    }

    public final void accept(Configuration configuration) {
        this.this$0.updateResources();
    }
}
