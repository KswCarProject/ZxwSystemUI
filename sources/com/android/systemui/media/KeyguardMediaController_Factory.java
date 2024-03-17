package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardMediaController_Factory implements Factory<KeyguardMediaController> {
    public final Provider<KeyguardBypassController> bypassControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<MediaHost> mediaHostProvider;
    public final Provider<NotificationLockscreenUserManager> notifLockscreenUserManagerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public KeyguardMediaController_Factory(Provider<MediaHost> provider, Provider<KeyguardBypassController> provider2, Provider<SysuiStatusBarStateController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<Context> provider5, Provider<ConfigurationController> provider6) {
        this.mediaHostProvider = provider;
        this.bypassControllerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.notifLockscreenUserManagerProvider = provider4;
        this.contextProvider = provider5;
        this.configurationControllerProvider = provider6;
    }

    public KeyguardMediaController get() {
        return newInstance(this.mediaHostProvider.get(), this.bypassControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.notifLockscreenUserManagerProvider.get(), this.contextProvider.get(), this.configurationControllerProvider.get());
    }

    public static KeyguardMediaController_Factory create(Provider<MediaHost> provider, Provider<KeyguardBypassController> provider2, Provider<SysuiStatusBarStateController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<Context> provider5, Provider<ConfigurationController> provider6) {
        return new KeyguardMediaController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static KeyguardMediaController newInstance(MediaHost mediaHost, KeyguardBypassController keyguardBypassController, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationLockscreenUserManager notificationLockscreenUserManager, Context context, ConfigurationController configurationController) {
        return new KeyguardMediaController(mediaHost, keyguardBypassController, sysuiStatusBarStateController, notificationLockscreenUserManager, context, configurationController);
    }
}
