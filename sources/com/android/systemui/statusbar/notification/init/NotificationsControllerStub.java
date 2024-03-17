package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import java.io.PrintWriter;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationsControllerStub.kt */
public final class NotificationsControllerStub implements NotificationsController {
    @NotNull
    public final NotificationListener notificationListener;

    public int getActiveNotificationsCount() {
        return 0;
    }

    public void requestNotificationUpdate(@NotNull String str) {
    }

    public void resetUserExpandedStates() {
    }

    public void setNotificationSnoozed(@NotNull StatusBarNotification statusBarNotification, @NotNull NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
    }

    public NotificationsControllerStub(@NotNull NotificationListener notificationListener2) {
        this.notificationListener = notificationListener2;
    }

    public void initialize(@NotNull NotificationPresenter notificationPresenter, @NotNull NotificationListContainer notificationListContainer, @NotNull NotifStackController notifStackController, @NotNull NotificationActivityStarter notificationActivityStarter, @NotNull NotificationRowBinderImpl.BindRowCallback bindRowCallback) {
        this.notificationListener.registerAsSystemService();
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr, boolean z) {
        printWriter.println();
        printWriter.println("Notification handling disabled");
        printWriter.println();
    }
}
