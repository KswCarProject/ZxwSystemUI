package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationStackSizeCalculator_Factory implements Factory<NotificationStackSizeCalculator> {
    public final Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public NotificationStackSizeCalculator_Factory(Provider<SysuiStatusBarStateController> provider, Provider<LockscreenShadeTransitionController> provider2, Provider<Resources> provider3) {
        this.statusBarStateControllerProvider = provider;
        this.lockscreenShadeTransitionControllerProvider = provider2;
        this.resourcesProvider = provider3;
    }

    public NotificationStackSizeCalculator get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.lockscreenShadeTransitionControllerProvider.get(), this.resourcesProvider.get());
    }

    public static NotificationStackSizeCalculator_Factory create(Provider<SysuiStatusBarStateController> provider, Provider<LockscreenShadeTransitionController> provider2, Provider<Resources> provider3) {
        return new NotificationStackSizeCalculator_Factory(provider, provider2, provider3);
    }

    public static NotificationStackSizeCalculator newInstance(SysuiStatusBarStateController sysuiStatusBarStateController, LockscreenShadeTransitionController lockscreenShadeTransitionController, Resources resources) {
        return new NotificationStackSizeCalculator(sysuiStatusBarStateController, lockscreenShadeTransitionController, resources);
    }
}
