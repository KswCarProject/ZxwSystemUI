package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import java.io.PrintWriter;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationsController.kt */
public interface NotificationsController {
    void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr, boolean z);

    int getActiveNotificationsCount();

    void initialize(@NotNull NotificationPresenter notificationPresenter, @NotNull NotificationListContainer notificationListContainer, @NotNull NotifStackController notifStackController, @NotNull NotificationActivityStarter notificationActivityStarter, @NotNull NotificationRowBinderImpl.BindRowCallback bindRowCallback);

    void requestNotificationUpdate(@NotNull String str);

    void resetUserExpandedStates();

    void setNotificationSnoozed(@NotNull StatusBarNotification statusBarNotification, @NotNull NotificationSwipeActionHelper.SnoozeOption snoozeOption);
}
