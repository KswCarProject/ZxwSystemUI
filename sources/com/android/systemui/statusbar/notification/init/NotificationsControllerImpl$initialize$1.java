package com.android.systemui.statusbar.notification.init;

/* compiled from: NotificationsControllerImpl.kt */
public final class NotificationsControllerImpl$initialize$1 implements Runnable {
    public final /* synthetic */ NotificationsControllerImpl this$0;

    public NotificationsControllerImpl$initialize$1(NotificationsControllerImpl notificationsControllerImpl) {
        this.this$0 = notificationsControllerImpl;
    }

    public final void run() {
        this.this$0.entryManager.updateNotifications("debug mode filter changed");
    }
}
