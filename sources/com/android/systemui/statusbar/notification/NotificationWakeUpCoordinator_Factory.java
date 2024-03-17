package com.android.systemui.statusbar.notification;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationWakeUpCoordinator_Factory implements Factory<NotificationWakeUpCoordinator> {
    public final Provider<KeyguardBypassController> bypassControllerProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<HeadsUpManager> mHeadsUpManagerProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public NotificationWakeUpCoordinator_Factory(Provider<HeadsUpManager> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardBypassController> provider3, Provider<DozeParameters> provider4, Provider<ScreenOffAnimationController> provider5) {
        this.mHeadsUpManagerProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.bypassControllerProvider = provider3;
        this.dozeParametersProvider = provider4;
        this.screenOffAnimationControllerProvider = provider5;
    }

    public NotificationWakeUpCoordinator get() {
        return newInstance(this.mHeadsUpManagerProvider.get(), this.statusBarStateControllerProvider.get(), this.bypassControllerProvider.get(), this.dozeParametersProvider.get(), this.screenOffAnimationControllerProvider.get());
    }

    public static NotificationWakeUpCoordinator_Factory create(Provider<HeadsUpManager> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardBypassController> provider3, Provider<DozeParameters> provider4, Provider<ScreenOffAnimationController> provider5) {
        return new NotificationWakeUpCoordinator_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static NotificationWakeUpCoordinator newInstance(HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController) {
        return new NotificationWakeUpCoordinator(headsUpManager, statusBarStateController, keyguardBypassController, dozeParameters, screenOffAnimationController);
    }
}
