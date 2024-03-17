package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.qs.QS;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerController$onViewAttached$1<T> implements Consumer {
    public final /* synthetic */ NotificationsQSContainerController this$0;

    public NotificationsQSContainerController$onViewAttached$1(NotificationsQSContainerController notificationsQSContainerController) {
        this.this$0 = notificationsQSContainerController;
    }

    public final void accept(@NotNull QS qs) {
        qs.setContainerController(this.this$0);
    }
}
