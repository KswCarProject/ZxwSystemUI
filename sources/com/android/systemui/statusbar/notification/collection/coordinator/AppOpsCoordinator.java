package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.util.concurrency.DelayableExecutor;

public class AppOpsCoordinator implements Coordinator {
    public final AppOpsController mAppOpsController;
    public final ForegroundServiceController mForegroundServiceController;
    public final DelayableExecutor mMainExecutor;
    public final NotifFilter mNotifFilter = new NotifFilter("AppOpsCoordinator") {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            StatusBarNotification sbn = notificationEntry.getSbn();
            return AppOpsCoordinator.this.mForegroundServiceController.isDisclosureNotification(sbn) && !AppOpsCoordinator.this.mForegroundServiceController.isDisclosureNeededForUser(sbn.getUser().getIdentifier());
        }
    };
    public NotifPipeline mNotifPipeline;
    public final NotifSectioner mNotifSectioner = new NotifSectioner("ForegroundService", 3) {
        public boolean isInSection(ListEntry listEntry) {
            NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
            if (representativeEntry == null) {
                return false;
            }
            if (isColorizedForegroundService(representativeEntry) || isCall(representativeEntry)) {
                return true;
            }
            return false;
        }

        public final boolean isColorizedForegroundService(NotificationEntry notificationEntry) {
            Notification notification = notificationEntry.getSbn().getNotification();
            if (!notification.isForegroundService() || !notification.isColorized() || notificationEntry.getImportance() <= 1) {
                return false;
            }
            return true;
        }

        public final boolean isCall(NotificationEntry notificationEntry) {
            Notification notification = notificationEntry.getSbn().getNotification();
            if (notificationEntry.getImportance() <= 1 || !notification.isStyle(Notification.CallStyle.class)) {
                return false;
            }
            return true;
        }
    };

    public AppOpsCoordinator(ForegroundServiceController foregroundServiceController, AppOpsController appOpsController, DelayableExecutor delayableExecutor) {
        this.mForegroundServiceController = foregroundServiceController;
        this.mAppOpsController = appOpsController;
        this.mMainExecutor = delayableExecutor;
    }

    public void attach(NotifPipeline notifPipeline) {
        this.mNotifPipeline = notifPipeline;
        notifPipeline.addPreGroupFilter(this.mNotifFilter);
    }

    public NotifSectioner getSectioner() {
        return this.mNotifSectioner;
    }
}
