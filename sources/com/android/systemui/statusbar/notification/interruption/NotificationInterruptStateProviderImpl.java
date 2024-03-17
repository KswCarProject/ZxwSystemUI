package com.android.systemui.statusbar.notification.interruption;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Handler;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.ArrayList;
import java.util.List;

public class NotificationInterruptStateProviderImpl implements NotificationInterruptStateProvider {
    public final AmbientDisplayConfiguration mAmbientDisplayConfiguration;
    public final BatteryController mBatteryController;
    public final ContentResolver mContentResolver;
    public final IDreamManager mDreamManager;
    public final NotifPipelineFlags mFlags;
    public final HeadsUpManager mHeadsUpManager;
    public final ContentObserver mHeadsUpObserver;
    public final KeyguardNotificationVisibilityProvider mKeyguardNotificationVisibilityProvider;
    public final NotificationInterruptLogger mLogger;
    public final NotificationFilter mNotificationFilter;
    public final PowerManager mPowerManager;
    public final StatusBarStateController mStatusBarStateController;
    public final List<NotificationInterruptSuppressor> mSuppressors = new ArrayList();
    @VisibleForTesting
    public boolean mUseHeadsUp = false;

    public NotificationInterruptStateProviderImpl(ContentResolver contentResolver, PowerManager powerManager, IDreamManager iDreamManager, AmbientDisplayConfiguration ambientDisplayConfiguration, NotificationFilter notificationFilter, BatteryController batteryController, StatusBarStateController statusBarStateController, HeadsUpManager headsUpManager, NotificationInterruptLogger notificationInterruptLogger, Handler handler, NotifPipelineFlags notifPipelineFlags, KeyguardNotificationVisibilityProvider keyguardNotificationVisibilityProvider) {
        this.mContentResolver = contentResolver;
        this.mPowerManager = powerManager;
        this.mDreamManager = iDreamManager;
        this.mBatteryController = batteryController;
        this.mAmbientDisplayConfiguration = ambientDisplayConfiguration;
        this.mNotificationFilter = notificationFilter;
        this.mStatusBarStateController = statusBarStateController;
        this.mHeadsUpManager = headsUpManager;
        this.mLogger = notificationInterruptLogger;
        this.mFlags = notifPipelineFlags;
        this.mKeyguardNotificationVisibilityProvider = keyguardNotificationVisibilityProvider;
        AnonymousClass1 r3 = new ContentObserver(handler) {
            public void onChange(boolean z) {
                NotificationInterruptStateProviderImpl notificationInterruptStateProviderImpl = NotificationInterruptStateProviderImpl.this;
                boolean z2 = notificationInterruptStateProviderImpl.mUseHeadsUp;
                boolean z3 = false;
                if (Settings.Global.getInt(notificationInterruptStateProviderImpl.mContentResolver, "heads_up_notifications_enabled", 0) != 0) {
                    z3 = true;
                }
                notificationInterruptStateProviderImpl.mUseHeadsUp = z3;
                NotificationInterruptStateProviderImpl.this.mLogger.logHeadsUpFeatureChanged(NotificationInterruptStateProviderImpl.this.mUseHeadsUp);
                NotificationInterruptStateProviderImpl notificationInterruptStateProviderImpl2 = NotificationInterruptStateProviderImpl.this;
                boolean z4 = notificationInterruptStateProviderImpl2.mUseHeadsUp;
                if (z2 != z4 && !z4) {
                    notificationInterruptStateProviderImpl2.mLogger.logWillDismissAll();
                    NotificationInterruptStateProviderImpl.this.mHeadsUpManager.releaseAllImmediately();
                }
            }
        };
        this.mHeadsUpObserver = r3;
        contentResolver.registerContentObserver(Settings.Global.getUriFor("heads_up_notifications_enabled"), true, r3);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("ticker_gets_heads_up"), true, r3);
        r3.onChange(true);
    }

    public void addSuppressor(NotificationInterruptSuppressor notificationInterruptSuppressor) {
        this.mSuppressors.add(notificationInterruptSuppressor);
    }

    public boolean shouldBubbleUp(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!canAlertCommon(notificationEntry) || !canAlertAwakeCommon(notificationEntry)) {
            return false;
        }
        if (!notificationEntry.canBubble()) {
            this.mLogger.logNoBubbleNotAllowed(sbn);
            return false;
        } else if (notificationEntry.getBubbleMetadata() != null && (notificationEntry.getBubbleMetadata().getShortcutId() != null || notificationEntry.getBubbleMetadata().getIntent() != null)) {
            return true;
        } else {
            this.mLogger.logNoBubbleNoMetadata(sbn);
            return false;
        }
    }

    public boolean shouldHeadsUp(NotificationEntry notificationEntry) {
        if (this.mStatusBarStateController.isDozing()) {
            return shouldHeadsUpWhenDozing(notificationEntry);
        }
        return shouldHeadsUpWhenAwake(notificationEntry);
    }

    public boolean shouldLaunchFullScreenIntentWhenAdded(NotificationEntry notificationEntry) {
        if (notificationEntry.getSbn().getNotification().fullScreenIntent == null || (shouldHeadsUp(notificationEntry) && this.mStatusBarStateController.getState() != 1)) {
            return false;
        }
        return true;
    }

    public final boolean shouldHeadsUpWhenAwake(NotificationEntry notificationEntry) {
        boolean z;
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!this.mUseHeadsUp) {
            this.mLogger.logNoHeadsUpFeatureDisabled();
            return false;
        } else if (!canAlertCommon(notificationEntry) || !canAlertAwakeCommon(notificationEntry)) {
            return false;
        } else {
            if (isSnoozedPackage(sbn)) {
                this.mLogger.logNoHeadsUpPackageSnoozed(sbn);
                return false;
            }
            boolean z2 = this.mStatusBarStateController.getState() == 0;
            if (notificationEntry.isBubble() && z2) {
                this.mLogger.logNoHeadsUpAlreadyBubbled(sbn);
                return false;
            } else if (notificationEntry.shouldSuppressPeek()) {
                this.mLogger.logNoHeadsUpSuppressedByDnd(sbn);
                return false;
            } else if (notificationEntry.getImportance() < 4) {
                this.mLogger.logNoHeadsUpNotImportant(sbn);
                return false;
            } else {
                try {
                    z = this.mDreamManager.isDreaming();
                } catch (RemoteException e) {
                    Log.e("InterruptionStateProvider", "Failed to query dream manager.", e);
                    z = false;
                }
                if (!(this.mPowerManager.isScreenOn() && !z)) {
                    this.mLogger.logNoHeadsUpNotInUse(sbn);
                    return false;
                }
                for (int i = 0; i < this.mSuppressors.size(); i++) {
                    if (this.mSuppressors.get(i).suppressAwakeHeadsUp(notificationEntry)) {
                        this.mLogger.logNoHeadsUpSuppressedBy(sbn, this.mSuppressors.get(i));
                        return false;
                    }
                }
                this.mLogger.logHeadsUp(sbn);
                return true;
            }
        }
    }

    public final boolean shouldHeadsUpWhenDozing(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!this.mAmbientDisplayConfiguration.pulseOnNotificationEnabled(-2)) {
            this.mLogger.logNoPulsingSettingDisabled(sbn);
            return false;
        } else if (this.mBatteryController.isAodPowerSave()) {
            this.mLogger.logNoPulsingBatteryDisabled(sbn);
            return false;
        } else if (!canAlertCommon(notificationEntry)) {
            this.mLogger.logNoPulsingNoAlert(sbn);
            return false;
        } else if (notificationEntry.shouldSuppressAmbient()) {
            this.mLogger.logNoPulsingNoAmbientEffect(sbn);
            return false;
        } else if (notificationEntry.getImportance() < 3) {
            this.mLogger.logNoPulsingNotImportant(sbn);
            return false;
        } else {
            this.mLogger.logPulsing(sbn);
            return true;
        }
    }

    public final boolean canAlertCommon(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!this.mFlags.isNewPipelineEnabled() && this.mNotificationFilter.shouldFilterOut(notificationEntry)) {
            this.mLogger.logNoAlertingFilteredOut(sbn);
            return false;
        } else if (!sbn.isGroup() || !sbn.getNotification().suppressAlertingDueToGrouping()) {
            for (int i = 0; i < this.mSuppressors.size(); i++) {
                if (this.mSuppressors.get(i).suppressInterruptions(notificationEntry)) {
                    this.mLogger.logNoAlertingSuppressedBy(sbn, this.mSuppressors.get(i), false);
                    return false;
                }
            }
            if (notificationEntry.hasJustLaunchedFullScreenIntent()) {
                this.mLogger.logNoAlertingRecentFullscreen(sbn);
                return false;
            } else if (!this.mKeyguardNotificationVisibilityProvider.shouldHideNotification(notificationEntry)) {
                return true;
            } else {
                this.mLogger.keyguardHideNotification(notificationEntry.getKey());
                return false;
            }
        } else {
            this.mLogger.logNoAlertingGroupAlertBehavior(sbn);
            return false;
        }
    }

    public final boolean canAlertAwakeCommon(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        for (int i = 0; i < this.mSuppressors.size(); i++) {
            if (this.mSuppressors.get(i).suppressAwakeInterruptions(notificationEntry)) {
                this.mLogger.logNoAlertingSuppressedBy(sbn, this.mSuppressors.get(i), true);
                return false;
            }
        }
        return true;
    }

    public final boolean isSnoozedPackage(StatusBarNotification statusBarNotification) {
        return this.mHeadsUpManager.isSnoozed(statusBarNotification.getPackageName());
    }
}
