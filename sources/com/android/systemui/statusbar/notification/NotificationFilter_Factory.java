package com.android.systemui.statusbar.notification;

import com.android.systemui.ForegroundServiceController;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationFilter_Factory implements Factory<NotificationFilter> {
    public final Provider<DebugModeFilterProvider> debugNotificationFilterProvider;
    public final Provider<ForegroundServiceController> foregroundServiceControllerProvider;
    public final Provider<NotificationEntryManager.KeyguardEnvironment> keyguardEnvironmentProvider;
    public final Provider<MediaFeatureFlag> mediaFeatureFlagProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<NotificationLockscreenUserManager> userManagerProvider;

    public NotificationFilter_Factory(Provider<DebugModeFilterProvider> provider, Provider<StatusBarStateController> provider2, Provider<NotificationEntryManager.KeyguardEnvironment> provider3, Provider<ForegroundServiceController> provider4, Provider<NotificationLockscreenUserManager> provider5, Provider<MediaFeatureFlag> provider6) {
        this.debugNotificationFilterProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.keyguardEnvironmentProvider = provider3;
        this.foregroundServiceControllerProvider = provider4;
        this.userManagerProvider = provider5;
        this.mediaFeatureFlagProvider = provider6;
    }

    public NotificationFilter get() {
        return newInstance(this.debugNotificationFilterProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardEnvironmentProvider.get(), this.foregroundServiceControllerProvider.get(), this.userManagerProvider.get(), this.mediaFeatureFlagProvider.get());
    }

    public static NotificationFilter_Factory create(Provider<DebugModeFilterProvider> provider, Provider<StatusBarStateController> provider2, Provider<NotificationEntryManager.KeyguardEnvironment> provider3, Provider<ForegroundServiceController> provider4, Provider<NotificationLockscreenUserManager> provider5, Provider<MediaFeatureFlag> provider6) {
        return new NotificationFilter_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static NotificationFilter newInstance(DebugModeFilterProvider debugModeFilterProvider, StatusBarStateController statusBarStateController, NotificationEntryManager.KeyguardEnvironment keyguardEnvironment, ForegroundServiceController foregroundServiceController, NotificationLockscreenUserManager notificationLockscreenUserManager, MediaFeatureFlag mediaFeatureFlag) {
        return new NotificationFilter(debugModeFilterProvider, statusBarStateController, keyguardEnvironment, foregroundServiceController, notificationLockscreenUserManager, mediaFeatureFlag);
    }
}
