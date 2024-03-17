package com.android.systemui.statusbar.phone;

import com.android.systemui.recents.OverviewProxyService;

/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerController$taskbarVisibilityListener$1 implements OverviewProxyService.OverviewProxyListener {
    public final /* synthetic */ NotificationsQSContainerController this$0;

    public NotificationsQSContainerController$taskbarVisibilityListener$1(NotificationsQSContainerController notificationsQSContainerController) {
        this.this$0 = notificationsQSContainerController;
    }

    public void onTaskbarStatusUpdated(boolean z, boolean z2) {
        this.this$0.taskbarVisible = z;
    }
}
