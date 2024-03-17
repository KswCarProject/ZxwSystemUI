package com.android.systemui.statusbar.notification;

import android.app.AppGlobals;
import android.content.pm.IPackageManager;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.media.MediaDataManagerKt;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;

public class NotificationFilter {
    public final DebugModeFilterProvider mDebugNotificationFilter;
    public final ForegroundServiceController mForegroundServiceController;
    public final Boolean mIsMediaFlagEnabled;
    public final NotificationEntryManager.KeyguardEnvironment mKeyguardEnvironment;
    public final StatusBarStateController mStatusBarStateController;
    public final NotificationLockscreenUserManager mUserManager;

    public NotificationFilter(DebugModeFilterProvider debugModeFilterProvider, StatusBarStateController statusBarStateController, NotificationEntryManager.KeyguardEnvironment keyguardEnvironment, ForegroundServiceController foregroundServiceController, NotificationLockscreenUserManager notificationLockscreenUserManager, MediaFeatureFlag mediaFeatureFlag) {
        this.mDebugNotificationFilter = debugModeFilterProvider;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardEnvironment = keyguardEnvironment;
        this.mForegroundServiceController = foregroundServiceController;
        this.mUserManager = notificationLockscreenUserManager;
        this.mIsMediaFlagEnabled = Boolean.valueOf(mediaFeatureFlag.getEnabled());
    }

    public boolean shouldFilterOut(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (this.mDebugNotificationFilter.shouldFilterOut(notificationEntry)) {
            return true;
        }
        if ((!this.mKeyguardEnvironment.isDeviceProvisioned() && !showNotificationEvenIfUnprovisioned(sbn)) || !this.mKeyguardEnvironment.isNotificationForCurrentProfiles(sbn)) {
            return true;
        }
        if (this.mUserManager.isLockscreenPublicMode(sbn.getUserId()) && (sbn.getNotification().visibility == -1 || this.mUserManager.shouldHideNotifications(sbn.getUserId()) || this.mUserManager.shouldHideNotifications(sbn.getKey()))) {
            return true;
        }
        if (this.mStatusBarStateController.isDozing() && notificationEntry.shouldSuppressAmbient()) {
            return true;
        }
        if ((!this.mStatusBarStateController.isDozing() && notificationEntry.shouldSuppressNotificationList()) || notificationEntry.getRanking().isSuspended()) {
            return true;
        }
        if (this.mForegroundServiceController.isDisclosureNotification(sbn) && !this.mForegroundServiceController.isDisclosureNeededForUser(sbn.getUserId())) {
            return true;
        }
        if (!this.mIsMediaFlagEnabled.booleanValue() || !MediaDataManagerKt.isMediaNotification(sbn)) {
            return false;
        }
        return true;
    }

    public static boolean showNotificationEvenIfUnprovisioned(StatusBarNotification statusBarNotification) {
        return showNotificationEvenIfUnprovisioned(AppGlobals.getPackageManager(), statusBarNotification);
    }

    @VisibleForTesting
    public static boolean showNotificationEvenIfUnprovisioned(IPackageManager iPackageManager, StatusBarNotification statusBarNotification) {
        return checkUidPermission(iPackageManager, "android.permission.NOTIFICATION_DURING_SETUP", statusBarNotification.getUid()) == 0 && statusBarNotification.getNotification().extras.getBoolean("android.allowDuringSetup");
    }

    public static int checkUidPermission(IPackageManager iPackageManager, String str, int i) {
        try {
            return iPackageManager.checkUidPermission(str, i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
