package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingMessage;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ViewConfigCoordinator.kt */
public final class ViewConfigCoordinator implements Coordinator, NotificationLockscreenUserManager.UserChangedListener, ConfigurationController.ConfigurationListener {
    @NotNull
    public final ConfigurationController mConfigurationController;
    public boolean mDispatchUiModeChangeOnUserSwitched;
    @NotNull
    public final NotificationGutsManager mGutsManager;
    @NotNull
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    @NotNull
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    @Nullable
    public NotifPipeline mPipeline;
    public boolean mReinflateNotificationsOnUserSwitched;

    public ViewConfigCoordinator(@NotNull ConfigurationController configurationController, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull NotificationGutsManager notificationGutsManager, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mConfigurationController = configurationController;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mGutsManager = notificationGutsManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        this.mPipeline = notifPipeline;
        if (notifPipeline.isNewPipelineEnabled()) {
            this.mLockscreenUserManager.addUserChangedListener(this);
            this.mConfigurationController.addCallback(this);
        }
    }

    public void onDensityOrFontScaleChanged() {
        MessagingMessage.dropCache();
        MessagingGroup.dropCache();
        if (!this.mKeyguardUpdateMonitor.isSwitchingUser()) {
            updateNotificationsOnDensityOrFontScaleChanged();
        } else {
            this.mReinflateNotificationsOnUserSwitched = true;
        }
    }

    public void onUiModeChanged() {
        if (!this.mKeyguardUpdateMonitor.isSwitchingUser()) {
            updateNotificationsOnUiModeChanged();
        } else {
            this.mDispatchUiModeChangeOnUserSwitched = true;
        }
    }

    public void onThemeChanged() {
        onDensityOrFontScaleChanged();
    }

    public void onUserChanged(int i) {
        if (this.mReinflateNotificationsOnUserSwitched) {
            updateNotificationsOnDensityOrFontScaleChanged();
            this.mReinflateNotificationsOnUserSwitched = false;
        }
        if (this.mDispatchUiModeChangeOnUserSwitched) {
            updateNotificationsOnUiModeChanged();
            this.mDispatchUiModeChangeOnUserSwitched = false;
        }
    }

    public final void updateNotificationsOnUiModeChanged() {
        Collection<NotificationEntry> allNotifs;
        NotifPipeline notifPipeline = this.mPipeline;
        if (notifPipeline != null && (allNotifs = notifPipeline.getAllNotifs()) != null) {
            for (NotificationEntry row : allNotifs) {
                ExpandableNotificationRow row2 = row.getRow();
                if (row2 != null) {
                    row2.onUiModeChanged();
                }
            }
        }
    }

    public final void updateNotificationsOnDensityOrFontScaleChanged() {
        Collection<NotificationEntry> allNotifs;
        NotifPipeline notifPipeline = this.mPipeline;
        if (notifPipeline != null && (allNotifs = notifPipeline.getAllNotifs()) != null) {
            for (NotificationEntry notificationEntry : allNotifs) {
                notificationEntry.onDensityOrFontScaleChanged();
                if (notificationEntry.areGutsExposed()) {
                    this.mGutsManager.onDensityOrFontScaleChanged(notificationEntry);
                }
            }
        }
    }
}
