package com.android.systemui.statusbar.notification.interruption;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.Objects;

public class HeadsUpController {
    public NotifCollectionListener mCollectionListener = new NotifCollectionListener() {
        public void onEntryAdded(NotificationEntry notificationEntry) {
            if (HeadsUpController.this.mInterruptStateProvider.shouldHeadsUp(notificationEntry)) {
                HeadsUpController.this.mHeadsUpViewBinder.bindHeadsUpView(notificationEntry, new HeadsUpController$1$$ExternalSyntheticLambda0(HeadsUpController.this));
            }
        }

        public void onEntryUpdated(NotificationEntry notificationEntry) {
            HeadsUpController.this.updateHunState(notificationEntry);
        }

        public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
            HeadsUpController.this.stopAlerting(notificationEntry);
        }

        public void onEntryCleanUp(NotificationEntry notificationEntry) {
            HeadsUpController.this.mHeadsUpViewBinder.abortBindCallback(notificationEntry);
        }
    };
    public final HeadsUpManager mHeadsUpManager;
    public final HeadsUpViewBinder mHeadsUpViewBinder;
    public final NotificationInterruptStateProvider mInterruptStateProvider;
    public final NotificationListener mNotificationListener;
    public OnHeadsUpChangedListener mOnHeadsUpChangedListener = new OnHeadsUpChangedListener() {
        public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
            if (!z && !notificationEntry.getRow().isRemoved()) {
                HeadsUpController.this.mHeadsUpViewBinder.unbindHeadsUpView(notificationEntry);
            }
        }
    };
    public final NotificationRemoteInputManager mRemoteInputManager;
    public final StatusBarStateController mStatusBarStateController;
    public final VisualStabilityManager mVisualStabilityManager;

    public HeadsUpController(HeadsUpViewBinder headsUpViewBinder, NotificationInterruptStateProvider notificationInterruptStateProvider, HeadsUpManager headsUpManager, NotificationRemoteInputManager notificationRemoteInputManager, StatusBarStateController statusBarStateController, VisualStabilityManager visualStabilityManager, NotificationListener notificationListener) {
        this.mHeadsUpViewBinder = headsUpViewBinder;
        this.mHeadsUpManager = headsUpManager;
        this.mInterruptStateProvider = notificationInterruptStateProvider;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mNotificationListener = notificationListener;
    }

    public void attach(NotificationEntryManager notificationEntryManager, HeadsUpManager headsUpManager) {
        notificationEntryManager.addCollectionListener(this.mCollectionListener);
        headsUpManager.addListener(this.mOnHeadsUpChangedListener);
    }

    public final void showAlertingView(NotificationEntry notificationEntry) {
        this.mHeadsUpManager.showNotification(notificationEntry);
        if (!this.mStatusBarStateController.isDozing()) {
            setNotificationShown(notificationEntry.getSbn());
        }
    }

    public final void updateHunState(NotificationEntry notificationEntry) {
        boolean alertAgain = alertAgain(notificationEntry, notificationEntry.getSbn().getNotification());
        boolean shouldHeadsUp = this.mInterruptStateProvider.shouldHeadsUp(notificationEntry);
        if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            if (shouldHeadsUp) {
                this.mHeadsUpManager.updateNotification(notificationEntry.getKey(), alertAgain);
            } else {
                this.mHeadsUpManager.removeNotification(notificationEntry.getKey(), false);
            }
        } else if (shouldHeadsUp && alertAgain) {
            HeadsUpViewBinder headsUpViewBinder = this.mHeadsUpViewBinder;
            HeadsUpManager headsUpManager = this.mHeadsUpManager;
            Objects.requireNonNull(headsUpManager);
            headsUpViewBinder.bindHeadsUpView(notificationEntry, new HeadsUpController$$ExternalSyntheticLambda0(headsUpManager));
        }
    }

    public final void setNotificationShown(StatusBarNotification statusBarNotification) {
        try {
            this.mNotificationListener.setNotificationsShown(new String[]{statusBarNotification.getKey()});
        } catch (RuntimeException e) {
            Log.d("HeadsUpBindController", "failed setNotificationsShown: ", e);
        }
    }

    public final void stopAlerting(NotificationEntry notificationEntry) {
        String key = notificationEntry.getKey();
        if (this.mHeadsUpManager.isAlerting(key)) {
            this.mHeadsUpManager.removeNotification(key, (this.mRemoteInputManager.isSpinning(key) && !NotificationRemoteInputManager.FORCE_REMOTE_INPUT_HISTORY) || !this.mVisualStabilityManager.isReorderingAllowed());
        }
    }

    public static boolean alertAgain(NotificationEntry notificationEntry, Notification notification) {
        return notificationEntry == null || !notificationEntry.hasInterrupted() || (notification.flags & 8) == 0;
    }
}
