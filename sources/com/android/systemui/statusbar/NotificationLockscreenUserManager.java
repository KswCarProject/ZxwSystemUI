package com.android.systemui.statusbar;

import android.content.pm.UserInfo;
import android.util.SparseArray;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;

public interface NotificationLockscreenUserManager {

    public interface KeyguardNotificationSuppressor {
        boolean shouldSuppressOnKeyguard(NotificationEntry notificationEntry);
    }

    public interface NotificationStateChangedListener {
        void onNotificationStateChanged();
    }

    public interface UserChangedListener {
        void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
        }

        void onUserChanged(int i) {
        }

        void onUserRemoved(int i) {
        }
    }

    void addKeyguardNotificationSuppressor(KeyguardNotificationSuppressor keyguardNotificationSuppressor);

    void addNotificationStateChangedListener(NotificationStateChangedListener notificationStateChangedListener);

    void addUserChangedListener(UserChangedListener userChangedListener);

    int getCurrentUserId();

    boolean isAnyProfilePublicMode();

    boolean isCurrentProfile(int i);

    boolean isLockscreenPublicMode(int i);

    boolean needsRedaction(NotificationEntry notificationEntry);

    boolean needsSeparateWorkChallenge(int i) {
        return false;
    }

    void setUpWithPresenter(NotificationPresenter notificationPresenter);

    boolean shouldAllowLockscreenRemoteInput();

    boolean shouldHideNotifications(int i);

    boolean shouldHideNotifications(String str);

    boolean shouldShowLockscreenNotifications();

    boolean shouldShowOnKeyguard(NotificationEntry notificationEntry);

    void updatePublicMode();

    boolean userAllowsNotificationsInPublic(int i);

    boolean userAllowsPrivateNotificationsInPublic(int i);
}