package com.android.systemui.statusbar.notification.interruption;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardNotificationVisibilityProviderImpl_Factory implements Factory<KeyguardNotificationVisibilityProviderImpl> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<HighPriorityProvider> highPriorityProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public KeyguardNotificationVisibilityProviderImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardStateController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<HighPriorityProvider> provider6, Provider<SysuiStatusBarStateController> provider7, Provider<BroadcastDispatcher> provider8, Provider<SecureSettings> provider9, Provider<GlobalSettings> provider10) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.keyguardStateControllerProvider = provider3;
        this.lockscreenUserManagerProvider = provider4;
        this.keyguardUpdateMonitorProvider = provider5;
        this.highPriorityProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.broadcastDispatcherProvider = provider8;
        this.secureSettingsProvider = provider9;
        this.globalSettingsProvider = provider10;
    }

    public KeyguardNotificationVisibilityProviderImpl get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.keyguardStateControllerProvider.get(), this.lockscreenUserManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.highPriorityProvider.get(), this.statusBarStateControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.secureSettingsProvider.get(), this.globalSettingsProvider.get());
    }

    public static KeyguardNotificationVisibilityProviderImpl_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardStateController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<HighPriorityProvider> provider6, Provider<SysuiStatusBarStateController> provider7, Provider<BroadcastDispatcher> provider8, Provider<SecureSettings> provider9, Provider<GlobalSettings> provider10) {
        return new KeyguardNotificationVisibilityProviderImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static KeyguardNotificationVisibilityProviderImpl newInstance(Context context, Handler handler, KeyguardStateController keyguardStateController, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardUpdateMonitor keyguardUpdateMonitor, HighPriorityProvider highPriorityProvider2, SysuiStatusBarStateController sysuiStatusBarStateController, BroadcastDispatcher broadcastDispatcher, SecureSettings secureSettings, GlobalSettings globalSettings) {
        return new KeyguardNotificationVisibilityProviderImpl(context, handler, keyguardStateController, notificationLockscreenUserManager, keyguardUpdateMonitor, highPriorityProvider2, sysuiStatusBarStateController, broadcastDispatcher, secureSettings, globalSettings);
    }
}
