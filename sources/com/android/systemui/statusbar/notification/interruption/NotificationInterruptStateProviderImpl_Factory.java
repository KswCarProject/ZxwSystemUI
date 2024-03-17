package com.android.systemui.statusbar.notification.interruption;

import android.content.ContentResolver;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Handler;
import android.os.PowerManager;
import android.service.dreams.IDreamManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationInterruptStateProviderImpl_Factory implements Factory<NotificationInterruptStateProviderImpl> {
    public final Provider<AmbientDisplayConfiguration> ambientDisplayConfigurationProvider;
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<ContentResolver> contentResolverProvider;
    public final Provider<IDreamManager> dreamManagerProvider;
    public final Provider<NotifPipelineFlags> flagsProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<KeyguardNotificationVisibilityProvider> keyguardNotificationVisibilityProvider;
    public final Provider<NotificationInterruptLogger> loggerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NotificationFilter> notificationFilterProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public NotificationInterruptStateProviderImpl_Factory(Provider<ContentResolver> provider, Provider<PowerManager> provider2, Provider<IDreamManager> provider3, Provider<AmbientDisplayConfiguration> provider4, Provider<NotificationFilter> provider5, Provider<BatteryController> provider6, Provider<StatusBarStateController> provider7, Provider<HeadsUpManager> provider8, Provider<NotificationInterruptLogger> provider9, Provider<Handler> provider10, Provider<NotifPipelineFlags> provider11, Provider<KeyguardNotificationVisibilityProvider> provider12) {
        this.contentResolverProvider = provider;
        this.powerManagerProvider = provider2;
        this.dreamManagerProvider = provider3;
        this.ambientDisplayConfigurationProvider = provider4;
        this.notificationFilterProvider = provider5;
        this.batteryControllerProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.headsUpManagerProvider = provider8;
        this.loggerProvider = provider9;
        this.mainHandlerProvider = provider10;
        this.flagsProvider = provider11;
        this.keyguardNotificationVisibilityProvider = provider12;
    }

    public NotificationInterruptStateProviderImpl get() {
        return newInstance(this.contentResolverProvider.get(), this.powerManagerProvider.get(), this.dreamManagerProvider.get(), this.ambientDisplayConfigurationProvider.get(), this.notificationFilterProvider.get(), this.batteryControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.headsUpManagerProvider.get(), this.loggerProvider.get(), this.mainHandlerProvider.get(), this.flagsProvider.get(), this.keyguardNotificationVisibilityProvider.get());
    }

    public static NotificationInterruptStateProviderImpl_Factory create(Provider<ContentResolver> provider, Provider<PowerManager> provider2, Provider<IDreamManager> provider3, Provider<AmbientDisplayConfiguration> provider4, Provider<NotificationFilter> provider5, Provider<BatteryController> provider6, Provider<StatusBarStateController> provider7, Provider<HeadsUpManager> provider8, Provider<NotificationInterruptLogger> provider9, Provider<Handler> provider10, Provider<NotifPipelineFlags> provider11, Provider<KeyguardNotificationVisibilityProvider> provider12) {
        return new NotificationInterruptStateProviderImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static NotificationInterruptStateProviderImpl newInstance(ContentResolver contentResolver, PowerManager powerManager, IDreamManager iDreamManager, AmbientDisplayConfiguration ambientDisplayConfiguration, NotificationFilter notificationFilter, BatteryController batteryController, StatusBarStateController statusBarStateController, HeadsUpManager headsUpManager, NotificationInterruptLogger notificationInterruptLogger, Handler handler, NotifPipelineFlags notifPipelineFlags, KeyguardNotificationVisibilityProvider keyguardNotificationVisibilityProvider2) {
        return new NotificationInterruptStateProviderImpl(contentResolver, powerManager, iDreamManager, ambientDisplayConfiguration, notificationFilter, batteryController, statusBarStateController, headsUpManager, notificationInterruptLogger, handler, notifPipelineFlags, keyguardNotificationVisibilityProvider2);
    }
}
