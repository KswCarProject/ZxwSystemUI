package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SensitiveContentCoordinatorImpl_Factory implements Factory<SensitiveContentCoordinatorImpl> {
    public final Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public SensitiveContentCoordinatorImpl_Factory(Provider<DynamicPrivacyController> provider, Provider<NotificationLockscreenUserManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<StatusBarStateController> provider4, Provider<KeyguardStateController> provider5) {
        this.dynamicPrivacyControllerProvider = provider;
        this.lockscreenUserManagerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
    }

    public SensitiveContentCoordinatorImpl get() {
        return newInstance(this.dynamicPrivacyControllerProvider.get(), this.lockscreenUserManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardStateControllerProvider.get());
    }

    public static SensitiveContentCoordinatorImpl_Factory create(Provider<DynamicPrivacyController> provider, Provider<NotificationLockscreenUserManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<StatusBarStateController> provider4, Provider<KeyguardStateController> provider5) {
        return new SensitiveContentCoordinatorImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static SensitiveContentCoordinatorImpl newInstance(DynamicPrivacyController dynamicPrivacyController, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarStateController statusBarStateController, KeyguardStateController keyguardStateController) {
        return new SensitiveContentCoordinatorImpl(dynamicPrivacyController, notificationLockscreenUserManager, keyguardUpdateMonitor, statusBarStateController, keyguardStateController);
    }
}
