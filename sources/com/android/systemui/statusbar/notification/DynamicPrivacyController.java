package com.android.systemui.statusbar.notification;

import android.util.ArraySet;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.Iterator;

public class DynamicPrivacyController implements KeyguardStateController.Callback {
    public boolean mCacheInvalid;
    public final KeyguardStateController mKeyguardStateController;
    public boolean mLastDynamicUnlocked;
    public final ArraySet<Listener> mListeners = new ArraySet<>();
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public final StatusBarStateController mStateController;
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;

    public interface Listener {
        void onDynamicPrivacyChanged();
    }

    public DynamicPrivacyController(NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController) {
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mStateController = statusBarStateController;
        this.mKeyguardStateController = keyguardStateController;
        keyguardStateController.addCallback(this);
        this.mLastDynamicUnlocked = isDynamicallyUnlocked();
    }

    public void onKeyguardFadingAwayChanged() {
        onUnlockedChanged();
    }

    public void onUnlockedChanged() {
        if (isDynamicPrivacyEnabled()) {
            boolean isDynamicallyUnlocked = isDynamicallyUnlocked();
            if (isDynamicallyUnlocked != this.mLastDynamicUnlocked || this.mCacheInvalid) {
                this.mLastDynamicUnlocked = isDynamicallyUnlocked;
                Iterator<Listener> it = this.mListeners.iterator();
                while (it.hasNext()) {
                    it.next().onDynamicPrivacyChanged();
                }
            }
            this.mCacheInvalid = false;
            return;
        }
        this.mCacheInvalid = true;
    }

    public boolean isDynamicPrivacyEnabled() {
        NotificationLockscreenUserManager notificationLockscreenUserManager = this.mLockscreenUserManager;
        return !notificationLockscreenUserManager.userAllowsPrivateNotificationsInPublic(notificationLockscreenUserManager.getCurrentUserId());
    }

    public boolean isDynamicallyUnlocked() {
        return (this.mKeyguardStateController.canDismissLockScreen() || this.mKeyguardStateController.isKeyguardGoingAway() || this.mKeyguardStateController.isKeyguardFadingAway()) && isDynamicPrivacyEnabled();
    }

    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }

    public boolean isInLockedDownShade() {
        int state;
        if (!isStatusBarKeyguardShowing() || !this.mKeyguardStateController.isMethodSecure() || (((state = this.mStateController.getState()) != 0 && state != 2) || !isDynamicPrivacyEnabled() || isDynamicallyUnlocked())) {
            return false;
        }
        return true;
    }

    public final boolean isStatusBarKeyguardShowing() {
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        return statusBarKeyguardViewManager != null && statusBarKeyguardViewManager.isShowing();
    }

    public void setStatusBarKeyguardViewManager(StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
    }
}
