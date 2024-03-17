package com.android.systemui.statusbar.notification.stack;

public abstract class NotificationStackScrollLayoutListContainerModule {
    public static NotificationListContainer provideListContainer(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return notificationStackScrollLayoutController.getNotificationListContainer();
    }
}
