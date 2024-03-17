package com.android.systemui.statusbar.notification;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import java.util.Objects;

public class NotificationListController {
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() {
        public void onDeviceProvisionedChanged() {
            NotificationListController.this.mEntryManager.updateNotifications("device provisioned changed");
        }
    };
    public final NotificationEntryListener mEntryListener = new NotificationEntryListener() {
        public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
            NotificationListController.this.mListContainer.cleanUpViewStateForEntry(notificationEntry);
        }
    };
    public final NotificationEntryManager mEntryManager;
    public final NotificationListContainer mListContainer;

    public NotificationListController(NotificationEntryManager notificationEntryManager, NotificationListContainer notificationListContainer, DeviceProvisionedController deviceProvisionedController) {
        Objects.requireNonNull(notificationEntryManager);
        this.mEntryManager = notificationEntryManager;
        Objects.requireNonNull(notificationListContainer);
        this.mListContainer = notificationListContainer;
        Objects.requireNonNull(deviceProvisionedController);
        this.mDeviceProvisionedController = deviceProvisionedController;
    }

    public void bind() {
        this.mEntryManager.addNotificationEntryListener(this.mEntryListener);
        this.mDeviceProvisionedController.addCallback(this.mDeviceProvisionedListener);
    }
}
