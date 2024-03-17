package com.android.systemui.media;

import android.content.Context;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaHierarchyManager_Factory implements Factory<MediaHierarchyManager> {
    public final Provider<KeyguardBypassController> bypassControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardViewController> keyguardViewControllerProvider;
    public final Provider<MediaCarouselController> mediaCarouselControllerProvider;
    public final Provider<NotificationLockscreenUserManager> notifLockscreenUserManagerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public MediaHierarchyManager_Factory(Provider<Context> provider, Provider<SysuiStatusBarStateController> provider2, Provider<KeyguardStateController> provider3, Provider<KeyguardBypassController> provider4, Provider<MediaCarouselController> provider5, Provider<NotificationLockscreenUserManager> provider6, Provider<ConfigurationController> provider7, Provider<WakefulnessLifecycle> provider8, Provider<KeyguardViewController> provider9, Provider<DreamOverlayStateController> provider10) {
        this.contextProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.keyguardStateControllerProvider = provider3;
        this.bypassControllerProvider = provider4;
        this.mediaCarouselControllerProvider = provider5;
        this.notifLockscreenUserManagerProvider = provider6;
        this.configurationControllerProvider = provider7;
        this.wakefulnessLifecycleProvider = provider8;
        this.keyguardViewControllerProvider = provider9;
        this.dreamOverlayStateControllerProvider = provider10;
    }

    public MediaHierarchyManager get() {
        return newInstance(this.contextProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.bypassControllerProvider.get(), this.mediaCarouselControllerProvider.get(), this.notifLockscreenUserManagerProvider.get(), this.configurationControllerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.keyguardViewControllerProvider.get(), this.dreamOverlayStateControllerProvider.get());
    }

    public static MediaHierarchyManager_Factory create(Provider<Context> provider, Provider<SysuiStatusBarStateController> provider2, Provider<KeyguardStateController> provider3, Provider<KeyguardBypassController> provider4, Provider<MediaCarouselController> provider5, Provider<NotificationLockscreenUserManager> provider6, Provider<ConfigurationController> provider7, Provider<WakefulnessLifecycle> provider8, Provider<KeyguardViewController> provider9, Provider<DreamOverlayStateController> provider10) {
        return new MediaHierarchyManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static MediaHierarchyManager newInstance(Context context, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, MediaCarouselController mediaCarouselController, NotificationLockscreenUserManager notificationLockscreenUserManager, ConfigurationController configurationController, WakefulnessLifecycle wakefulnessLifecycle, KeyguardViewController keyguardViewController, DreamOverlayStateController dreamOverlayStateController) {
        return new MediaHierarchyManager(context, sysuiStatusBarStateController, keyguardStateController, keyguardBypassController, mediaCarouselController, notificationLockscreenUserManager, configurationController, wakefulnessLifecycle, keyguardViewController, dreamOverlayStateController);
    }
}
